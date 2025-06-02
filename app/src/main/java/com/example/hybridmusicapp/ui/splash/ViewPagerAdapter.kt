package com.example.hybridmusicapp.ui.splash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hybridmusicapp.R

class ViewPagerAdapter(fragment: FragmentActivity):FragmentStateAdapter(fragment) {

    private val pages = listOf(
        SplashFragment.newInstance(
            fragment.getString(R.string.onboarding_title1),
            fragment.getString(R.string.onboarding_des1),
            R.drawable.bg_5
        ),
        SplashFragment.newInstance(
            fragment.getString(R.string.onboarding_title2),
            fragment.getString(R.string.onboarding_des2),
            R.drawable.bg_13
        ),
        SplashFragment.newInstance(
            fragment.getString(R.string.onboarding_title3),
            fragment.getString(R.string.onboarding_des3),
            R.drawable.bg_9
        )
    )
    override fun getItemCount(): Int {
      return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}