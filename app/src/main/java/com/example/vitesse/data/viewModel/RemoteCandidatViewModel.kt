package com.example.vitesse.data.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.data.repository.RemoteCandidatRepository
import com.example.vitesse.model.Candidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteCandidatViewModel @Inject constructor(
    private val remoteCandidatRepository: RemoteCandidatRepository
) : ViewModel() {

    private val _remoteCandidats = MutableLiveData<List<Candidate>>()
    val remoteCandidats: LiveData<List<Candidate>> get() = _remoteCandidats

    fun fetchCandidats() {
        viewModelScope.launch {

                val candidats = remoteCandidatRepository.fetchCandidats()
                _remoteCandidats.value = candidats
        }
    }
}