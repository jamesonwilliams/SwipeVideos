package com.lentimosystems.swipevideos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.lentimosystems.swipevideos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPagerVideos.adapter = VideosAdapter(listOf(
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