package com.example.expenseappmvvm.screens.loginScreen

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.utils.Validations
import com.example.expenseappmvvm.utils.enums.FormErrorsEnum
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils


class LoginViewModel(private val resourceUtils: ResourceUtils) : ViewModel() {
    val showHide = MutableLiveData<Boolean>().apply { value = false }
    val formErrors = ObservableArrayList<FormErrorsEnum>()
    val passwordErr = MutableLiveData<String>().apply { value = "" }

    var username: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

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
        if (!validatePassword()) {
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
        if (!validatePassword()) {
            formErrors.add(FormErrorsEnum.INVALID_PASSWORD)
        }
    }

    private fun validatePassword(): Boolean {
        if (Validations.passwordEmpty(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_empty)
            return false
        } else if (!Validations.passwordContainsDigits(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_digits)
            return false
        } else if (!Validations.passwordContainsLowercase(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_lowercase)
            return false
        } else if (!Validations.passwordContainsUppercase(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_uppercase)
            return false
        } else if (!Validations.passwordContainsSpecialChar(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_specialChar)
            return false
        } else if (!Validations.passwordExcludesSpace(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_whiteSpace)
            return false
        } else if (!Validations.passwordLength(password.value.toString())) {
            passwordErr.value = resourceUtils.getStringResource(R.string.password_err_length)
            return false
        }else{
            passwordErr.value = ""
            return true
        }
    }
}