package com.joel.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.models.SortBy
import com.joel.wordapp.models.SortKey
import com.joel.wordapp.models.SortOrder
import com.joel.wordapp.models.Word
import com.joel.wordapp.repository.WordRepository
import com.joel.wordapp.utils.StorageService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class NewWordViewModel(val repo: WordRepository, val storageService: StorageService) : ViewModel() {
    val words: MutableLiveData<List<Word>> = MutableLiveData()
    val sortBy: MutableLiveData<String> = MutableLiveData()
    val sortOrder: MutableLiveData<String> = MutableLiveData()

    val swipeRefreshLayoutFinished: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {
        getWords("")
        sortBy.value = storageService.getString(SortKey.SORT_BY.name)
        sortOrder.value = storageService.getString(SortKey.SORT_ORDER.name)
    }

    fun onChangeSortBy(value: String) {
        sortBy.value = value
        storageService.setString(SortKey.SORT_BY.name, value)
    }

    fun onChangeSortOrder(value: String) {
        sortOrder.value = value
        storageService.setString(SortKey.SORT_ORDER.name, value)
    }

    fun onRefresh() {
        viewModelScope.launch {
            delay(3000)
            getWords("")
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }

    fun getWords(str: String) {
        val res = repo.getWords(str, false)
        words.value = res.filter { !it.status }
    }

    fun sortNewWords(str: String, order: String, type: String) {
        var res = repo.getWords(str, false)
        if (order == SortOrder.ASCENDING.name && type == SortBy.TITLE.name) {
            res = res.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
                it.title
            })
        } else if (order == SortOrder.DESCENDING.name && type == SortBy.TITLE.name) {
            res = res.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) {
                it.title
            })
        } else if (order == SortOrder.DESCENDING.name && type == SortBy.DATE.name) {
            res = res.reversed()
        }
        words.value = res.filter { !it.status }
    }

    class Provider(val repo: WordRepository, val storageService: StorageService) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NewWordViewModel(repo, storageService) as T
        }
    }
}