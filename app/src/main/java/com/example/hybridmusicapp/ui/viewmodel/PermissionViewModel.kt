package com.example.hybridmusicapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {
    private val _permissionAsked = MutableLiveData<Boolean>()
    val permissionAsked: LiveData<Boolean> = _permissionAsked

    private val _permissionGranted = MutableLiveData<Boolean>()
    val isPermissionGranted: LiveData<Boolean> = _permissionGranted

    fun setPermissionAsked(permissionAsked: Boolean) {
        _permissionAsked.value = permissionAsked
    }

    fun setPermissionGranted(permissionGranted: Boolean) {
        _permissionGranted.value = permissionGranted
    }

    companion object {
        var isRegistered: Boolean = false
        private var _instance: PermissionViewModel? = null
        val instance: PermissionViewModel
            get() {
                if (_instance == null) {
                    _instance = PermissionViewModel()
                }
                return _instance!!
            }
    }
}