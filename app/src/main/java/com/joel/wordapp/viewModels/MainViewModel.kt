package com.joel.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val refreshWords: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshCompletedWords: MutableLiveData<Boolean> = MutableLiveData(false)

    fun shouldRefreshWords(refresh: Boolean) {}

    fun shouldRefreshCompletedWords(refresh: Boolean) {}
}