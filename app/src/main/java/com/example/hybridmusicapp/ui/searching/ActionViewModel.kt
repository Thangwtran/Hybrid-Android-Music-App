package com.example.hybridmusicapp.ui.searching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActionViewModel: ViewModel() {
    private val _clearEvent = MutableLiveData<ClearType>()
    private val _searchKey = MutableLiveData<String>()

    val clearEvent : LiveData<ClearType>
        get() = _clearEvent

    val searchKey : LiveData<String>
        get() = _searchKey

    fun setClearEvent(type: ClearType){}

    fun setSearchKey(key: String){}

}