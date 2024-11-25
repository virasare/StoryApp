package com.dicoding.storyapp.data.remote.local

import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference).also { instance = it }
            }
        }
    }
}
