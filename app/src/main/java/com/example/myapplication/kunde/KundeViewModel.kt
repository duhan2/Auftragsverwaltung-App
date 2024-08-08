package com.example.myapplication.kunde

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KundeViewModel(private val repository: KundeRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    fun getallKunden() = repository.getAllKunden().asLiveData(viewModelScope.coroutineContext)

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(kunde: Kunde) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(kunde)
    }

    fun delete(kunde: Kunde) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(kunde)
    }

    fun update(kunde: Kunde) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(kunde)
    }

}

class KundeViewModelFactory(private val repository: KundeRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KundeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KundeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}