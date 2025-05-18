package com.example.hybridmusicapp.ui.home

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.ActivityHomeBinding
import com.example.hybridmusicapp.ui.discovery.DiscoveryFragment
import com.example.hybridmusicapp.ui.library.LibraryFragment
import com.example.hybridmusicapp.ui.setting.SettingFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val bottomNav = binding.bottomNav
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.navigation_library -> {
                    replaceFragment(LibraryFragment())
                    true
                }

                R.id.navigation_discovery -> {
                    replaceFragment(DiscoveryFragment())
                    true
                }

                R.id.navigation_setting ->{
                    replaceFragment(SettingFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setReorderingAllowed(true)
            .commit()
    }
}