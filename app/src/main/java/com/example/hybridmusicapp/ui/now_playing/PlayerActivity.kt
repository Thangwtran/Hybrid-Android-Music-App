package com.example.hybridmusicapp.ui.now_playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.ActivityPlayerBinding
import com.example.hybridmusicapp.ui.viewmodel.AnimationViewModel
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NetworkViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils

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
        nowPlayingViewModel?.setMiniPlayerVisible(true)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Listener
    private fun setupListeners() {
        binding.btnPlayPause.setOnClickListener(this)
        binding.btnPlayerShuffle.setOnClickListener(this)
        binding.btnPlayerRepeat.setOnClickListener(this)
        binding.btnPlayerNext.setOnClickListener(this)
        binding.btnPlayerPrevious.setOnClickListener(this)

        binding.seekBarPlayer.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    mediaController!!.seekTo(progress.toLong())
                }
                binding.tvPlayerCurrentDuration.text =
                    playerViewModel.miliToSecond(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        // TODO: check internet
    }

    private fun checkAndShowInternetState() {}

    override fun onClick(v: View) {
        pressAnimator.setTarget(v)  // set animation click
        val internetAccess = MusicAppUtils.isNetworkAvailable(applicationContext)
        if (internetAccess) {
            pressAnimator.start()
            when (v) {
                binding.btnPlayerShuffle -> setupShuffleAction()
                binding.btnPlayPause -> setupPlayPauseAction()
                binding.btnPlayerRepeat -> setupRepeatAction()
//                binding.btnPlayerFavorite -> setupFavorite()
                binding.btnPlayerNext -> setupSkipNext()
                binding.btnPlayerPrevious -> setupSkipPrev()
//                binding.btnPlayerShare -> {
//                    val message = getString(R.string.message_not_implemented)
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                }
            }
        } else {
            pressAnimator.pause()
            val titleId = R.string.title_no_internet
            val messageId = R.string.message_no_internet
            // showNoInternetDialog(titleId, messageId)
        }
    }

    private fun setupShuffleAction() {}
    private fun setupPlayPauseAction() {}
    private fun setupRepeatAction() {}
    private fun setupSkipNext() {}
    private fun setupSkipPrev() {}


    // Animator
    private fun setupAnimator() {
        pressAnimator = AnimatorInflater.loadAnimator(this, R.animator.button_pressed)

        rotationAnimator =
            ObjectAnimator.ofFloat(binding.imgPlayerArtworkCircle, "rotation", 360f, 0f)
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.setDuration(12000)
        rotationAnimator.repeatMode = ValueAnimator.RESTART // restart when finish a round rotation
        rotationAnimator.repeatCount = ValueAnimator.INFINITE
    }


    // Media Controller
    private fun setupMediaController() {
        MediaViewModel.instance.mediaController.observe(this) {
            mediaController = it
            nowPlayingViewModel?.playingSong?.observe(this) { playingSong ->
                val song = playingSong?.song
                val ncs = playingSong?.ncSong
                Log.i("PlayerActivity", "songFav: ${song?.isFavorite}")
                Log.i("PlayerActivity", "songFav: ${ncs?.isFavourite}")

                if (song != null || ncs != null) {
                    updateSeekBar()
                    showSongNcs(song, ncs, playingSong.isNcsSong)
                } else {
                    throw Exception("null exception")
                }
            }
            setupController()
        }
    }

    private fun updateSeekBar() {}

    private fun showSongNcs(song: Song?, ncs: NCSong?, isNcs: Boolean) {
        if (mediaController != null) {
            updateSeekBarMaxValue()
            updateDuration() // set duration time
            if (isNcs) {
                showNcs(ncs)
            } else {
                showSong(song)
            }
            showRepeatMode()
            showFavorite(song, ncs,isNcs)
            showShuffleState(mediaController!!.shuffleModeEnabled)
        }
    }

    private fun updateSeekBarMaxValue() {}
    private fun showSong(song: Song?) {
        binding.tvPlayerAlbum.text = song?.album
        binding.tvPlayerArtist.text = song?.artist
        with(binding.tvPlayerSongName) {
            text = song?.title
            isSelected = true
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1 // marquee_forever
            maxLines = 1
            isFocusable = true
            isFocusableInTouchMode = true
            setHorizontallyScrolling(true)
            requestFocus()
        }
        with(binding.tvPlayerDesc) {
            text = song?.title
            isSelected = true
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1 // marquee_forever
            maxLines = 1
            isFocusable = true
            isFocusableInTouchMode = true
            setHorizontallyScrolling(true)
            requestFocus()
        }
        Glide.with(this)
            .load(song?.image)
            .error(R.drawable.itunes)
            .into(binding.imgPlayerArtwork)
        Glide.with(this)
            .load(song?.image)
            .circleCrop()
            .error(R.drawable.itunes)
            .into(binding.imgPlayerArtworkCircle)
    }

    private fun showNcs(ncs: NCSong?) {
        binding.tvPlayerAlbum.text = ncs?.artist
        binding.tvPlayerArtist.text = ncs?.artist
        with(binding.tvPlayerSongName) {
            text = ncs?.ncsName
            isSelected = true
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1 // marquee_forever
            maxLines = 1
            isFocusable = true
            isFocusableInTouchMode = true
            setHorizontallyScrolling(true)
            requestFocus()
        }
        with(binding.tvPlayerDesc) {
            text = ncs?.description
            isSelected = true
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1 // marquee_forever
            maxLines = 1
            isFocusable = true
            isFocusableInTouchMode = true
            setHorizontallyScrolling(true)
            requestFocus()
        }
        Glide.with(this)
            .load(ncs?.imageRes)
            .error(R.drawable.itunes)
            .into(binding.imgPlayerArtwork)
        Glide.with(this)
            .load(ncs?.imageRes)
            .circleCrop()
            .error(R.drawable.itunes)
            .into(binding.imgPlayerArtworkCircle)
    }

    private fun updateDuration() {
        val timestamp = playerViewModel.miliToSecond(mediaController!!.duration)
        binding.tvTotalDuration.text = timestamp
    }

    private fun showRepeatMode() {
        if (mediaController != null) {
            when (mediaController!!.repeatMode) {
                Player.REPEAT_MODE_ALL -> binding.btnPlayerRepeat.setImageResource(R.drawable.ic_repeat_all)
                Player.REPEAT_MODE_ONE -> binding.btnPlayerRepeat.setImageResource(R.drawable.ic_repeat_one)
                Player.REPEAT_MODE_OFF -> binding.btnPlayerRepeat.setImageResource(R.drawable.ic_repeat_off)
            }
        }
    }

    private fun showShuffleState(isShuffle: Boolean) {
        if (isShuffle) {
            binding.btnPlayerShuffle.setImageResource(R.drawable.ic_shuffle_on)
        } else {
            binding.btnPlayerShuffle.setImageResource(R.drawable.ic_shuffle_off)
        }
    }

    private fun showFavorite(song: Song?, ncSong: NCSong?,isNcs: Boolean) {
        Log.i("PlayerActivity", "showFavorite: ${song?.isFavorite}")
        Log.i("PlayerActivity", "showFavorite: ${ncSong?.isFavourite}")
        Log.i("PlayerActivity", "showFavorite: $isNcs")
        if(isNcs){
            if (ncSong?.isFavourite == true) {
                binding.icFavourite.setImageResource(R.drawable.ic_favorite_on)
            } else {
                Log.i("PlayerActivity", "else ncs: ${song?.isFavorite}")
                binding.icFavourite.setImageResource(R.drawable.ic_favorite_off)
            }
        }else{
            if (song?.isFavorite == true) {
                binding.icFavourite.setImageResource(R.drawable.ic_favorite_on)
            } else { // null, false
                Log.i("PlayerActivity", "else song: ${song?.isFavorite}")
                binding.icFavourite.setImageResource(R.drawable.ic_favorite_off)
            }
        }
    }

    private fun setupController() {
        registerMediaControllerListener()
        if (mediaController != null) {
            val playlist: Playlist? = nowPlayingViewModel?.currentPlaylist?.value
            if (mediaController!!.mediaItemCount > 0) {
                playerViewModel.setPlayingState(mediaController!!.isPlaying)
            }
        }
    }

    private fun registerMediaControllerListener() {}


    // ViewModel
    private fun setupViewModel() {
        AnimationViewModel.instance.currentAngle.observe(this) { angle ->
            binding.imgPlayerArtworkCircle.rotation = angle
            val currentPlayTime = MusicAppUtils.getCurrentPlayTime(
                rotationAnimator.duration, angle
            )
            rotationAnimator.currentPlayTime = currentPlayTime
        }

        playerViewModel.playingState.observe(this) { isPlaying ->
            if (isPlaying) {
                if (rotationAnimator.isPaused) {
                    rotationAnimator.resume()
                } else {
                    rotationAnimator.start()
                }
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause_circle)
            } else {
                rotationAnimator.pause()
                binding.btnPlayPause.setImageResource(R.drawable.ic_play_circle)
            }

            binding.btnPlayerNext.isEnabled =
                (mediaController!!.hasNextMediaItem() || mediaController!!.repeatMode == Player.REPEAT_MODE_ALL)
            binding.btnPlayerPrevious.isEnabled =
                (mediaController!!.hasPreviousMediaItem() || mediaController!!.repeatMode == Player.REPEAT_MODE_ALL)
        }

        // Set media item for player activity
        nowPlayingViewModel?.currentPlaylist?.observe(this) { currentPlaylist ->
            currentPlaylist?.mediaItems?.let {
                playerViewModel.setMediaItems(it)
            }
        }

        NetworkViewModel.instance.throwable.observe(this) { throwable ->
            val titleId = R.string.title_server_error
            val messageId = R.string.message_server_error
//            showNoInternetDialog(titleId, messageId)
        }
    }

    // ToolBar
    private fun setupToolBar() {
        binding.playerToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}