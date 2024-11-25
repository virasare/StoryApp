package com.dicoding.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.remote.local.DetailStoryRepository
import com.dicoding.storyapp.data.remote.local.StoryRepository
import com.dicoding.storyapp.data.remote.local.UserRepository
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.view.detailstory.DetailViewModel
import com.dicoding.storyapp.view.login.LoginViewModel
import com.dicoding.storyapp.view.main.MainViewModel
import com.dicoding.storyapp.view.signup.SignupViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val provideRepositoryStory: StoryRepository,
    private val provideRepositoryDetail: DetailStoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, provideRepositoryStory) as T
            }
//            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
//                AddStoryViewModel(repository, provideRepositoryStory) as T
//            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(provideRepositoryDetail) as T
            }
//            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
//                WelcomeViewModel(repository) as T
//            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context), Injection.provideRepositoryStory(context), Injection.provideRepositoryDetail(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}