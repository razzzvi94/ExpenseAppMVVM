package com.example.expenseappmvvm.screens.mainScreen.fragments.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
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
import io.reactivex.subjects.PublishSubject
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
    val orderedList = MutableLiveData<MutableList<RecyclerTransaction>>().apply { value = mutableListOf() }
    val onItemDelete = PublishSubject.create<RecyclerTransaction>()
    val onItemEdit = PublishSubject.create<RecyclerTransaction>()

    init {
        initComponents()
        initTransactionRecyclerViewList()
        editItemFromRecycler()
        deleteItemFormRecycler()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun calculateDynamicBalance(
        orderedList: List<RecyclerTransaction>,
        userBalance: Double
    ): List<RecyclerTransaction> {
        orderedList[0].balance = userBalance
        for (index in 1 until orderedList.size) {
            computeValue(orderedList[index], orderedList[index - 1])
        }

        return orderedList
    }

    private fun computeValue(
        currentValue: RecyclerTransaction,
        previewsValue: RecyclerTransaction
    ) {
        currentValue.balance =
            if (previewsValue.category == CategoryEnum.INCOME.getName(resourceUtils.getContext())
                    .toLowerCase().capitalize()
            ) {
                previewsValue.balance - previewsValue.amount
            } else {
                previewsValue.balance + previewsValue.amount
            }
        orderedList.value = orderedList.value
    }

    private fun initComponents() {
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }
        endDay = Calendar.getInstance().timeInMillis
        val startWeek = DateUtils.getEndDate(Calendar.WEEK_OF_MONTH)
        getWeekExpense(startWeek)
    }

    private fun initTransactionRecyclerViewList() {
        transactionRepository.getUserTransactionsDescending(userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({ transactionList ->
                orderedList.value?.clear()
                transactionList.forEach {
                    orderedList.value!!.add(
                        RecyclerTransaction(
                            it.transactionId,
                            it.transactionDate,
                            it.transactionAmount,
                            it.transactionCategory,
                            Constants.EMPTY_STRING,
                            0.0,
                            it.transactionDetails,
                            it.transactionImage
                        )
                    )
                }
                calculateDynamicBalance(orderedList.value!!, calculateUserBalance(orderedList.value!!))
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }

    private fun calculateUserBalance(listTransaction: MutableList<RecyclerTransaction>): Double{
        var balance = 0.0
        listTransaction.reverse()
        listTransaction.forEach{
            if(it.category == CategoryEnum.INCOME.name.toLowerCase().capitalize()){
                balance += it.amount
            }
            else{
                balance -= it.amount
            }
        }
        return balance
    }

    private fun deleteItemFormRecycler() {
        onItemDelete.observeOn(rxSchedulers.androidUI())
            .subscribe({
                val index =
                    orderedList.value?.indexOfFirst { t -> t.transactionId == it.transactionId }
                if (index != -1) {
                    transactionRepository.deleteTransactionById(it.transactionId)
                }
            }, {
                it.localizedMessage
            })
            .disposeBy(compositeDisposable)
    }

    private fun editItemFromRecycler() {
        return onItemEdit.observeOn(rxSchedulers.background())
            .flatMap { transactionRepository.editTransaction(it.transactionId, it.amount).toObservable() }
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
            }, {
                Timber.e(it.localizedMessage)
            })
            .disposeBy(compositeDisposable)
    }

    fun getWeekExpense(startWeek: Long) {
        transactionRepository.getExpenseByDate(startWeek, endDay, userId)
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
        showHide.value = !showHide.value!!
    }
}