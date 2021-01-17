package com.lentimosystems.swipevideos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

import com.lentimosystems.swipevideos.R.id
import com.lentimosystems.swipevideos.R.layout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val videosViewPager = findViewById<ViewPager2>(id.viewPagerVideos)
        videosViewPager.adapter = VideosAdapter(listOf(
                VideoItem(
                        videoURL = "https://liciolentimo.co.ke/img/videos/facebook.mp4",
                        videoTitle = "Women In Tech",
                        videoDesc = "International Women's Day 2019"
                ),
                VideoItem(
                        videoURL = "https://liciolentimo.co.ke/img/videos/facebook3.mp4",
                        videoTitle = "Sasha Solomon",
                        videoDesc = "How Sasha Solomon Became a Software Developer at Twitter"
                ),
                VideoItem(
                        videoURL = "https://liciolentimo.co.ke/img/videos/facebook2.mp4",
                        videoTitle = "Happy Hour Wednesday",
                        videoDesc = " Depth-First Search Algorithm"
                )
        ))
    }
}