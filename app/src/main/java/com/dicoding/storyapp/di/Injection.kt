package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.data.remote.local.StoryRepository
import com.dicoding.storyapp.data.remote.local.UserRepository
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideRepositoryStory(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }

//    fun provideRepositoryStoryID(context: Context): StoryIDRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
//        return StoryIDRepository.getInstance(apiService, pref)
//    }
}