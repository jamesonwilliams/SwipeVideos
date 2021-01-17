package com.lentimosystems.swipevideos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lentimosystems.swipevideos.databinding.ActivityMainBinding
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = VideosAdapter()
        binding.viewPagerVideos.adapter = adapter

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val videoItems = downloadPlaylist()
                withContext(Dispatchers.Main) {
                    adapter.loadVideos(videoItems)
                }
            }
        }
    }

    private suspend fun downloadPlaylist(): List<VideoItem> {
        val videosUrl = "https://www.dropbox.com/s/fd4zf3ejsojnj2w/playlist.json?dl=1"
        val client = HttpClient(OkHttp)
        val jsonArray = JSONArray(client.get<String>(videosUrl))
        val videoItems = mutableListOf<VideoItem>()
        for (index in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(index)
            videoItems.add(VideoItem(
                    item.getString("url"),
                    item.getString("title"),
                    item.getString("description")
            ))
        }
        client.close()
        return videoItems
    }
}
