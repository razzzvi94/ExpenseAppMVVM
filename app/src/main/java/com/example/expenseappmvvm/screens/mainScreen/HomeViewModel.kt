package com.example.expenseappmvvm.screens.mainScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Currency
import com.example.expenseappmvvm.data.database.repositories.CurrencyRepository
import com.example.expenseappmvvm.data.database.repositories.TransactionRepository
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.data.rest.CurrencyResponse
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.network.RestServiceInterface
import com.example.expenseappmvvm.utils.*
import com.example.expenseappmvvm.utils.enums.CurrencyEnum
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


class HomeViewModel(
    private val resourceUtils: ResourceUtils,
    private val sharedPref: Preferences,
    private val userRepository: UserRepository,
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
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
    var openChangeCurrencyDialog = SingleLiveEvent<Any>()

    val userName = MutableLiveData<String>().apply { value = Constants.EMPTY_STRING }
    var currencyBase = MutableLiveData<String>().apply { value = CurrencyEnum.RON.name }
    var currencyTarget = MutableLiveData<String>().apply { value = CurrencyEnum.RON.name }

    private fun getCurrency(base: String) {
        return converterAPI.allCurrency(base)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnError {
                Timber.e(it.localizedMessage)
            }
            .doOnNext { currencyResponse -> saveCurrencyToDB(currencyResponse) }
            .subscribe({
                Timber.i(resourceUtils.getStringResource(R.string.currency_retrieve_success))
            }, {
                Timber.e(resourceUtils.getStringResource(R.string.currency_retrieve_failed))
            }).disposeBy(compositeDisposable)
    }

    private fun saveCurrencyToDB(currencyObj: CurrencyResponse) {
        val currency = Currency(
            currencyBase = currencyObj.base,
            currencyDate = TimeUtils.currencyDateToGMT(currencyObj.date),
            RON = currencyObj.rates.RON,
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
                Timber.d(resourceUtils.getStringResource(R.string.save_currency_success))
            }, {
                Timber.e(resourceUtils.getStringResource(R.string.save_currency_failed))
            })
            .disposeBy(compositeDisposable)
    }

    private fun updateUserAmountPreferredCurrency(baseCurrency: String, targetCurrency: String) {
        var userId: Long = 0

        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }
        transactionRepository.getMultiplier(baseCurrency)
            .subscribeOn(rxSchedulers.background())
            .subscribe({
                applyMultiplier(it, targetCurrency, userId)
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }

    private fun applyMultiplier(currency: Currency, targetCurrency: String, userId: Long) {
        var multiplier = 1.0
        when(targetCurrency){
            CurrencyEnum.RON.name -> multiplier = currency.RON
            CurrencyEnum.EUR.name -> multiplier = currency.EUR
            CurrencyEnum.USD.name -> multiplier = currency.USD
            CurrencyEnum.GBP.name -> multiplier = currency.GBP
            CurrencyEnum.CHF.name -> multiplier = currency.CHF
            CurrencyEnum.AUD.name -> multiplier = currency.AUD
        }
        transactionRepository.updateUserAmountCurrency(multiplier, userId)
            .subscribeOn(rxSchedulers.background())
            .subscribe({
                Timber.i("Updated rows: $it")
            },{
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }

    fun getCurrencyDate() {
        return currencyRepository.getCurrencyDate()
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnSuccess {
                if (resourceUtils.isNetworkConnected() && TimeUtils.lastCurrencyCheck(it)) {
                    for (i in CurrencyEnum.values().indices){
                        getCurrency(CurrencyEnum.values()[i].name)
                    }
                }
            }
            .doOnComplete {
                for (i in CurrencyEnum.values().indices){
                    getCurrency(CurrencyEnum.values()[i].name)
                }
            }
            .subscribe({
            }, {
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

    fun getUserNameAndCurrency() {
        var userId: Long = 0
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }

        return userRepository.getUser(userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                userName.value = it.userName
                currencyTarget.value = it.userCurrency
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }

    fun openChangeCurrencyBottomSheet() {
        openChangeCurrencyDialog.call()
    }

    fun getSelectedUserPosition(): Int {
        return when (currencyTarget.value) {
            CurrencyEnum.RON.name -> 0
            CurrencyEnum.EUR.name -> 1
            CurrencyEnum.USD.name -> 2
            CurrencyEnum.GBP.name -> 3
            CurrencyEnum.CHF.name -> 4
            CurrencyEnum.AUD.name -> 5
            else -> 0
        }
    }

    fun saveUserPreferredCurrency() {
        var userId: Long = 0
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }
        userRepository.preferredCurrency(currencyTarget.value.toString(), userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                updateUserAmountPreferredCurrency(
                    currencyBase.value.toString(),
                    currencyTarget.value.toString()
                )
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }
}