package com.joel.wordapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity data class for use by the Repository and RoomDatabase (Word is an entity and in RoomDatabase will appear in a Table format with rows and columns.)
@Entity
data class Word(

    // Primary Key is a unique ID used in the RoomDatabase to differentiate all the Words (assigned to the ID)
    @PrimaryKey
    val id: Long? = null,
    val title: String,
    val meaning: String,
    val synonym: String,
    val details: String,

    // Status is used to filter the NewWords and CompletedWords
    var status: Boolean = false
)