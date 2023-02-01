package com.joel.wordapp.data.models

data class User(
    val userId: Int? = null,
    val name: String,
    val email: String,
    val pass: String
)