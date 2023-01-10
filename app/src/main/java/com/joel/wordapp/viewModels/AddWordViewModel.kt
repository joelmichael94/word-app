package com.joel.wordapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.repository.WordRepository
import kotlinx.coroutines.launch

class AddWordViewModel(private val repo: WordRepository) : ViewModel() {
    fun addWord(word: Word) {
        viewModelScope.launch {
            repo.addWord(word)
        }
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddWordViewModel(repo) as T
        }
    }
}