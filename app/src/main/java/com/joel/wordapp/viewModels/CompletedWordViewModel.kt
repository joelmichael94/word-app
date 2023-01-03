package com.joel.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joel.wordapp.models.Word
import com.joel.wordapp.repository.WordRepository

class CompletedWordViewModel(private val repo: WordRepository) : ViewModel() {
    val words: MutableLiveData<List<Word>> = MutableLiveData()

    init {
        getWords("")
    }

    fun getWords(str: String) {
        val res = repo.getWords(str, true)
        words.value = res.filter { it.status }
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CompletedWordViewModel(repo) as T
        }
    }
}