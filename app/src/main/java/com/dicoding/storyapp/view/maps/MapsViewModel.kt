package com.dicoding.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.remote.local.repository.StoryRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<Result<StoryResponse>>()
    val stories: LiveData<Result<StoryResponse>> = _stories

    private val _snackBarMaps = MutableLiveData<String?>()
    val snackBarMaps: LiveData<String?> = _snackBarMaps

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    fun fetchStoriesWithLocation(){
        _isLoading.value = true
        viewModelScope.launch {
            try{
                val response = repository.getStoriesWithLocation()
                _stories.value = Result.success(response)
            }catch (e: Exception){
                _stories.value = Result.failure(e)
            }finally {
                _isLoading.value = false
            }
        }
    }
}