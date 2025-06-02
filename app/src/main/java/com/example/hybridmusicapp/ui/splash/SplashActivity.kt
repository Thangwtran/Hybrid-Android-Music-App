package com.example.hybridmusicapp.ui.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hybridmusicapp.databinding.ActivitySplashBinding
import com.example.hybridmusicapp.ui.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewpager()
    }

//    override fun onResume() {
//        super.onResume()
//        // Hide the status bar.
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        actionBar?.hide()
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        actionBar?.hide()
//    }

    private fun setupViewpager() {
        val pagerAdapter = ViewPagerAdapter(this)
        val splashPager = binding.splashPager
        splashPager.adapter = pagerAdapter
        binding.dotsIndicator.attachTo(splashPager)
        binding.btnSplash.setOnClickListener {
            Log.d("TAG", "setupViewpager: ${splashPager.currentItem}")
            if(splashPager.currentItem == splashPager.adapter?.itemCount?.minus(1)){
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
            }else{
                splashPager.currentItem += 1
            }
        }
//        splashPager.isUserInputEnabled = false
    }
}