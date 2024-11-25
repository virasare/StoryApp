package com.dicoding.storyapp.view.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.remote.local.DetailStoryRepository
import com.dicoding.storyapp.data.remote.response.DetailStoryResponse
import com.dicoding.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: DetailStoryRepository): ViewModel() {
    private val _storyDetail = MutableLiveData<Story?>()
    val storyDetail: MutableLiveData<Story?> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchDetailStory(id: String){
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.fetchDetailStory(id)
            response.enqueue(object : Callback<DetailStoryResponse>{
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    _isLoading.value = false
                    if(response.isSuccessful && response.body() != null){
                        _storyDetail.value = response.body()?.story
                    }else{
                        _errorMessage.value = "Error: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Failure: ${t.message}"
                }
            })
        }
    }
}