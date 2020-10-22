package com.example.expenseappmvvm.screens.mainScreen

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Currency
import com.example.expenseappmvvm.data.database.repositories.CurrencyRepository
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.data.rest.CurrencyResponse
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.network.RestServiceInterface
import com.example.expenseappmvvm.utils.*
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


class HomeViewModel(
    private val resourceUtils: ResourceUtils,
    private val sharedPref: Preferences,
    private val userRepository: UserRepository,
    private val currencyRepository: CurrencyRepository,
    private val rxSchedulers: AppRxSchedulers,
    private val converterAPI: RestServiceInterface,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var goToAddActionScreen = SingleLiveEvent<Any>()
    var goToCurrencyConverterScreen = SingleLiveEvent<Any>()
    var goToLogout = SingleLiveEvent<Any>()
    var goToBudgetFragment = SingleLiveEvent<Any>()
    var goToExpenseFragment = SingleLiveEvent<Any>()
    var isExpenseTabSelected = MutableLiveData<Boolean>().apply { value = false }

    val userName = MutableLiveData<String>().apply { value = Constants.EMPTY_STRING }

    private fun getCurrency() {
        return converterAPI.allCurrency
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnError {
                Timber.e(it.localizedMessage)
            }
            .doOnNext { currencyResponse -> saveCurrencyToDB(currencyResponse) }
            .subscribe({
                Timber.d(resourceUtils.getStringResource(R.string.currency_retrieve_success))
            }, {
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.currency_retrieve_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }).disposeBy(compositeDisposable)
    }

    private fun saveCurrencyToDB(currencyObj: CurrencyResponse) {
        val currency = Currency(
            currencyBase = currencyObj.base,
            currencyDate = TimeUtils.currencyDateToGMT(currencyObj.date),
            EUR = currencyObj.rates.EUR,
            USD = currencyObj.rates.USD,
            GBP = currencyObj.rates.GBP,
            CHF = currencyObj.rates.CHF,
            AUD = currencyObj.rates.AUD
        )
        currencyRepository.insertCurrency(currency)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.save_currency_success),
                    Toast.LENGTH_SHORT
                ).show()
            }, {
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.save_currency_failed),
                    Toast.LENGTH_SHORT
                ).show()
            })
            .disposeBy(compositeDisposable)
    }

    fun getCurrencyDate() {
        return currencyRepository.getCurrencyDate()
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnSuccess {
                if(isNetworkConnected(resourceUtils.getContext()) && TimeUtils.lastCurrencyCheck(it)){
                    getCurrency()
                }
            }
            .doOnComplete {
                getCurrency()
            }
            .subscribe({
            },{
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }

    fun onLogoutClick() {
        sharedPref.clear()
        goToLogout.call()
    }

    fun onAddActionClick() {
        goToAddActionScreen.call()
    }

    fun onCurrencyConverterClick() {
        goToCurrencyConverterScreen.call()
    }

    fun budgetTabOnClick() {
        isExpenseTabSelected.value = false
        goToBudgetFragment.call()
    }

    fun expenseTabOnClick() {
        isExpenseTabSelected.value = true
        goToExpenseFragment.call()
    }

    fun getUserName() {
        var userId: Long = 0
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }

        return userRepository.getUserName(userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                userName.value = it
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }
}