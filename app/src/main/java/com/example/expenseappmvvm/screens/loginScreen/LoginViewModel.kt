package com.example.expenseappmvvm.screens.loginScreen

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils

class LoginViewModel(private val resourceUtils: ResourceUtils) : ViewModel() {
    val showHide = MutableLiveData<Boolean>().apply { value = false }
    var username: String = ""
    var email: String = ""
    var password: String = ""

    fun onCreate() {}

    fun onLoginClick() {
        if(showHide.value == true) {
            validateRegister()
        }
        else{
            validateLogin()
        }
    }

    fun onRegisterLinkClick() {
        showHide.value = showHide.value != true
    }

    private fun validateLogin() {
        if (email.isEmpty()) {
            Toast.makeText(resourceUtils.getContext(), "Invalid email", Toast.LENGTH_SHORT).show()
        }

        if (password.isEmpty()) {
            Toast.makeText(resourceUtils.getContext(), "Invalid password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateRegister(){
        if (username.isEmpty()) {
            Toast.makeText(resourceUtils.getContext(), "Invalid username", Toast.LENGTH_SHORT).show()
        }

        if (email.isEmpty()) {
            Toast.makeText(resourceUtils.getContext(), "Invalid email", Toast.LENGTH_SHORT).show()
        }

        if (password.isEmpty()) {
            Toast.makeText(resourceUtils.getContext(), "Invalid password", Toast.LENGTH_SHORT).show()
        }
    }
}