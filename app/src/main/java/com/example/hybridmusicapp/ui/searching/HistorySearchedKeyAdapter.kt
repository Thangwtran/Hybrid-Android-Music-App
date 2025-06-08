package com.example.hybridmusicapp.ui.searching

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.databinding.ItemSearchKeyBinding

class HistorySearchedKeyAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HistorySearchedKeyAdapter.ViewHolder>() {

    private val keys: MutableList<HistorySearchedKey> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemSearchKeyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,listener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(keys[position])
    }

    override fun getItemCount(): Int {
        return keys.size
    }

    class ViewHolder(
        private val binding: ItemSearchKeyBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(key: HistorySearchedKey) {

        }
    }

    interface OnItemClickListener {
        fun onItemClick(key: String)
    }
}