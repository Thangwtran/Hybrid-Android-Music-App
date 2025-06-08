package com.example.hybridmusicapp.ui.library.recent

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.databinding.ItemRecentNcsBinding
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel

open class RecentNcsAdapter(
    private val listener: RecentNcsAdapter.OnItemClickListener,
    private val menuListener: RecentNcsAdapter.OnMenuItemClick
): RecyclerView.Adapter<RecentNcsAdapter.ViewHolder>() {

    private val songs: MutableList<NCSong> = mutableListOf()
    private var currentPlayingIndex = -1


    @SuppressLint("NotifyDataSetChanged")
    fun updateSongs(songList: List<NCSong>?) {
        if (songList != null) {
            songs.clear()
            songs.addAll(songList)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCurrentPlayingIndex(index: Int) {
        currentPlayingIndex = index
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecentNcsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position], position, currentPlayingIndex!!)
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(
        private val binding: ItemRecentNcsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: NCSong, index: Int, currentPlayingIndex: Int) {
            binding.itemAbTextSong.text = song.ncsName
            binding.itemAbTextArtist.text = song.artist
            Log.i("SongListAdapter", "current: $currentPlayingIndex")

            Glide.with(binding.itemAbImageSong)
                .load(song.imageRes)
                .error(R.drawable.ic_no_song)
                .into(binding.itemAbImageSong)

            if (index == currentPlayingIndex) {
                binding.audioVisual.visibility = View.VISIBLE
                binding.imgPause.visibility = View.INVISIBLE
                binding.itemAbTextSong.setTextColor(binding.root.context.getColor(R.color.blue_violet))
            } else {
                binding.audioVisual.visibility = View.INVISIBLE
                binding.imgPause.visibility = View.VISIBLE
                binding.itemAbTextSong.setTextColor(binding.root.context.getColor(R.color.white))
            }

            binding.root.setOnClickListener {
                val isGranted = PermissionViewModel.instance.isPermissionGranted.value
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.instance.setPermissionAsked(true)
                }
                listener.onItemClick(song, index)
            }

            binding.itemSongOption.setOnClickListener {
                menuListener.onMenuItemClick(song)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(song: NCSong, index: Int)
    }

    interface OnMenuItemClick {
        fun onMenuItemClick(ncs: NCSong)
    }
}