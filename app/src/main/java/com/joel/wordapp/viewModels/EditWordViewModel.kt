package com.joel.wordapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.repository.WordRepository
import kotlinx.coroutines.launch

class EditWordViewModel(private val repo: WordRepository) : ViewModel() {
    fun updateWord(id: Long, word: Word) {
        viewModelScope.launch {
            repo.updateWord(id, word)
        }
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditWordViewModel(repo) as T
        }
    }
}