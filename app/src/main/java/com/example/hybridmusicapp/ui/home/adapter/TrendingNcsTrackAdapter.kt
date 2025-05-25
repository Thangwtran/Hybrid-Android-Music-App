package com.example.hybridmusicapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.databinding.ItemNcsTrackBinding

class TrendingNcsTrackAdapter: RecyclerView.Adapter<TrendingNcsTrackAdapter.ViewHolder>() {

    private val ncsTracks = mutableListOf<NCSong>()

    fun updateNcsTracks(ncsTracks: List<NCSong>){
        this.ncsTracks.clear()
        this.ncsTracks.addAll(ncsTracks)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNcsTrackBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(ncsTracks[position])
    }

    override fun getItemCount(): Int {
        return ncsTracks.size
    }

    class ViewHolder(
        private val binding: ItemNcsTrackBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(ncsTrack: NCSong){
            binding.titleNcsTrack.text = ncsTrack.ncsName
            binding.artistNcsTrack.text = ncsTrack.artist
            Glide.with(binding.imgNcsTrack)
                .load(ncsTrack.imageRes)
                .error(R.drawable.royalty)
                .into(binding.imgNcsTrack)
        }
    }
}