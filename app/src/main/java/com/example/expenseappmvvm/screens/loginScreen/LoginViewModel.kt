package com.example.expenseappmvvm.screens.loginScreen

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.User
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.utils.EncryptionUtils
import com.example.expenseappmvvm.utils.Validations
import com.example.expenseappmvvm.utils.disposeBy
import com.example.expenseappmvvm.utils.enums.FormErrorsEnum
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
    private val resourceUtils: ResourceUtils,
    private val userRepository: UserRepository,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {
    val showHide = MutableLiveData<Boolean>().apply { value = false }
    val formErrors = ObservableArrayList<FormErrorsEnum>()
    val passwordErr = MutableLiveData<String>().apply { value = "" }

    var username: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

    var goToHomeScreen = MutableLiveData<Boolean>().apply { value = false }

    var isValid = true

    fun nameTextChanged(registerTextInputEditText: TextInputEditText) {
        registerTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                formErrors.clear()
                isValid = true
                if (!Validations.nameValidation(username.value.toString())) {
                    formErrors.add(FormErrorsEnum.MISSING_NAME)
                    isValid = false
                }
            }
        })
    }

    fun emailTextChanged(emailTextInputEditText: TextInputEditText) {
        emailTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                formErrors.clear()
                isValid = true
                if (!Validations.emailValidation(email.value.toString())) {
                    formErrors.add(FormErrorsEnum.INVALID_EMAIL)
                    isValid = false
                }
            }
        })
    }

    fun passwordTextChanged(passwordTextInputText: TextInputEditText) {
        passwordTextInputText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                formErrors.clear()
                isValid = true
                if (!validatePassword()) {
                    formErrors.add(FormErrorsEnum.INVALID_PASSWORD)
                    isValid = false
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

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
        isValid = true
        if (!Validations.emailValidation(email.value.toString())) {
            formErrors.add(FormErrorsEnum.INVALID_EMAIL)
            isValid = false
        }
        if (!validatePassword()) {
            formErrors.add(FormErrorsEnum.INVALID_PASSWORD)
            isValid = false
        }
        if (isValid) {
            loginUser()
        }
    }

    private fun validateRegister() {
        formErrors.clear()
        isValid = true
        if (!Validations.nameValidation(username.value.toString())) {
            formErrors.add(FormErrorsEnum.MISSING_NAME)
            isValid = false
        }
        if (!Validations.emailValidation(email.value.toString())) {
            formErrors.add(FormErrorsEnum.INVALID_EMAIL)
            isValid = false
        }
        if (!validatePassword()) {
            formErrors.add(FormErrorsEnum.INVALID_PASSWORD)
            isValid = false
        }
        if (isValid) {
            registerUser()
            showHide.value = false
        }
    }

    private fun registerUser() {
        val user = User(
            userEmail = email.value.toString(),
            userName = username.value.toString(),
            userPassword = EncryptionUtils.md5(password.value.toString())
        )

        userRepository.registerUser(user)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.registered_success),
                    Toast.LENGTH_SHORT
                ).show()
            }, {
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.registered_failed),
                    Toast.LENGTH_SHORT
                ).show()
            })
            .disposeBy(compositeDisposable)
    }

    private fun loginUser() {
        val user = User(
            userEmail = email.value.toString(),
            userPassword = EncryptionUtils.md5(password.value.toString())
        )

        userRepository.loginUser(user.userEmail, user.userPassword)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({

                Toast.makeText(resourceUtils.getContext(), resourceUtils.getStringResource(R.string.login_success), Toast.LENGTH_SHORT).show()
                goToHomeScreen.value = true
            }, {
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }).disposeBy(compositeDisposable)
    }

    fun getActivityNavigation(): Boolean?{
        return goToHomeScreen.value
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
        } else {
            passwordErr.value = ""
            return true
        }
    }
}