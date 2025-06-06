package com.example.hybridmusicapp.ui.discovery.genre

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.ItemCarouselBinding
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import com.google.android.material.animation.AnimationUtils

class CarouselGenreAdapter(
    private val listener: OnAlbumClickListener
) : RecyclerView.Adapter<CarouselGenreAdapter.ViewHolder>() {

    private val _genre = mutableListOf<Genre>()

    fun updateGenre(genre: List<Genre>) {
        _genre.clear()
        _genre.addAll(genre)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(_genre[position])
    }

    override fun getItemCount(): Int {
        return _genre.size
    }

    class ViewHolder(
        private val binding: ItemCarouselBinding,
        private val listener: OnAlbumClickListener

    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("RestrictedApi")
        fun bind(genre: Genre) {
            Glide.with(binding.carouselImageView)
                .load(genre.artworkResId)
                .error(R.drawable.mortals)
                .into(binding.carouselImageView)
            binding.carouselTitle.text = genre.genre

            binding.root.setOnMaskChangedListener { maskRect ->
                binding.carouselTitle.translationX = maskRect.left
                binding.carouselTitle.alpha = AnimationUtils.lerp(1F, 0F, 0F, 80F, maskRect.left)
            }

            binding.carouselImageView.setOnClickListener {
                val isGranted = PermissionViewModel.instance.isPermissionGranted.value
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.instance.setPermissionAsked(true) // Ask permission
                }
                listener.onClick(genre)
                if (MusicAppUtils.sConfigChanged) {
                    MusicAppUtils.sConfigChanged = false
                }
            }
        }
    }

    interface OnAlbumClickListener {
        fun onClick(genre: Genre)
    }
}