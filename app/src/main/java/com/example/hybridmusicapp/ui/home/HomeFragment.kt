package com.example.hybridmusicapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.databinding.FragmentHomeBinding
import com.example.hybridmusicapp.ui.home.adapter.CarouselAdapter
import com.example.hybridmusicapp.ui.home.album.AlbumViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.MultiBrowseCarouselStrategy

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var carouselAdapter: CarouselAdapter
    private val albumViewModel by activityViewModels<AlbumViewModel> {
        val application = requireActivity().application as MusicApplication
        AlbumViewModel.Factory(application.albumRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        albumViewModel.getTop10AlbumsFireStore()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCarousel()
    }

    private fun setupCarousel() {
        carouselAdapter = CarouselAdapter(object : CarouselAdapter.OnAlbumClickListener{
            override fun onClick(album: Album) {
                Toast.makeText(requireContext(), album.name, Toast.LENGTH_SHORT).show()
            }
        })

        albumViewModel.albums.observe(viewLifecycleOwner){
            if(it != null){
                Toast.makeText(requireContext(), "Albums loaded successfully", Toast.LENGTH_SHORT).show()
                carouselAdapter.updateAlbums(it)
            }else{
                Toast.makeText(requireContext(), "Error loading albums", Toast.LENGTH_SHORT).show()
            }

        }
        binding.rvCarousel.adapter = carouselAdapter

    }
}