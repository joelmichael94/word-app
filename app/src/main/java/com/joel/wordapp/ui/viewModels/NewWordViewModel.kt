package com.joel.wordapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joel.wordapp.data.models.SortBy
import com.joel.wordapp.data.models.SortKey
import com.joel.wordapp.data.models.SortOrder
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.data.repository.WordRepository
import com.joel.wordapp.data.service.StorageService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

// class with functions to be used by other classes / Fragments (New Word Fragment)
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

    // save value of sortBy when changed
    fun onChangeSortBy(value: String) {
        sortBy.value = value
        storageService.setString(SortKey.SORT_BY.name, value)
    }

    // save value of sortOrder when changed
    fun onChangeSortOrder(value: String) {
        sortOrder.value = value
        storageService.setString(SortKey.SORT_ORDER.name, value)
    }

    // refresh the layout/fragment/view when view swiped down
    fun onRefresh() {
        viewModelScope.launch {
            delay(3000)
            getWords("")
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }

    // fetch all the data / words
    fun getWords(str: String) {
        viewModelScope.launch {
            val res = repo.getWords(str, false)
            words.value = res.filter { !it.status }
        }
    }

    // sort all the data in the new words fragment
    fun sortNewWords(str: String, order: String, type: String) {
        viewModelScope.launch {
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
    }

    class Provider(val repo: WordRepository, val storageService: StorageService) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NewWordViewModel(repo, storageService) as T
        }
    }
}