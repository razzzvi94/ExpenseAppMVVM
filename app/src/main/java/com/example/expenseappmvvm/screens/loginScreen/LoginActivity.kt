package com.example.expenseappmvvm.screens.loginScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityLoginBinding
import com.example.expenseappmvvm.screens.mainScreen.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            viewModel = this@LoginActivity.loginViewModel
            lifecycleOwner = this@LoginActivity
        }

        loginViewModel.apply {
            nameTextChanged(edit_text_name_register)
            emailTextChanged(edit_text_email_login)
            passwordTextChanged(edit_text_password_login)
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        loginViewModel.goToHomeScreen.observe(this, Observer {
            when (it) {
                true -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

    companion object {
        fun startLogin(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}