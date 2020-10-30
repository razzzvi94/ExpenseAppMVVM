package com.example.expenseappmvvm.screens.mainScreen.fragments.budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.repositories.TransactionRepository
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import java.text.DecimalFormat
import java.util.*

class BudgetFragmentViewModel(
    private val transactionRepository: TransactionRepository,
    private val sharedPref: Preferences,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private var userId: Long = 0L
    private var startDay: Long = 0L
    private var endDay: Long = 0L
    private var c: Calendar = Calendar.getInstance(TimeZone.getTimeZone(Constants.TIME_ZONE_UTC))
    private var startMonth: Long = 0L
    private var startWeek: Long = 0L
    private var c1: Calendar = Calendar.getInstance(TimeZone.getTimeZone(Constants.TIME_ZONE_UTC))
    private val twoDigitsFormatter: DecimalFormat = DecimalFormat(Constants.TWO_DECIMAL_FORMATTER)

    val userBalance = MutableLiveData<Double>().apply { value = 0.0 }
    val todayExpense = MutableLiveData<Double>().apply { value = 0.0 }
    val weekExpense = MutableLiveData<Double>().apply { value = 0.0 }
    val monthExpense = MutableLiveData<Double>().apply { value = 0.0 }

    init {
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }

        startDay = Calendar.getInstance().timeInMillis - Constants.oneDayMs
        endDay = Calendar.getInstance().timeInMillis

        c.add(Calendar.MONTH, -1)
        startMonth = c.timeInMillis

        c1.add(Calendar.WEEK_OF_MONTH, -1)
        startWeek = c1.timeInMillis

        getCurrentBalance()
        getTodayExpense()
        getWeekExpense()
        getMonthExpense()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun getCurrentBalance() {
        return transactionRepository.getUserBalance(userId)
            .observeOn(rxSchedulers.androidUI())
            .doOnNext { balance -> userBalance.value = twoDigitsFormatter.format(balance).toString().toDouble() }
            .subscribe().disposeBy(compositeDisposable)
    }

    private fun getTodayExpense() {
        return transactionRepository.getExpenseByDate(startDay, endDay, userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnNext { expense -> todayExpense.value = twoDigitsFormatter.format(expense).toString().toDouble() }
            .subscribe().disposeBy(compositeDisposable)
    }

    private fun getWeekExpense() {
        return transactionRepository.getExpenseByDate(startWeek, endDay, userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnNext { expense -> weekExpense.value = twoDigitsFormatter.format(expense).toString().toDouble() }
            .subscribe().disposeBy(compositeDisposable)
    }

    private fun getMonthExpense() {
        return transactionRepository.getExpenseByDate(startMonth, endDay, userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .doOnNext { expense -> monthExpense.value = twoDigitsFormatter.format(expense).toString().toDouble() }
            .subscribe().disposeBy(compositeDisposable)
    }
}