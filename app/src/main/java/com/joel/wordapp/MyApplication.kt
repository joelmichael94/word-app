package com.joel.wordapp

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.joel.wordapp.repository.WordRepository
import com.joel.wordapp.utils.StorageService

class MyApplication : Application() {
    val wordRepo = WordRepository.getInstance()
    lateinit var storageService: StorageService

    override fun onCreate() {
        super.onCreate()
        val name: String = this.packageName ?: throw NullPointerException("No package name found")
        storageService = StorageService.getInstance(
            this.getSharedPreferences(name, Context.MODE_PRIVATE),
            Gson()
        )
    }
}
// MVVM, Clean Architecture