package com.joel.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// class with functions to be used by other classes / Fragments (Various Fragments)
class MainViewModel : ViewModel() {
    val refreshWords: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshCompletedWords: MutableLiveData<Boolean> = MutableLiveData(false)

    // refreshes data displayed on newWords Fragment
    fun shouldRefreshWords(refresh: Boolean) {
        refreshWords.value = refresh
    }

    // refreshes data displayed on CompletedWords Fragment
    fun shouldRefreshCompletedWords(refresh: Boolean) {
        refreshCompletedWords.value = refresh
    }
}