package com.example.expenseappmvvm.screens.currencyConverterScreen

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.rest.CurrencyResponse
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.network.RestServiceInterface
import com.example.expenseappmvvm.utils.disposeBy
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CurrencyConverterViewModel(
    private val resourceUtils: ResourceUtils,
    private val converterAPI: RestServiceInterface,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private var currencyObj: CurrencyResponse? = null
    var bnrMessage: MutableLiveData<String> = MutableLiveData()

//    var localCoin = MutableLiveData<Int>().apply { value = 0 }
//    var foreignCoin = MutableLiveData<Int>().apply { value = 0 }

    init {
//        convertCurrencyValueToRon()
//        convertCurrencyValueToRon()
    }

//    private fun convertCurrencyRonToValue() {
//        getCurrencyRonToValue()
//            .debounce(Constants.AWAIT_INPUT, TimeUnit.MILLISECONDS)
//            .observeOn(rxSchedulers.androidUI())
//            .subscribe {
//                if (isDigitsOnly(it) && !view.isForeignCaseSelected() && it.isNotEmpty())
//                    view.calculateRonToValue(it.toString().toDouble())
//            }.disposeBy(compositeDisposable)
//    }
//
//    private fun convertCurrencyValueToRon() {
//        getCurrencyValueToRon()
//            .debounce(Constants.AWAIT_INPUT, TimeUnit.MILLISECONDS)
//            .observeOn(rxSchedulers.androidUI())
//            .subscribe {
//                if (isDigitsOnly(it) && !view.isRonCaseSelected() && it.isNotEmpty())
//                    view.calculateValueToRon(it.toString().toDouble())
//            }.disposeBy(compositeDisposable)
//    }

    fun getCurrency() {
        return converterAPI.allCurrency
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnError {
                Timber.e(it.localizedMessage)
            }
            .doOnNext { currencyResponse ->
                currencyObj = currencyResponse
                //view.initSpinners(currencyObj?.rates)
                bnrMessage.value = resourceUtils.getStringResourceAppend(
                    R.string.dodgy_api_date,
                    currencyResponse.date
                )
            }
            .subscribe({
                Timber.d(resourceUtils.getStringResource(R.string.response_received))
            }, {
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.retrieve_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }).disposeBy(compositeDisposable)
    }
}