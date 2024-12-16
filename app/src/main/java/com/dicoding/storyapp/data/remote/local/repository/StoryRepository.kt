package com.dicoding.storyapp.data.remote.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.data.StoryPagingSource
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.local.StoryDatabase
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
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
