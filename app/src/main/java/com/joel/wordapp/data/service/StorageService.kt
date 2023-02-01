package com.joel.wordapp.data.service

import android.content.SharedPreferences
import com.google.gson.Gson
import com.joel.wordapp.data.models.User

// class that stores certain settings or preferences that will remain even after app is closed and reopened
class StorageService private constructor(
    private val sharedPref: SharedPreferences,
    private val gson: Gson
) {

    // not in use
    fun setBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    // not in use
    fun getBoolean(key: String): Boolean = sharedPref.getBoolean(key, false)

    // not in use
    fun removeBoolean(key: String) {
        val editor = sharedPref.edit()
        editor.remove(key)
        editor.apply()
    }

    // sets a string to save the sort preferences values
    fun setString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // fetches the sort preferences values that was previously saved
    fun getString(key: String): String = sharedPref.getString(key, null) ?: ""

    // not in use
    fun removeString(key: String) {
        val editor = sharedPref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun setUser(key: String, user: User) {
        val editor = sharedPref.edit()
        val jsonString = gson.toJson(user)
        editor.putString(key, jsonString)
        editor.apply()
    }

    fun getUser(key: String): User? {
        val jsonString = sharedPref.getString(key, "null")
        return gson.fromJson(jsonString, User::class.java)
    }

    fun removeUser(key: String) {
        val editor = sharedPref.edit()
        editor.remove(key)
        editor.apply()
    }

    // to be called by another class to use its above functions
    companion object {
        private var storageService: StorageService? = null
        fun getInstance(sharedPref: SharedPreferences, gson: Gson): StorageService {
            if (storageService == null) {
                storageService = StorageService(sharedPref, gson)
            }

            return storageService!!
        }
    }
}