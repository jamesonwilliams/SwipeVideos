package com.lentimosystems.swipevideos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.lentimosystems.swipevideos.VideosAdapter.VideoViewHolder
import com.lentimosystems.swipevideos.databinding.ItemVideosContainerBinding

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
            binding.videoView.setVideoPath(videoItem.url)
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
        }
    }
}
