package com.example.hybridmusicapp.ui.searching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentHistorySearchedKeyBinding
import com.example.hybridmusicapp.databinding.ItemSearchKeyBinding


class HistorySearchedKeyFragment : Fragment() {
    private lateinit var binding: FragmentHistorySearchedKeyBinding
    private lateinit var adapter: HistorySearchedKeyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistorySearchedKeyBinding.inflate(inflater, container, false)
        return binding.root
    }

}