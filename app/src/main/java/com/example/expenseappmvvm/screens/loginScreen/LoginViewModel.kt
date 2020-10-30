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
import com.example.expenseappmvvm.utils.*
import com.example.expenseappmvvm.utils.enums.FormErrorsEnum
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
    private val resourceUtils: ResourceUtils,
    private val userRepository: UserRepository,
    private val sharedPref: Preferences,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {
    val showHide = MutableLiveData<Boolean>().apply { value = false }
    val formErrorsList = ObservableArrayList<FormErrorsEnum>()
    val passwordErr = MutableLiveData<String>().apply { value = Constants.EMPTY_STRING }

    var username: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

    var goToHomeScreen = SingleLiveEvent<Any>()

    private var isValid = true

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun addFormError(formError: FormErrorsEnum) {
        formErrorsList.add(formError)
        isValid = false
    }

    private fun validateLogin() {
        formErrorsList.clear()
        isValid = true
        if (!Validations.emailValidation(email.value.toString())) {
            addFormError(FormErrorsEnum.INVALID_EMAIL)
        }
        if (!validatePassword()) {
            addFormError(FormErrorsEnum.INVALID_PASSWORD)
        }
        if (isValid) {
            loginUser()
        }
    }

    private fun validateRegister() {
        formErrorsList.clear()
        isValid = true
        if (!Validations.nameValidation(username.value.toString())) {
            addFormError(FormErrorsEnum.MISSING_NAME)
        }
        if (!Validations.emailValidation(email.value.toString())) {
            addFormError(FormErrorsEnum.INVALID_EMAIL)
        }
        if (!validatePassword()) {
            addFormError(FormErrorsEnum.INVALID_PASSWORD)
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
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.login_success),
                    Toast.LENGTH_SHORT
                ).show()
                sharedPref.write(Constants.USER_ID, it.userId)
                goToHomeScreen.call()
            }, {
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }).disposeBy(compositeDisposable)
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
            passwordErr.value = Constants.EMPTY_STRING
            return true
        }
    }

    fun nameTextChanged(registerTextInputEditText: TextInputEditText) {
        registerTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                formErrorsList.clear()
                isValid = true
                if (!Validations.nameValidation(username.value.toString())) {
                    addFormError(FormErrorsEnum.MISSING_NAME)
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
                formErrorsList.clear()
                isValid = true
                if (!Validations.emailValidation(email.value.toString())) {
                    addFormError(FormErrorsEnum.INVALID_EMAIL)
                }
            }
        })
    }

    fun passwordTextChanged(passwordTextInputText: TextInputEditText) {
        passwordTextInputText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                formErrorsList.clear()
                isValid = true
                if (!validatePassword()) {
                    addFormError(FormErrorsEnum.INVALID_PASSWORD)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
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
}