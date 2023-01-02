package com.joel.wordapp.models

data class Word(
    val id: Long? = 0,
    val title: String,
    val meaning: String,
    val synonym: String,
    val details: String,
    var status: Boolean = false
)