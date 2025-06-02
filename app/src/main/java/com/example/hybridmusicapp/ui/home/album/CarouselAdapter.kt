package com.example.hybridmusicapp.ui.home.album

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.databinding.ItemCarouselBinding
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import com.google.android.material.animation.AnimationUtils

class CarouselAdapter(
    private val listener: OnAlbumClickListener

) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    private val _albums = mutableListOf<Album>()

    fun updateAlbums(albums: List<Album>){
        _albums.clear()
        _albums.addAll(albums)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,listener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(_albums[position])
    }

    override fun getItemCount(): Int {
        return _albums.size
    }

    class ViewHolder(
        private val binding: ItemCarouselBinding,
        private val listener: OnAlbumClickListener

    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("RestrictedApi")
        fun bind(album: Album) {
            Glide.with(binding.carouselImageView)
                .load(album.artwork)
                .error(R.drawable.mortals)
                .into(binding.carouselImageView)
            binding.carouselTitle.text = album.name

            binding.root.setOnMaskChangedListener { maskRect ->
                binding.carouselTitle.translationX = maskRect.left
                binding.carouselTitle.alpha = AnimationUtils.lerp(1F, 0F, 0F, 80F, maskRect.left)
            }

            binding.carouselImageView.setOnClickListener {
                val isGranted = PermissionViewModel.instance.isPermissionGranted.value
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.instance.setPermissionAsked(true) // Ask permission
                }
                listener.onClick(album)
                if (MusicAppUtils.sConfigChanged) {
                    MusicAppUtils.sConfigChanged = false
                }
            }
        }
    }

    fun interface OnAlbumClickListener {
        fun onClick(album: Album)
    }
}