package com.joel.wordapp.data.service

import android.app.Application
import android.content.Context
import com.joel.wordapp.MyApplication
import com.joel.wordapp.data.models.User

class AuthService private constructor(private val storageService: StorageService) {
    fun authenticate(user: User) {
        storageService.setUser("user", user)
    }

    fun deauthenticate() {
        storageService.removeUser("user")
    }

    fun isAuthenticated(): Boolean {
        val user = storageService.getUser("user")
        return user != null
    }

    fun getAuthenticUser(): User? {
        return storageService.getUser("user")
    }

    companion object {
        private var authService: AuthService? = null
        fun getInstance(context: Context): AuthService {
            if (authService == null) {
                authService =
                    AuthService((context.applicationContext as MyApplication).storageService)
            }
            return authService!!
        }
    }
}