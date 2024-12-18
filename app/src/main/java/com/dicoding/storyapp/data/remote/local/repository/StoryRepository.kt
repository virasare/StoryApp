package com.dicoding.storyapp.data.remote.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.storyapp.data.StoryPagingSource
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.local.StoryDatabase
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {

    suspend fun getStoriesWithLocation(): List<ListStoryItem> {
        val token = userPreference.getToken()

        val apiService = ApiConfig.getApiService(token)

        try {
            val response = apiService.getStoriesWithLocation(location = 1)
            return response.listStory.map { listStoryItem ->
                ListStoryItem(
                    id = listStoryItem.id,
                    name = listStoryItem.name,
                    description = listStoryItem.description.toString(),
                    photoUrl = listStoryItem.photoUrl,
                    lat = listStoryItem.lat,
                    lon = listStoryItem.lon
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }

    suspend fun getStory(): Flow<PagingData<ListStoryItem>> {
        val token = userPreference.getToken()

        if (token.isEmpty()) {
            throw Exception("Token is empty or null")
        }

        val apiServiceWithToken = ApiConfig.getApiService(token)

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiServiceWithToken) }
        ).flow
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, storyDatabase: StoryDatabase): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference, storyDatabase).also { instance = it }
            }
        }
    }
}
