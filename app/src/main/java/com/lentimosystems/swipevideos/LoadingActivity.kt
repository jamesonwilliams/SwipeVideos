package com.lentimosystems.swipevideos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.lentimosystems.swipevideos.R.string

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.BufferedSink
import okio.buffer
import okio.sink

import org.json.JSONArray

import java.io.File
import java.io.IOException
import java.net.URL
import java.util.ArrayList

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Suppress("BlockingMethodInNonBlockingContext")
class LoadingActivity : AppCompatActivity() {
    private lateinit var http: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        http = OkHttpClient()
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch(Dispatchers.IO) {
            val downloadJobs = downloadPlaylist().map { async { downloadVideo(it) } }
            val videos = ArrayList(downloadJobs.awaitAll())
            val intent = Intent(applicationContext, VideoActivity::class.java)
            intent.putExtra("videos", videos)
            GlobalScope.launch(Dispatchers.Main) {
                startActivityForResult(intent, 0)
            }
        }
    }

    private suspend fun downloadVideo(video: VideoItem): VideoItem {
        Log.d(TAG, "About to download $video.")

        val filename = URL(video.resourceLocation).path.replace('/', '_')
        val file = File(applicationContext.filesDir, filename)

        val response = download(video.resourceLocation)
        val sink: BufferedSink = file.sink().buffer()
        sink.writeAll(response.body!!.source())
        sink.close()

        return VideoItem(file.absolutePath, video.title, video.description)
    }

    private suspend fun downloadPlaylist(): List<VideoItem> {
        val playlistUrl = resources.getString(string.playlist_url)
        val response = download(playlistUrl)
        val jsonArray = JSONArray(response.body!!.string())
        val videoItems = mutableListOf<VideoItem>()
        for (index in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(index)
            videoItems.add(VideoItem(
                    item.getString("url"),
                    item.getString("title"),
                    item.getString("description")
            ))
        }
        return videoItems
    }

    private suspend fun download(url: String): Response {
        return suspendCoroutine { continuation ->
            http.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }
            })
        }
    }

    companion object {
        @JvmStatic
        private val TAG = LoadingActivity::class.simpleName
    }
}
