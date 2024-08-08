package com.example.myapplication.kategorie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KategorieViewModel(private val repository: KategorieRepository) : ViewModel() {

    fun getallKategorien() =
        repository.getAllKategorien().asLiveData(viewModelScope.coroutineContext)

    fun insert(kategorie: Kategorie) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(kategorie)
    }

    fun delete(kategorie: Kategorie) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(kategorie)
    }

    fun update(kategorie: Kategorie) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(kategorie)
    }

}


class KategorieViewModelFactory(private val repository: KategorieRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(KategorieViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return KategorieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}