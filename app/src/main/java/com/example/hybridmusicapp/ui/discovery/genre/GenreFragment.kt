package com.example.hybridmusicapp.ui.discovery.genre

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentGenreBinding

class GenreFragment : Fragment() {
    private lateinit var binding: FragmentGenreBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGenreBinding.inflate(inflater,container,false)
        return binding.root
    }

}