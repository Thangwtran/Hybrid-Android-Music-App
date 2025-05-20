package com.example.hybridmusicapp.ui.home.ncs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NcsViewModel(
    private val ncsRepository: NCSongRepositoryImp
) : ViewModel() {

    private val _ncsSongs = MutableLiveData<List<NCSong>>()
    val ncsSongs: MutableLiveData<List<NCSong>>
        get() = _ncsSongs

    fun insert( ncSongs: List<NCSong>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ncsRepository.insert(ncSongs)
            if (result.isNotEmpty()){
                _ncsSongs.postValue(ncSongs)
            }else{
                _ncsSongs.postValue(emptyList())
            }
        }
    }

    fun getNCSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ncsRepository.getNCSongs()
            if (result.isNotEmpty()) {
                _ncsSongs.postValue(result)
            } else {
                ncsSongs.postValue(emptyList())
            }
        }
    }


    class Factory(
        private val ncsRepository: NCSongRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NcsViewModel::class.java)) {
                return NcsViewModel(ncsRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}