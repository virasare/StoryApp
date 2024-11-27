package com.dicoding.storyapp.data.remote.local

import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.retrofit.ApiService

class DetailStoryRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference) {

    suspend fun fetchDetailStory(id: String) = apiService.getStoryDetail(id)

    companion object{
        @Volatile
        private var instance: DetailStoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): DetailStoryRepository =
            instance?: synchronized(this){
                instance ?: DetailStoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}


