package com.dicoding.storyapp.data.remote.local.repository

import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.retrofit.ApiService

class DetailStoryRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun fetchDetailStory(id: String) = apiService.getStoryDetail(id)

    companion object{
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): DetailStoryRepository = DetailStoryRepository(apiService, userPreference)
    }
}


