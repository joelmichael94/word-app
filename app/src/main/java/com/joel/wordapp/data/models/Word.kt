package com.joel.wordapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey
    val id: Long? = null,
    val title: String,
    val meaning: String,
    val synonym: String,
    val details: String,
    var status: Boolean = false
)