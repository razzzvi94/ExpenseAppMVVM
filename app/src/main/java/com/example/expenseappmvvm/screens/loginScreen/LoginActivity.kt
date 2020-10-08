package com.example.expenseappmvvm.screens.loginScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityLoginBinding
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            viewModel = this@LoginActivity.loginViewModel
            lifecycleOwner = this@LoginActivity
        }
        loginViewModel.onCreate()
    }

    companion object {
        fun startLogin(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}