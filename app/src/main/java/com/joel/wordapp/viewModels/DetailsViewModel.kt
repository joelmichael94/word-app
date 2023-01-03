package com.joel.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joel.wordapp.models.Word
import com.joel.wordapp.repository.WordRepository
import com.joel.wordapp.ui.DetailsFragmentArgs

class DetailsViewModel(private val repo: WordRepository) : ViewModel() {

    val word: MutableLiveData<Word> = MutableLiveData()

    fun getWordById(id: Long) {
        val res = repo.getWordById(id)
        res?.let {
            word.value = it
        }
    }

    fun deleteWord(id: Long) {
        repo.deleteWord(id)
    }

    fun changeStatus(id: Long) {
        repo.changeStatus(id)
    }

    class Provider(val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailsViewModel(repo) as T
        }
    }
}