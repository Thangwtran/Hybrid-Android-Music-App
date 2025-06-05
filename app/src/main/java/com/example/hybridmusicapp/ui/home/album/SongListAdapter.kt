package com.example.hybridmusicapp.ui.home.album

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.ItemSongBinding
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel

class SongListAdapter(
    private val listener: OnItemClickListener,
    private val menuListener: OnMenuItemClick
) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {

    private val songs: MutableList<Song> = mutableListOf()
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    @SuppressLint("NotifyDataSetChanged")
    fun updateSongs(songList: List<Song>?) {
        if (songList != null) {
            songs.clear()
            songs.addAll(songList)
            selectedPosition = RecyclerView.NO_POSITION // reset trạng thái
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(
        private val binding: ItemSongBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, index: Int) {
            binding.itemAbTextSong.text = song.title
            binding.itemAbTextArtist.text = song.artist
            Glide.with(binding.itemAbImageSong)
                .load(song.image)
                .error(R.drawable.ic_no_song)
                .into(binding.itemAbImageSong)

            // Chỉ hiển thị audioVisual nếu đây là item đang được chọn

            if (index == selectedPosition) {
                binding.audioVisual.visibility = View.VISIBLE
                binding.itemAbTextSong.setTextColor(binding.root.context.getColor(R.color.blue_violet))
            } else {
                binding.audioVisual.visibility = View.INVISIBLE
                binding.itemAbTextSong.setTextColor(binding.root.context.getColor(R.color.white))
            }

            binding.root.setOnClickListener {
                val isGranted = PermissionViewModel.instance.isPermissionGranted.value
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.instance.setPermissionAsked(true)
                }

                // Cập nhật item trước và item hiện tại
                val previousPosition = selectedPosition
                selectedPosition = index
                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition)
                }
                notifyItemChanged(selectedPosition)

                listener.onItemClick(song, index)
            }

            binding.itemSongOption.setOnClickListener {
                menuListener.onMenuItemClick(song)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(song: Song, index: Int)
    }

    interface OnMenuItemClick {
        fun onMenuItemClick(song: Song)
    }
}

