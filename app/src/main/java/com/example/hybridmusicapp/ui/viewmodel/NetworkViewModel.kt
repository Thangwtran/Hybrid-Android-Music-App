package com.example.hybridmusicapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NetworkViewModel private constructor(): ViewModel(){

    private val _networkStatus = MutableLiveData<NetworkState>()
    val networkStatus: LiveData<NetworkState> = _networkStatus

    private val _throwable = MutableLiveData<Throwable>()
    val throwable: LiveData<Throwable> = _throwable

    fun setNetworkStatus(networkState: NetworkState){
        _networkStatus.value = networkState
    }

    fun setThrowable(throwable: Throwable){
        _throwable.value = throwable
    }



    class NetworkState(val state: Int, val isWifi: Boolean, val isCellular: Boolean){

        companion object{
            const val ACTIVE: Int = 0
            const val INACTIVE: Int = 1
            const val BLOCK: Int = 2
            const val UNBLOCK: Int = 3
        }
    }

    companion object{
        private var _instance: NetworkViewModel? = null
        val instance: NetworkViewModel
            get() {
                if (_instance == null){
                    _instance = NetworkViewModel()
                }
                return _instance!!
            }
    }
}