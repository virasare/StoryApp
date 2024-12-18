package com.dicoding.storyapp.view.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.remote.local.repository.DetailStoryRepository
import com.dicoding.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailStoryRepository) : ViewModel() {
    private val _storyDetail = MutableLiveData<Story?>()
    val storyDetail: LiveData<Story?> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchDetailStory(storyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.fetchDetailStory(storyId).collect { result ->
                    _storyDetail.value = result
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat detail story: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
