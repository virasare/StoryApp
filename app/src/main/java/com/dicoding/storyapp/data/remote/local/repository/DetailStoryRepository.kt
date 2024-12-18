package com.dicoding.storyapp.data.remote.local.repository

import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailStoryRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun fetchDetailStory(id: String): Flow<Story?> = flow {
        val token = userPreference.getToken()
        val apiService = ApiConfig.getApiService(token)

        try {
            val response = apiService.getStoryDetail(id)
            emit(response.story)
        } catch (e: Exception) {
            emit(null)
        }
    }


    companion object{
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): DetailStoryRepository = DetailStoryRepository(apiService, userPreference)
    }
}


