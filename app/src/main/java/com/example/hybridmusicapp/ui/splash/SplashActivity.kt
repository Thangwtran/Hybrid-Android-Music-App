package com.example.hybridmusicapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.ActivitySplashBinding
import com.example.hybridmusicapp.ui.onboarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewpager()
    }
    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        actionBar?.hide()
    }

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