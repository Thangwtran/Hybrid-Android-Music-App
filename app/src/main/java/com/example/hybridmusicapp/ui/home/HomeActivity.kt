package com.example.hybridmusicapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.ActivityHomeBinding
import com.example.hybridmusicapp.ui.discovery.DiscoveryFragment
import com.example.hybridmusicapp.ui.home.ncs.NcsViewModel
import com.example.hybridmusicapp.ui.library.LibraryFragment
import com.example.hybridmusicapp.ui.setting.SettingFragment
import kotlin.getValue

class HomeActivity : AppCompatActivity() {
    private val tag = "HomeActivity"
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        val application = application as MusicApplication
        val songRepository = application.songRepository
        val albumRepository = application.albumRepository
        HomeViewModel.Factory(songRepository, albumRepository)
    }
    private val ncsViewModel by viewModels<NcsViewModel> {
        val application = application as MusicApplication
        val ncsRepository = application.ncsRepository
        NcsViewModel.Factory(ncsRepository)
    }

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

    companion object {
        const val EXTRA_GENRE_RECOMMENDED = "EXTRA_GENRE_RECOMMENDED"
    }
}