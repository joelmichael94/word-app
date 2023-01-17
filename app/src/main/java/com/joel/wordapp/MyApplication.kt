package com.joel.wordapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.joel.wordapp.data.WordDatabase
import com.joel.wordapp.repository.WordRepository
import com.joel.wordapp.utils.StorageService

// Main Application running for the App
class MyApplication : Application() {
    //    val wordRepo = WordRepository.getInstance()
    lateinit var storageService: StorageService
    lateinit var wordRepo: WordRepository

    override fun onCreate() {
        super.onCreate()

        // Required functions for StorageService which saves certain preferences (Sort settings in this app is saved upon Sort confirmation, Sort settings will remain the same upon closing and reopening the app)
        val name: String = this.packageName ?: throw NullPointerException("No package name found")
        storageService = StorageService.getInstance(
            this.getSharedPreferences(name, Context.MODE_PRIVATE),
            Gson()
        )

        // Required functions for RoomDatabase service, which is a local storage database (Add, Update and Deleted Words are saved in the database, Words in the app are saved and will still be available within the app even after closing and reopening the app)
        val wordDatabase =
            Room.databaseBuilder(this, WordDatabase::class.java, WordDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        // Connecting the app wordRepository to the RoomDatabase
        wordRepo = WordRepository(wordDatabase.wordDao)
    }
}
// MVVM, Clean Architecture