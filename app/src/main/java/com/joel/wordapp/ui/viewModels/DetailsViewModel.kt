package com.joel.wordapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.data.repository.WordRepository
import com.joel.wordapp.ui.DetailsFragmentArgs
import kotlinx.coroutines.launch

// class with functions to be used by other classes / Fragments (Details Fragment)
class DetailsViewModel(private val repo: WordRepository) : ViewModel() {
    val word: MutableLiveData<Word> = MutableLiveData()

    // fetch single data by id
    fun getWordById(id: Long) {
        viewModelScope.launch {
            val res = repo.getWordById(id)
            res?.let {
                word.value = it
            }
        }
    }

    // delete data by id
    fun deleteWord(id: Long) {
        viewModelScope.launch {
            repo.deleteWord(id)
        }
    }

    // change status of single data
    fun changeStatus(id: Long) {
        viewModelScope.launch {
            repo.changeStatus(id)
        }
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailsViewModel(repo) as T
        }
    }
}