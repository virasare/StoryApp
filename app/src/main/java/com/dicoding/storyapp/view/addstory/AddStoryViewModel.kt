package com.dicoding.storyapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.remote.local.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    suspend fun getToken(): String {
        return repository.getSession().map { it.token }.first()
    }
}