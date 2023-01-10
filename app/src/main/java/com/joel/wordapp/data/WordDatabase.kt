package com.joel.wordapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joel.wordapp.data.models.Word

@Database(entities = [Word::class], version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordDao: WordDao

    companion object {
        const val DATABASE_NAME = "word_database"
    }
}