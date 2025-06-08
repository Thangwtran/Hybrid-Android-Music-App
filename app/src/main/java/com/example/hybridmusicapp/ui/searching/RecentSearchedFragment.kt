package com.example.hybridmusicapp.ui.searching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentRecentSearchedBinding
import com.example.hybridmusicapp.ui.home.album.SongListAdapter

class RecentSearchedFragment : Fragment() {
    private lateinit var binding: FragmentRecentSearchedBinding
    private lateinit var adapter: RecentSearchSongsAdapter
    private val actionViewModel by viewModels<ActionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentSearchedBinding.inflate(inflater, container, false)
        return binding.root
    }

    internal class RecentSearchSongsAdapter(
        listener: SongListAdapter.OnItemClickListener,
        menuItemClick: SongListAdapter.OnMenuItemClick
    ) : SongListAdapter(listener, menuItemClick)
}