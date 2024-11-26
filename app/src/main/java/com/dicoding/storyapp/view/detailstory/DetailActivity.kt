package com.dicoding.storyapp.view.detailstory

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.ActivityDetailBinding
import com.dicoding.storyapp.helper.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("story_id")
        if (storyId != null) {
            detailViewModel.fetchDetailStory(storyId)
        }

        detailViewModel.storyDetail.observe(this) { story ->
            with(binding) {
                tvItemName.text = story?.name
                tvDescription.text = story?.description
                Glide.with(this@DetailActivity)
                    .load(story?.photoUrl)
                    .into(ivItemPhoto)
            }
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
