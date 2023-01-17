package com.joel.wordapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joel.wordapp.data.models.Word

// Defines the Entity (Word) and version(must be >= 1 and must increase everytime there is an update to the Entity), to be used by the RoomDatabase(RD)
@Database(entities = [Word::class], version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordDao: WordDao

    companion object {
        const val DATABASE_NAME = "word_database"
    }
}