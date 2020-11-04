package com.example.expenseappmvvm.screens.addActionScreen

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Transaction
import com.example.expenseappmvvm.data.database.repositories.TransactionRepository
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.screens.addActionScreen.adapter.models.CategoryItem
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.disposeBy
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class AddActionViewModel(
    private val resourceUtils: ResourceUtils,
    private val rxSchedulers: AppRxSchedulers,
    private val sharedPref: Preferences,
    private val transactionRepository: TransactionRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {
    var openDatePicker = SingleLiveEvent<Any>()
    var goToHomeScreen = SingleLiveEvent<Any>()
    val onItemClick = PublishSubject.create<CategoryItem>()
    var categorySelected: String = resourceUtils.getStringResource(R.string.income)

    val dateTimeText: MutableLiveData<String> = MutableLiveData()
    val amountText: MutableLiveData<String> = MutableLiveData()
    val detailsText: MutableLiveData<String> = MutableLiveData()
    var timestamp: Long = 0L

    private var defaultDetailsText: String = ""
    var defaultDetailsImage: ByteArray = ByteArray(0)

    init {
        itemClickAction()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun selectDate() {
        openDatePicker.call()
    }

    private fun itemClickAction() {
        onItemClick.observeOn(rxSchedulers.androidUI())
            .subscribe({ item ->
                categorySelected = item.categoryName.toLowerCase().capitalize()
            }, {
                Timber.i(it.localizedMessage)
            })
            .disposeBy(compositeDisposable)
    }

    fun saveAction() {
        if (dateTimeText.value != null && amountText.value != null) {
            if (categorySelected == resourceUtils.getStringResource(R.string.income)) {
                saveTransaction()
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.budget_saved),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                saveTransaction()
                Toast.makeText(
                    resourceUtils.getContext(),
                    resourceUtils.getStringResource(R.string.expense_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                resourceUtils.getContext(),
                resourceUtils.getStringResource(R.string.mandatory_fields),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveTransaction() {
        sharedPref.read(Constants.USER_ID, 0L)?.run {
            val transaction = getTransaction(this, categorySelected)
            transactionRepository.insertTransaction(transaction)
                .subscribeOn(rxSchedulers.background())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    goToHomeScreen.call()
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    private fun getTransaction(userId: Long, category: String): Transaction {
        if (detailsText.value != null) {
            defaultDetailsText = detailsText.value.toString()
        }
        val amount = if (amountText.value == null) 0.0 else amountText.value.toString()
            .toDouble()
        return Transaction(
            userId = userId,
            transactionDate = timestamp,
            transactionAmount = amount,
            transactionCategory = category,
            transactionDetails = defaultDetailsText,
            transactionImage = defaultDetailsImage
        )
    }
}