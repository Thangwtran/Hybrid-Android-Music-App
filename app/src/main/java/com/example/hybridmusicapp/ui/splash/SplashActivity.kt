package com.example.hybridmusicapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewpager()
    }

    private fun setupViewpager() {
        val pagerAdapter = ViewPagerAdapter(this)
        val splashPager = binding.splashPager
        splashPager.adapter = pagerAdapter
        binding.dotsIndicator.attachTo(splashPager)
        binding.btnSplash.setOnClickListener {
            Log.d("TAG", "setupViewpager: ${splashPager.currentItem}")
            if(splashPager.currentItem == splashPager.adapter?.itemCount?.minus(1)){
                binding.btnSplash.text = getString(R.string.get_started)
            }else{
                splashPager.currentItem += 1
            }
        }
        splashPager.isUserInputEnabled = false
    }
}