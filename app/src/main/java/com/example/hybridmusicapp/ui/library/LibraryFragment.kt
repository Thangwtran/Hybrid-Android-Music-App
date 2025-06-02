package com.example.hybridmusicapp.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentLibraryBinding


class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater,container,false)
        binding.textView4.isSelected = true
        var parentScale:Float = 1f
        binding.frame.animate().scaleX(parentScale)
        binding.frame.animate().scaleY(parentScale)
        val childScale: Float = 1.0f / parentScale
        binding.textView4?.animate()?.scaleX(childScale)
        binding.textView4?.animate()?.scaleY(childScale)
        return binding.root
    }


}