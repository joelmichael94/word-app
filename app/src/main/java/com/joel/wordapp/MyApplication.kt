package com.joel.wordapp

import android.app.Application
import com.joel.wordapp.repository.WordRepository

class MyApplication : Application() {
    val wordRepo = WordRepository.getInstance()
}
// MVVM, Clean Architecture