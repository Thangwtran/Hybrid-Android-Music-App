package com.example.hybridmusicapp.ui.home.artist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.databinding.ItemArtistBinding

class TopArtistAdapter: RecyclerView.Adapter<TopArtistAdapter.ViewHolder>() {

    private val artists = mutableListOf<Artist>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateArtists(artistList: List<Artist>){
        artists.clear();
        artists.addAll(artistList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArtistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(artists[position])
    }

    override fun getItemCount(): Int {
        return artists.size;
    }

    class ViewHolder(
        private val binding: ItemArtistBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(artist: Artist){
            binding.textArtist.text = artist.name
            Glide.with(binding.imageArtist)
                .load(artist.avatar)
                .error(R.drawable.royalty)
//                .dontAnimate()
                .into(binding.imageArtist)
        }
    }
}