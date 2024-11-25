package com.dicoding.storyapp.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.isEnabled = false

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
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                binding.loginButton.isEnabled = false
                loginViewModel.loginUser(email, password)
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Email dan password harus diisi.")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            }
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this) { user ->
            binding.progressBar.visibility = View.GONE
            if (user != null) {
                AlertDialog.Builder(this).apply {
                    lifecycleScope.launch {
                        val userPreference = UserPreference.getInstance(dataStore)
                        userPreference.saveToken(user.token)
                    }
                    setTitle("Yeay")
                    setMessage("Kamu Berhasil Login!")
                    setCancelable(false)
                    setPositiveButton("Melanjutkan untuk Mendaftar") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtras(Bundle().apply {
                            putString("extra_token", user.token)
                        })
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
        loginViewModel.isLogin.observe(this) { isLogin ->
            if (!isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle("Login Gagal")
                    setMessage("Akun tidak ditemukan, pastikan Email dan Password Benar !")
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    create()
                    show()
                }
            }
        }
    }

    private fun setupTextWatchers() {
        with(binding) {
            edLoginEmail.addTextChangedListener { enableLoginButton() }
            edLoginPassword.addTextChangedListener { enableLoginButton() }
        }
    }

    private fun enableLoginButton() {
        binding.loginButton.isEnabled = binding.edLoginEmail.text?.isNotEmpty() == true &&
                binding.edLoginPassword.text?.isNotEmpty() == true
    }

}
