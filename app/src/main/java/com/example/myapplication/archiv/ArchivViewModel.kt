package com.example.myapplication.archiv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArchivViewModel(private val repository: ArchivRepository) : ViewModel() {
    fun getallArchiv() = repository.getAllArchiv().asLiveData(viewModelScope.coroutineContext)

    fun insert(archiv: Archiv) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(archiv)
    }

    fun delete(archiv: Archiv) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(archiv)
    }

    fun update(archiv: Archiv) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(archiv)
    }
}

class ArchivViewModelFactory(private val repository: ArchivRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArchivViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}