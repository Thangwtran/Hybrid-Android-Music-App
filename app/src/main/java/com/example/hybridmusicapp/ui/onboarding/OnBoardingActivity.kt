package com.example.hybridmusicapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hybridmusicapp.databinding.ActivityOnBoardingBinding
import com.example.hybridmusicapp.HomeActivity
import com.example.hybridmusicapp.ui.home.HomeFragment

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private val listGenre = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setChipSelected()


        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putStringArrayListExtra(
                HomeActivity.EXTRA_GENRE_RECOMMENDED,
                ArrayList(listGenre)
            )
            startActivity(intent)
        }
    }

    private fun setChipSelected() {
        // pop
        binding.chipPop.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // dubstep
        binding.chipDubstep.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }

        }
        // rap
        binding.chipRap.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // chill
        binding.chipChill.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // epic
        binding.chipEpic.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // cinematic
        binding.chipCinematic.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // bass
        binding.chipBass.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // edm
        binding.chipEdm.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // vocal
        binding.chipVocal.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
        // edm rap
        binding.chipEdmRap.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }

        // other
        binding.chipOther.setOnCheckedChangeListener { chip, isCheck ->
            if (isCheck) {
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.add(genre)
            }else{
                val genre = chip.text.toString().split(" ").drop(1).joinToString(" ")
                listGenre.remove(genre)
            }
        }
    }
}