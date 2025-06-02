package com.example.hybridmusicapp.ui.home.album

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.ItemAlbumDetailBinding
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel

class SongListAdapter(
    private val listener: OnItemClickListener,
    private val menuListener: OnMenuItemClick
) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {
    private val songs: MutableList<Song> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateSongs(songList: List<Song>?) {
        if (songList != null) {
            songs.clear()
            songs.addAll(songList)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemAlbumDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener, menuListener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(songs[position], position)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    class ViewHolder(
        private val binding: ItemAlbumDetailBinding,
        private val listener: OnItemClickListener,
        private val menuListener: OnMenuItemClick
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, index: Int) {
            binding.itemAbTextSong.text = song.title
            binding.itemAbTextArtist.text = song.artist
            Glide.with(binding.itemAbImageSong)
                .load(song.image)
                .error(R.drawable.ic_no_song)
                .into(binding.itemAbImageSong)

            binding.root.setOnClickListener {

                val isGranted = PermissionViewModel.instance.isPermissionGranted.value
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.instance.setPermissionAsked(true)
                }

                listener.onItemClick(song, index,binding.itemAbTextSong)
                // TODO: config change
            }

            binding.itemSongOption.setOnClickListener {
                menuListener.onMenuItemClick(song)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(song: Song, index: Int,view: TextView)
    }

    interface OnMenuItemClick {
        fun onMenuItemClick(song: Song)
    }
}