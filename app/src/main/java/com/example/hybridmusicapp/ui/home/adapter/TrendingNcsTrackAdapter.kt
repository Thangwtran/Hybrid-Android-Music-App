package com.example.hybridmusicapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.databinding.ItemNcsTrackBinding
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils

class TrendingNcsTrackAdapter(
    private val listener: OnNcsItemClickListener
) : RecyclerView.Adapter<TrendingNcsTrackAdapter.ViewHolder>() {

    private val ncsTracks = mutableListOf<NCSong>()

    fun updateNcsTracks(ncsTracks: List<NCSong>) {
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
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(ncsTracks[position], position)
    }

    override fun getItemCount(): Int {
        return ncsTracks.size
    }

    class ViewHolder(
        private val binding: ItemNcsTrackBinding,
        private val listener: OnNcsItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ncsTrack: NCSong, index: Int) {
            binding.titleNcsTrack.text = ncsTrack.ncsName
            binding.artistNcsTrack.text = ncsTrack.artist
            Glide.with(binding.imgNcsTrack)
                .load(ncsTrack.imageRes)
                .error(R.drawable.royalty)
                .into(binding.imgNcsTrack)

            binding.root.setOnClickListener {
                val isGranted = PermissionViewModel.instance.isPermissionGranted.value
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.instance.setPermissionAsked(true) // Ask permission
                }
                listener.onNcsSongClick(ncsTrack, index)
                if (MusicAppUtils.sConfigChanged) {
                    MusicAppUtils.sConfigChanged = false
                }
            }
        }
    }

    interface OnNcsItemClickListener {
        fun onNcsSongClick(ncs: NCSong, index: Int)
    }
}