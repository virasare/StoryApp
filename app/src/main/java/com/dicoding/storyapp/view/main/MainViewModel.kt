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
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val storyRepository : StoryRepository) : ViewModel() {

//    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
//    val stories: LiveData<List<ListStoryItem?>?> = _stories

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun getToken(): String {
        return repository.getSession().map { it.token }.first()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    suspend fun fetchStory(token: String) {
//        _isLoading.value = true
        try {
            _isLoading.value = true
//            val response = ApiConfig.getApiService(token).getStories()
//            _stories.value = response.listStory?.filterNotNull()
        } catch (e: Exception) {
            _errorMessage.value = e.localizedMessage
        } finally {
            _isLoading.value = false
        }
    }
}