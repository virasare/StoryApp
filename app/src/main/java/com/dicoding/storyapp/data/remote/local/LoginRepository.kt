package com.dicoding.storyapp.data.remote.local

import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService

class LoginRepository (private val apiService: ApiService) {
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }
}