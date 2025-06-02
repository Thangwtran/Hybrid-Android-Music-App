package com.example.hybridmusicapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnimationViewModel private constructor() : ViewModel() {
    private val _currentAngle = MutableLiveData<Float>()

    val currentAngle: LiveData<Float>
        get() = _currentAngle

    fun setCurrentAngle(angle: Float) {
        _currentAngle.value = angle
    }

    companion object {
        val instance: AnimationViewModel = AnimationViewModel()
    }
}