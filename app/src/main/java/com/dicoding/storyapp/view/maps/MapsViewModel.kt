package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.remote.local.repository.StoryRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _snackBarMaps = MutableLiveData<String?>()
    val snackBarMaps: LiveData<String?> = _snackBarMaps

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchStoriesWithLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedStories = repository.getStoriesWithLocation()
                _stories.postValue(fetchedStories)
            } catch (e: Exception) {
                Log.e("MapsViewModel", "Failed to fetch stories", e)
                _stories.postValue(emptyList())
                _snackBarMaps.postValue("Failed to load stories. Please try again.") // Tampilkan error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
