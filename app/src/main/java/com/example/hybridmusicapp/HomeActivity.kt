package com.example.hybridmusicapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.databinding.ActivityHomeBinding
import com.example.hybridmusicapp.ui.discovery.DiscoveryFragment
import com.example.hybridmusicapp.ui.home.HomeFragment
import com.example.hybridmusicapp.ui.home.HomeViewModel
import com.example.hybridmusicapp.ui.home.album.AlbumViewModel
import com.example.hybridmusicapp.ui.library.LibraryFragment
import com.example.hybridmusicapp.ui.setting.SettingFragment
import com.example.hybridmusicapp.ui.viewmodel.ArtistViewModel
import com.example.hybridmusicapp.ui.viewmodel.NcsViewModel
import com.example.hybridmusicapp.ui.viewmodel.NetworkViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel
import com.example.hybridmusicapp.ui.viewmodel.PlaylistViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    private val tag = "HomeActivity"
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharePreferences: SharedPreferences
    private lateinit var networkViewModel: NetworkViewModel

    private val nowPlayingViewModel = NowPlayingViewModel.instance
    private val albumViewModel by viewModels<AlbumViewModel> {
        val application = application as MusicApplication
        AlbumViewModel.Factory(application.albumRepository)
    }
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
    private val artistViewModel by viewModels<ArtistViewModel> {
        val application = application as MusicApplication
        val artistRepository = application.artistRepository
        ArtistViewModel.Factory(artistRepository)
    }
    private val playlistViewModel by viewModels<PlaylistViewModel> {
        val application = application as MusicApplication
        val playlistRepository = application.playlistRepository
        PlaylistViewModel.Factory(playlistRepository)
    }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                val snackBar =
                    Snackbar.make(
                        binding.root,
                        getString(R.string.err_permission_denied),
                        Snackbar.LENGTH_SHORT
                    )
                snackBar.show()
            } else {
                val internetAccess = MusicAppUtils.isNetworkAvailable(applicationContext)
                if (internetAccess) {
                    PermissionViewModel.Companion.instance.setPermissionGranted(true)
                }
            }
        }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.parseColor("#99000000")
        supportActionBar?.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupComponents()
        setupObservers()
        setupBottomNav()
    }

    private fun setupComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30 (Android 11) trở lên
            val windowMetrics = windowManager.currentWindowMetrics
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34 (Android 14) trở lên
                MusicAppUtils.DENSITY = windowMetrics.density
            } else {
                /**
                 * Trên Android 11 đến Android 13, tính density bằng cách chia chiều rộng màn hình (bounds.width)
                 * cho giá trị mật độ mặc định (DENSITY_DEFAULT, thường là 160dpi).
                 * Đây là cách tương thích để tính density trên các phiên bản cũ hơn.
                 */
                MusicAppUtils.DENSITY =
                    1f * windowMetrics.bounds.width() / DisplayMetrics.DENSITY_DEFAULT

            }
        } else {
            /**
             * Trên các phiên bản Android cũ hơn (API < 30), sử dụng DisplayMetrics để lấy thông tin màn hình.
             * DisplayMetrics là cách truyền thống để lấy density trên các phiên bản Android thấp hơn.
             */
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            MusicAppUtils.DENSITY = displayMetrics.density
        }
        /**
         * Khởi tạo SharedPreferences để lưu trữ thông tin bài hát đang phát hoặc danh sách phát.
         * MODE_PRIVATE đảm bảo rằng file SharedPreferences chỉ có thể được truy cập bởi ứng dụng này.
         */
        sharePreferences =
            applicationContext.getSharedPreferences(getString(R.string.pref_file_key), MODE_PRIVATE)

        networkViewModel = NetworkViewModel.Companion.instance
        // TODO: Load artist remote
    }

    private fun setupObservers() {
        // permission
        PermissionViewModel.Companion.instance.permissionAsked.observe(this) { isAsked -> // oke
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isAsked) { // đã hỏi
                checkPermission() // check xem có cấp quyền không
            } else {
                // Trên API < 33, không cần quyền POST_NOTIFICATIONS, thông báo được hiển thị tự do
                PermissionViewModel.Companion.instance.setPermissionGranted(true)
                Toast.makeText(this, "Permission granted in Android 9", Toast.LENGTH_SHORT).show()
            }
        }

