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

    fun sortCompletedWords(str: String, order: String, type: String) {
        var res = repo.getWords(str, true)
        if (order == "ascending" && type == "title") {
            res = res.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
                it.title
            })
        } else if (order == "descending" && type == "title") {
            res = res.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) {
                it.title
            })
        } else if (order == "descending" && type == "date") {
            res = res.reversed()
        }
        words.value = res.filter { it.status }
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CompletedWordViewModel(repo) as T
        }
    }
}