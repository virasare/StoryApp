package com.dicoding.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.remote.local.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<UserModel?>()
    val loginResult: LiveData<UserModel?> = _loginResult

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private var extraToken: String? = null

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = userRepository.login(email, password)
                if (result.error == false) {
                    _isLogin.value = true
                    val loginResult = result.loginResult
                    if (loginResult != null) {
                        val user = loginResult.token?.let { UserModel(email, it, true) }
                        _loginResult.postValue(user)
                        extraToken = user?.token
                        if (user != null) {
                            saveSession(user)
                        }
                    }
                } else {
                    _isLogin.value = false
                }
            } catch (e: Exception) {
                _isLogin.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}