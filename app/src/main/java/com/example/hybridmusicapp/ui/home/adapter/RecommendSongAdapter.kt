package com.example.hybridmusicapp.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.ItemRecommendedSongBinding

class RecommendedSongAdapter(
    private val listener: OnRecommendItemClickListener
) : RecyclerView.Adapter<RecommendedSongAdapter.ViewHolder>() {

    private val recommendedSongs = mutableListOf<RecommendedSong>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecommendedSongs(newRecommendedSongs: MutableList<RecommendedSong>) {
        recommendedSongs.clear()
        recommendedSongs.addAll(newRecommendedSongs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemRecommendedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,listener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(recommendedSongs[position],position)
    }

    override fun getItemCount(): Int {
        return recommendedSongs.size
    }

    class ViewHolder(
        private val binding: ItemRecommendedSongBinding,
        private val listener: OnRecommendItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendedSong: RecommendedSong,index: Int) {
            binding.titleRecommendSong.text = recommendedSong.title
            binding.artistRecommendSong.text = recommendedSong.artist

            if (recommendedSong.remoteImageRes.isEmpty()) {
                Glide.with(binding.root)
                    .load(recommendedSong.imageRes)
                    .error(R.drawable.cradles)
                    .into(binding.imgRecommedSong)
            } else {
                Glide.with(binding.root)
                    .load(recommendedSong.remoteImageRes)
                    .error(R.drawable.cradles)
                    .into(binding.imgRecommedSong)
            }

            binding.root.setOnClickListener {
                listener.onItemClick(recommendedSong,index)
            }
        }
    }

    interface OnRecommendItemClickListener{
        fun onItemClick(recommendedSong: RecommendedSong,index: Int)
    }
}