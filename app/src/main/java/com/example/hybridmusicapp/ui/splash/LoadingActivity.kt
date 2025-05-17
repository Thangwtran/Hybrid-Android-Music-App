package com.example.hybridmusicapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.ui.discovery.artist.ArtistViewModel
import com.example.hybridmusicapp.ui.home.HomeViewModel
import com.example.hybridmusicapp.ui.home.album.AlbumViewModel
import com.example.hybridmusicapp.ui.home.ncs.NcsViewModel
import com.example.hybridmusicapp.utils.NCSUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {
    // homeViewModel
    private val homeViewModel by viewModels<HomeViewModel>{
        val application = application as MusicApplication
        val songRepository = application.songRepository
        val albumRepository = application.albumRepository
        HomeViewModel.Factory(songRepository, albumRepository)
    }

    private val artistViewModel by viewModels<ArtistViewModel>{
        val application = application as MusicApplication
        val artistRepository = application.artistRepository
        ArtistViewModel.Factory(artistRepository)
    }

    private val albumViewModel by viewModels<AlbumViewModel>{
        val application = application as MusicApplication
        val albumRepository = application.albumRepository
        AlbumViewModel.Factory(albumRepository)
    }

    private val ncsViewModel by viewModels<NcsViewModel>{
        val application = application as MusicApplication
        val ncsRepository = application.ncsRepository
        NcsViewModel.Factory(ncsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        enableEdgeToEdge()
//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, SplashActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 4000)

//        homeViewModel.remoteSongLoaded.observe(this){isLoaded->
//            if(isLoaded){
//                val songs = homeViewModel.remoteSongs
//                homeViewModel.addSongToFireStore(songs)
////                val intent = Intent(this, SplashActivity::class.java)
////                startActivity(intent)
////                finish()
//                Toast.makeText(this, "Songs loaded successfully", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "Error loading songs", Toast.LENGTH_SHORT).show()
//            }
//        }

//        artistViewModel.loadRemoteArtist()
//        artistViewModel.remoteArtist.observe(this){artists->
//            if(artists != null){
//                artistViewModel.addArtistsToFireStore(artists)
//                val intent = Intent(this, SplashActivity::class.java)
//                startActivity(intent)
//                finish()
//                Toast.makeText(this, "Artists loaded successfully", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "Error loading artists", Toast.LENGTH_SHORT).show()
//            }
//        }

//        albumViewModel.loadAlbums()
//        albumViewModel.albums.observe(this) { albums ->
//            if(albums != null){
//                albumViewModel.addAlbumToFireStore(albums)
//                val intent = Intent(this, SplashActivity::class.java)
//                startActivity(intent)
//                finish()
//                Toast.makeText(this, "Albums loaded successfully", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "Error loading albums", Toast.LENGTH_SHORT).show()
//            }
//        }

        val ncsSongs = NCSUtils.initData(applicationContext)
        ncsViewModel.insert(ncsSongs)
        ncsViewModel.ncsSongs.observe(this) {ncsSongs->
            if(ncsSongs.isNotEmpty()){
                setFirstLaunchFalse()
                Toast.makeText(this, "NCS songs loaded successfully", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 4000)

            }else{
                Toast.makeText(this, "Error loading ncs songs", Toast.LENGTH_SHORT).show()
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