package com.example.expenseappmvvm.screens.loginScreen

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.utils.FormErrorsEnum
import com.example.expenseappmvvm.utils.Validations


class LoginViewModel() : ViewModel() {
    val showHide = MutableLiveData<Boolean>().apply { value = false }
    var username: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    val formErrors = ObservableArrayList<FormErrorsEnum>()

    fun onCreate() {}

    fun onLoginClick() {
        if (showHide.value == true) {
            validateRegister()
        } else {
            validateLogin()
        }
    }

    fun onRegisterLinkClick() {
        showHide.value = showHide.value != true
    }

    private fun validateLogin() {
        formErrors.clear()
        if (!Validations.emailValidation(email.value.toString())) {
            formErrors.add(FormErrorsEnum.INVALID_EMAIL)
        }
        if (!Validations.passwordValidation(password.value.toString())) {
            formErrors.add(FormErrorsEnum.INVALID_PASSWORD)
        }
    }

    private fun validateRegister() {
        formErrors.clear()
        if (!Validations.nameValidation(username.value.toString())) {
            formErrors.add(FormErrorsEnum.MISSING_NAME)
        }
        if (!Validations.emailValidation(email.value.toString())) {
            formErrors.add(FormErrorsEnum.INVALID_EMAIL)
        }
        if (!Validations.passwordValidation(password.value.toString())) {
            formErrors.add(FormErrorsEnum.INVALID_PASSWORD)
        }
    }
}