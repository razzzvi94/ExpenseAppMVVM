package com.example.expenseappmvvm.screens.mainScreen.fragments.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Transaction
import com.example.expenseappmvvm.data.database.repositories.TransactionRepository
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.screens.addActionScreen.enums.CategoryEnum
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.models.RecyclerTransaction
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.DateUtils
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.disposeBy
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*

class ExpenseFragmentViewModel(
    private val resourceUtils: ResourceUtils,
    private val sharedPref: Preferences,
    private val transactionRepository: TransactionRepository,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {
    private var endDay: Long = Constants.DEFAULT_LONG
    private var userId: Long = Constants.DEFAULT_LONG
    private val twoDigitsFormatter: DecimalFormat = DecimalFormat(Constants.TWO_DECIMAL_FORMATTER)
    val startWeek = DateUtils.getEndDate(Calendar.WEEK_OF_MONTH)
    var totalExpense = MutableLiveData<String>().apply { value = Constants.EMPTY_EXPENSE }
    val showHide = MutableLiveData<Boolean>().apply { value = false }
    val orderedList = mutableListOf<RecyclerTransaction>()

    private fun calculateDynamicBalance(
        orderedList: List<RecyclerTransaction>,
        userBalance: Double
    ): List<RecyclerTransaction> {
        orderedList[0].balance = userBalance
        orderedList[orderedList.size - 1].balance = orderedList[orderedList.size - 1].amount
        for (index in 1 until orderedList.size) {
            if (orderedList[index].category == CategoryEnum.INCOME.getName(resourceUtils.getContext())
                    .toLowerCase().capitalize()
            ) {
                if (orderedList[index - 1].category == CategoryEnum.INCOME.getName(resourceUtils.getContext())
                        .toLowerCase().capitalize()
                ) {
                    orderedList[index].balance =
                        orderedList[index - 1].balance - orderedList[index - 1].amount
                } else {
                    orderedList[index].balance =
                        orderedList[index - 1].balance + orderedList[index - 1].amount
                }
            } else {
                orderedList[index].balance =
                    orderedList[index - 1].balance + orderedList[index - 1].amount
            }
        }
        orderedList[orderedList.size - 1].balance = orderedList[orderedList.size - 1].amount

        return orderedList
    }

    fun getWeekExpense(startWeek: Long) {
        return transactionRepository.getExpenseByDate(startWeek, endDay, userId)
            .observeOn(rxSchedulers.androidUI())
            .doOnError {
                Timber.e(it.localizedMessage)
            }
            .doOnNext { expense ->
                totalExpense.value = twoDigitsFormatter.format(expense).toString()
            }
            .subscribe({
                Timber.i(resourceUtils.getStringResource(R.string.get_expense_successful))
            }, {
                Timber.e(resourceUtils.getStringResource(R.string.get_expense_failed))
            })
            .disposeBy(compositeDisposable)
    }

    fun seeMore() {
        showHide.value = showHide.value != true
    }

    fun initComponents() {
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }
        endDay = Calendar.getInstance().timeInMillis
        val startWeek = DateUtils.getEndDate(Calendar.WEEK_OF_MONTH)
        getWeekExpense(startWeek)
    }

    fun initTransactionRecyclerViewList() {
            transactionRepository.getUserTransactionsDescending(userId)
                .subscribeOn(rxSchedulers.background())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({transactionList ->
                    for (index in 0 until transactionList.size) {
                        val transactionObject = RecyclerTransaction(
                            transactionList[index].transactionId,
                            transactionList[index].transactionDate,
                            transactionList[index].transactionAmount,
                            transactionList[index].transactionCategory,
                            Constants.EMPTY_STRING,
                            0.0,
                            transactionList[index].transactionDetails,
                            transactionList[index].transactionImage
                        )
                        orderedList.add(transactionObject)
                    }

                    transactionRepository.getUserBalance(userId)
                        .subscribeOn(rxSchedulers.background())
                        .observeOn(rxSchedulers.androidUI())
                        .subscribe({
                            if (orderedList.isNotEmpty()) {
                                calculateDynamicBalance(orderedList, it)
                            }
                        }, {
                            Timber.e(it.localizedMessage)
                        }).disposeBy(compositeDisposable)

                },{
                    Timber.e(it.localizedMessage)
                }).disposeBy(compositeDisposable)
    }
}