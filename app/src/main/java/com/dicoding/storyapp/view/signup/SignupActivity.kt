package com.dicoding.storyapp.view.signup

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signupButton.isEnabled = false

        setupView()
        setupAction()
        observeViewModel()
        setupTextWatchers()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() &&
                binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null) {
                binding.signupButton.isEnabled = false
                signupViewModel.registerUser(name, email, password)
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Please fill in all fields correctly.")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            }
        }
    }

    private fun observeViewModel() {
        signupViewModel.registerResult.observe(this) { user ->
            binding.progressBar.visibility = View.GONE
            if (user != null) {
                AlertDialog.Builder(this).apply {
                    setTitle("Yeay!")
                    setMessage("Account has been created successfully")
                    setPositiveButton("Continue Register") { _, _ ->
                        finishAfterTransition()
                    }
                    create()
                    show()
                }
            }
        }
        signupViewModel.isRegister.observe(this) { isLogin ->
            if (!isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle("Register Failed!")
                    setMessage("Email has already taken")
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    create()
                    show()
                }
            }
        }
    }

    private fun setupTextWatchers() {
        with(binding) {
            edRegisterName.addTextChangedListener { enableSignUpButton() }
            edRegisterEmail.addTextChangedListener { enableSignUpButton() }
            edRegisterPassword.addTextChangedListener { enableSignUpButton() }
        }
    }

    private fun enableSignUpButton() {
        binding.signupButton.isEnabled = binding.edRegisterName.text?.isNotEmpty() == true &&
                binding.edRegisterEmail.text?.isNotEmpty() == true &&
                binding.edRegisterPassword.text?.isNotEmpty() == true &&
                binding.emailEditTextLayout.error == null &&
                binding.passwordEditTextLayout.error == null
    }
}
