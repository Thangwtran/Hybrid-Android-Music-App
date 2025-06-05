package com.example.hybridmusicapp.ui.now_playing

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.hybridmusicapp.databinding.ActivityPlayerBinding
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPlayerBinding
    private var nowPlayingViewModel: NowPlayingViewModel? = NowPlayingViewModel.instance
    private var mediaController: MediaController? = null
    private var playerListener: Player.Listener? = null
    private val playerViewModel by viewModels<PlayerViewModel>()
    private var handler: Handler? = null
    private lateinit var callback: Runnable
    private lateinit var pressAnimator: Animator
    private lateinit var rotationAnimator: ObjectAnimator

    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.parseColor("#99000000")
        supportActionBar?.hide()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolBar()
        setupViewModel()
        setupMediaController()
        setupAnimator()
        setupListeners()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupListeners() {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    private fun setupAnimator() {
        TODO("Not yet implemented")
    }

    private fun setupMediaController() {
        TODO("Not yet implemented")
    }

    private fun setupViewModel() {
        TODO("Not yet implemented")
    }

    private fun setupToolBar() {
        binding.playerToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


}