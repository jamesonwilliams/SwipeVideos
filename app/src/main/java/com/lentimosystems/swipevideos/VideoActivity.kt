package com.lentimosystems.swipevideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.lentimosystems.swipevideos.VideoActivity.VideosAdapter.VideoViewHolder
import com.lentimosystems.swipevideos.databinding.ActivityVideoBinding
import com.lentimosystems.swipevideos.databinding.ItemVideosContainerBinding

class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoItems = intent.extras!!.getParcelableArrayList<VideoItem>("videos")!!
        val adapter = VideosAdapter()
        binding.viewPagerVideos.adapter = adapter
        adapter.loadVideos(videoItems)
    }

    class VideosAdapter : Adapter<VideoViewHolder>() {
        private val videoItems = mutableListOf<VideoItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemVideosContainerBinding.inflate(inflater, parent, false)
            return VideoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            holder.setVideoData(videoItems[position])
        }

        override fun getItemCount(): Int {
            return videoItems.size
        }

        fun loadVideos(videos: List<VideoItem>) {
            videoItems.clear()
            videoItems.addAll(videos)
            notifyDataSetChanged()
        }

        class VideoViewHolder(private var binding: ItemVideosContainerBinding) : ViewHolder(binding.root) {
            fun setVideoData(videoItem: VideoItem) {
                binding.txtTitle.text = videoItem.title
                binding.txtDesc.text = videoItem.description
                binding.videoView.setVideoPath(videoItem.resourceLocation)
                binding.videoView.setOnPreparedListener { mp ->
                    binding.progressBar.visibility = View.GONE
                    mp.start()
                    val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
                    val screenRatio = binding.videoView.width / binding.videoView.height.toFloat()
                    val scale = videoRatio / screenRatio
                    if (scale >= 1f) {
                        binding.videoView.scaleX = scale
                    } else {
                        binding.videoView.scaleY = 1f / scale
                    }
                }
                binding.videoView.setOnCompletionListener { mp -> mp.start() }
                binding.root.setOnClickListener {
                    if (binding.videoView.isPlaying && binding.videoView.canPause()) {
                        binding.videoView.pause()
                    } else {
                        binding.videoView.resume()
                    }
                }
            }
        }
    }
}