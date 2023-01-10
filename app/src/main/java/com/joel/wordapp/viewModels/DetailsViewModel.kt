package com.joel.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.repository.WordRepository
import com.joel.wordapp.ui.DetailsFragmentArgs
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo: WordRepository) : ViewModel() {

    val word: MutableLiveData<Word> = MutableLiveData()

    fun getWordById(id: Long) {
        viewModelScope.launch {
            val res = repo.getWordById(id)
            res?.let {
                word.value = it
            }
        }
    }

    fun deleteWord(id: Long) {
        viewModelScope.launch {
            repo.deleteWord(id)
        }
    }

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