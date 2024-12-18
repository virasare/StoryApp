package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.remote.local.repository.StoryRepository
import com.dicoding.storyapp.data.remote.local.repository.UserRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository,
    private val storyRepository : StoryRepository
) : ViewModel() {

    suspend fun getStories(): Flow<PagingData<ListStoryItem>> {
        return storyRepository.getStory().cachedIn(viewModelScope)
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}