package com.example.expenseappmvvm.screens.currencyConverterScreen

import android.text.Editable
import android.text.TextUtils.isDigitsOnly
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Currency
import com.example.expenseappmvvm.data.database.repositories.CurrencyRepository
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.TimeUtils
import com.example.expenseappmvvm.utils.disposeBy
import com.example.expenseappmvvm.utils.enums.CurrencyEnum
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CurrencyConverterViewModel(
    private val currencyRepository: CurrencyRepository,
    private val rxSchedulers: AppRxSchedulers,
    private val resourceUtils: ResourceUtils,
    private val sharedPref: Preferences,
    private val userRepository: UserRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val currencyValueList: MutableList<Double> = mutableListOf()
    var bnrMessage: MutableLiveData<String> = MutableLiveData()
    var nativeCurrency = MutableLiveData<String>().apply { value = "0" }
    var foreignCurrency = MutableLiveData<String>().apply { value = "0" }
    var userCurrency = MutableLiveData<String>().apply { value = CurrencyEnum.RON.name }
    var currencyValue: Double = 0.0

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun populateSpinnerList(currency: Currency) {
        currencyValueList.run {
            add(0.0)
            add(currency.RON)
            add(currency.EUR)
            add(currency.USD)
            add(currency.GBP)
            add(currency.CHF)
            add(currency.AUD)
        }
    }

    private fun getLocalCurrency() {
        return currencyRepository.getCurrency(userCurrency.value.toString())
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnSuccess {
                populateSpinnerList(it)
                bnrMessage.value = resourceUtils.getStringResourceAppend(
                    R.string.dodgy_api_date,
                    TimeUtils.currencyDateFormat(it.currencyDate)
                )
            }
            .doOnError {
                Timber.e(it.localizedMessage)
            }
            .subscribe({

            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }

    fun nativeCurrencyTextChanged(nativeCurrencyEditText: EditText) {
        nativeCurrencyEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isDigitsOnly(s) && nativeCurrencyEditText.text.isNotEmpty() && nativeCurrencyEditText.isFocused) {
                    calculateRonToValue()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun foreignCurrencyTextChanged(foreignCurrencyEditText: EditText) {
        foreignCurrencyEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isDigitsOnly(s) && foreignCurrencyEditText.text.isNotEmpty() && foreignCurrencyEditText.isFocused) {
                    calculateValueToRon()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun calculateRonToValue() {
        if (currencyValue != 0.0)
            foreignCurrency.value = String.format(
                Constants.TWO_DECIMAL,
                (Integer.parseInt(nativeCurrency.value.toString()) * currencyValue)
            )
    }


    fun calculateValueToRon() {
        if (currencyValue != 0.0)
            nativeCurrency.value = String.format(
                Constants.TWO_DECIMAL,
                (Integer.parseInt(foreignCurrency.value.toString()) * currencyValue)
            )
    }

    fun getPosition(position: Int) {
        if(currencyValueList.isNotEmpty()){
            currencyValue = currencyValueList[position]
        }
    }

    fun getUserCurrency() {
        var userId: Long = 0
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }

        return userRepository.getUser(userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnSuccess {
                userCurrency.value = it.userCurrency
            }
            .subscribe({
                getLocalCurrency()
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }
}