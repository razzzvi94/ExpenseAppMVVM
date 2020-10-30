package com.example.expenseappmvvm.screens.splashScreen

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.expenseappmvvm.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel.verifyUserLoggedIn(this)
        startAnimation(this)
    }

    private fun startAnimation(activity: SplashActivity){
        val topAnim: Animation = AnimationUtils.loadAnimation(activity, R.anim.top_animation)
        val bottomAnim: Animation = AnimationUtils.loadAnimation(activity, R.anim.bottom_animation)

        splashScreen_logo.animation = topAnim
        splashScreen_textView.animation = bottomAnim

    }
}