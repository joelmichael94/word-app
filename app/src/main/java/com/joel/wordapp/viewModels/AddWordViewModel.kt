package com.joel.wordapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joel.wordapp.models.Word
import com.joel.wordapp.repository.WordRepository

class AddWordViewModel(private val repo: WordRepository) : ViewModel() {
    fun addWord(word: Word) {
        repo.addWord(word)
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddWordViewModel(repo) as T
        }
    }
}