//        // artist
//        artistViewModel.getArtists()
//        artistViewModel.remoteArtist.observe(this) { artists ->
//            if (artists != null) {
//                artistViewModel.saveArtistToDB(artists)
//                artistViewModel.saveArtistSongCrossRef(homeViewModel.remoteSongs, artists)
//            } else {
//                Toast.makeText(this, "Load Artist Error", Toast.LENGTH_LONG).show()
//            }
//        }


        // now playing - mini player
        nowPlayingViewModel?.isMiniPlayerVisible?.observe(this) {
            Log.d("HomeActivity", "isMiniPlayerVisible: $it")
            if (it) {
                binding.fragmentContainerMiniPlayer.visibility = View.VISIBLE
            } else {
                binding.fragmentContainerMiniPlayer.visibility = View.GONE
            }
        }

        // home
        val isNetworkAccess = MusicAppUtils.isNetworkAvailable(this)
        if(isNetworkAccess){
            homeViewModel.loadRemoteSongs()
            homeViewModel.remoteSongLoaded.observe(this) { isLoaded ->
                if (isLoaded) {
                    saveSongData()
                } else {
                    Toast.makeText(this, "Load Song Error", Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show()
            // TODO: show dialog 
        }
       

        // ncs playlist
        ncsViewModel.getNCSongs()
        ncsViewModel.ncsSongs.observe(this) { ncsSongList ->
            nowPlayingViewModel?.setupNcsPlaylist(
                this,
                ncsSongList,
                MusicAppUtils.DefaultPlaylistName.NCS_SONG.value
            )
            playlistViewModel.setNcsPlaylist(ncsSongs = ncsSongList)
        }

        // recommend

        // search playlist
        nowPlayingViewModel?.historySearchSongs?.observe(this) { songs ->
            nowPlayingViewModel?.setupPlaylist(
                songs,
                MusicAppUtils.DefaultPlaylistName.SEARCHED.value
            )
        }

        // list playlists
        playlistViewModel.loadPlaylistWithSongs()
        playlistViewModel.playlists.observe(this) { playlistWithSongs ->
            playlistViewModel.setListPlaylist(playlistWithSongs)
            nowPlayingViewModel?.setPlaylistWithSongs(playlistWithSongs)
        }

        observerLocalData()

    }

    private fun saveSongData() {
        homeViewModel.saveSongToDB(object : ResultCallback<Boolean> {
            override fun onResult(result: Boolean) {
                if (result) {
                    homeViewModel.loadLocalSongs()
                } else {
                    Toast.makeText(this@HomeActivity, "Save Song Error", Toast.LENGTH_LONG).show()
                }
            }
        })
        observerLocalData()
    }

    private fun observerLocalData() {
        homeViewModel.localSongs.observe(this) { songs ->
            nowPlayingViewModel?.setupPlaylist(
                songs, MusicAppUtils.DefaultPlaylistName.DEFAULT.value
            )
        }

        // TODO: save recommend to local and observe

        ncsViewModel.ncsSongs.observe(this) { ncsSongs -> // oke
            nowPlayingViewModel?.setupNcsPlaylist(
                this,
                ncsSongs,
                MusicAppUtils.DefaultPlaylistName.NCS_SONG.value
            )
        }
    }

    private fun saveCurrentSong() {
        val playingSong = nowPlayingViewModel?.playingSong?.value
        if (playingSong != null) {
            val song = playingSong.song
            if (song != null) {
                sharePreferences.edit()
                    .putString(PREF_SONG_ID, song.id)
                    .putString(PREF_PLAYLIST_NAME, playingSong.playlist!!.name)
                    .apply()
            }
        }
    }

    private fun registerNetworkCallBack() {
        val connectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    super.onBlockedStatusChanged(network, blocked)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                }
            })
    }

    private fun loadPrevSessionPlayingSong() {
        val songId = sharePreferences.getString(PREF_SONG_ID, null)
        val playlistName = sharePreferences.getString(PREF_PLAYLIST_NAME, null)
        homeViewModel.localSongs.observe(this) { songs ->
            if (!songs.isNullOrEmpty()) {
                nowPlayingViewModel?.loadPrevSessionPlayingSong(songId, playlistName)
            }
        }
    }

    private fun checkPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        val permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        if (permissionGranted) {
            PermissionViewModel.Companion.instance.setPermissionGranted(true)
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            val snackBar = Snackbar.make(
                binding.root.rootView, getString(R.string.permission_desc),
                Snackbar.LENGTH_SHORT
            )
            snackBar.setAction("YES") { resultLauncher.launch(permission) }
            snackBar.show()
        } else {
            resultLauncher.launch(permission)
        }
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

                R.id.navigation_setting -> {
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
        const val PREF_SONG_ID: String = "SONG_ID"
        const val PREF_PLAYLIST_NAME: String = "PLAYLIST_NAME"
    }
}