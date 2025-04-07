package com.example.hybridmusicapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgSplash.setImageResource(arguments?.getInt(IMG_RES)!!)
        binding.titleSplash.text = arguments?.getString(TITLE)
        binding.descSplash.text = arguments?.getString(DESC)
    }

    companion object {
        fun newInstance(title: String, desc: String, imgRes: Int): SplashFragment {
            val fragment = SplashFragment()
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(DESC, desc)
            args.putInt(IMG_RES, imgRes)
            fragment.arguments = args
            return fragment
        }
        private const val TITLE = "title"
        private const val DESC = "desc"
        private const val IMG_RES = "imgRes"
    }
}