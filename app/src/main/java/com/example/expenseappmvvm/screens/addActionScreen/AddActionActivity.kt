package com.example.expenseappmvvm.screens.addActionScreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screens.loginScreen.LoginActivity

class AddActionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)
    }

    companion object {
        fun startAddAction(activity: Activity) {
            val intent = Intent(activity, AddActionActivity::class.java)
            activity.startActivity(intent)
        }
    }
}