package com.dicoding.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.dicoding.storyapp.helper.ViewModelFactory

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
        playAnimation()
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
                    setMessage("Akun berhasil dibuat!")
                    setPositiveButton("Lanjutkan untuk Login") { _, _ ->
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

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(register)
        }

        AnimatorSet().apply {
            playSequentially(title, name, nameEditTextLayout, email, emailEditTextLayout, password, passwordEditTextLayout, together)
            start()
        }
    }
}
