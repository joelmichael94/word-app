package com.joel.wordapp.models

data class Word(
    val id: Int? = 0,
    val title: String,
    val meaning: String,
    val synonym: String,
    val details: String
)