package com.joel.wordapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.data.repository.WordRepository
import kotlinx.coroutines.launch

// class with functions to be used by other classes / Fragments (Edit Word Fragment)
class EditWordViewModel(private val repo: WordRepository) : ViewModel() {

    // update/edit single data based on id
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