package com.lentimosystems.swipevideos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.lentimosystems.swipevideos.R.id
import com.lentimosystems.swipevideos.R.layout
import com.lentimosystems.swipevideos.VideosAdapter.VideoViewHolder

class VideosAdapter(private val mVideoItems: List<VideoItem>) : Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context)
                .inflate(layout.item_videos_container, parent, false))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.setVideoData(mVideoItems[position])
    }

    override fun getItemCount(): Int {
        return mVideoItems.size
    }

    class VideoViewHolder(itemView: View) : ViewHolder(itemView) {
        private var videoView: VideoView = itemView.findViewById(id.videoView)
        private var txtTitle: TextView = itemView.findViewById(id.txtTitle)
        private var txtDesc: TextView = itemView.findViewById(id.txtDesc)
        private var progressBar: ProgressBar = itemView.findViewById(id.progressBar)

        fun setVideoData(videoItem: VideoItem) {
            txtTitle.text = videoItem.videoTitle
            txtDesc.text = videoItem.videoDesc
            videoView.setVideoPath(videoItem.videoURL)
            videoView.setOnPreparedListener { mp ->
                progressBar.visibility = View.GONE
                mp.start()
                val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
                val screenRatio = videoView.width / videoView.height.toFloat()
                val scale = videoRatio / screenRatio
                if (scale >= 1f) {
                    videoView.scaleX = scale
                } else {
                    videoView.scaleY = 1f / scale
                }
            }
            videoView.setOnCompletionListener { mp -> mp.start() }
        }
    }
}
