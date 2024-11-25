package com.dicoding.storyapp.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.remote.local.StoryRepository
import com.dicoding.storyapp.data.remote.local.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AddStoryViewModel(private val repository: UserRepository, storyRepository: StoryRepository): ViewModel() {
    suspend fun getToken(): String {
        return repository.getSession().map { it.token }.first()
    }
}