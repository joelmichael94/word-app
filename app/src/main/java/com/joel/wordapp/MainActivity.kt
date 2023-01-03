package com.joel.wordapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joel.wordapp.repository.WordRepository

// Joel Michael Seah Quan Shen

class MainActivity : AppCompatActivity() {
    val wordRepo = WordRepository.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// Recycler View
// Floating Button
// Tab Layout
// View Pager
// Dialog box