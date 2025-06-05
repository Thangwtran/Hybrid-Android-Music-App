package com.example.hybridmusicapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.ui.home.album.AlbumViewModel
import com.example.hybridmusicapp.ui.home.HomeViewModel
import com.example.hybridmusicapp.ui.viewmodel.ArtistViewModel
import com.example.hybridmusicapp.ui.viewmodel.NcsViewModel
import com.example.hybridmusicapp.utils.NCSUtils

class LoadingActivity : AppCompatActivity() {
    // homeViewModel
    private val homeViewModel by viewModels<HomeViewModel> {
        val application = application as MusicApplication
        val songRepository = application.songRepository
        val albumRepository = application.albumRepository
        HomeViewModel.Factory(songRepository, albumRepository)
    }

    private val artistViewModel by viewModels<ArtistViewModel> {
        val application = application as MusicApplication
        val artistRepository = application.artistRepository
        ArtistViewModel.Factory(artistRepository)
    }

    private val albumViewModel by viewModels<AlbumViewModel> {
        val application = application as MusicApplication
        val albumRepository = application.albumRepository
        AlbumViewModel.Factory(albumRepository)
    }

    private val ncsViewModel by viewModels<NcsViewModel> {
        val application = application as MusicApplication
        val ncsRepository = application.ncsRepository
        NcsViewModel.Factory(ncsRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        enableEdgeToEdge()
        if(isFirstLaunch()){
            insertNcsSong()
            insertArtist()
            setFirstLaunchFalse()
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }

    }

    private fun insertArtist() {
        // artist
        artistViewModel.getArtists()
        homeViewModel.loadRemoteSongs()
        artistViewModel.remoteArtist.observe(this) { artists ->
            Log.i("LoadingActivity", "artists: $artists")
            if (artists != null) {
                artistViewModel.saveArtistToDB(artists)
                artistViewModel.saveArtistSongCrossRef(homeViewModel.remoteSongs, artists)
            } else {
                Toast.makeText(this, "Load Artist Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun insertNcsSong() {
        val ncsSongs = NCSUtils.initData(applicationContext)
        ncsViewModel.insert(ncsSongs)
        ncsViewModel.ncsSongs.observe(this) { ncsSongs ->
            if (ncsSongs.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000)
                Toast.makeText(
                    this,
                    "NCS songs loaded successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Error loading ncs songs",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isFirstLaunch(): Boolean {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return prefs.getBoolean("isFirstLaunch", true)
    }

    private fun setFirstLaunchFalse() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("isFirstLaunch", false).apply()
    }
}

