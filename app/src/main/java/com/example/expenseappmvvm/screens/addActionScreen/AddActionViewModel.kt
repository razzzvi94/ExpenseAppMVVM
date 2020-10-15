package com.example.expenseappmvvm.screens.addActionScreen

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.screens.addActionScreen.adapter.models.CategoryItem
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.disposeBy
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class AddActionViewModel(
    private val resourceUtils: ResourceUtils,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {
    var openDatePicker = SingleLiveEvent<Any>()
    val onItemClick = PublishSubject.create<CategoryItem>()
    var categorySelected: String = resourceUtils.getStringResource(R.string.income)
    val emptyField = MutableLiveData<Boolean>().apply { value = true }
    val saveIncome = MutableLiveData<Boolean>().apply { value = true }

    fun selectDate() {
        openDatePicker.call()
    }

    fun itemClickAction() {
        onItemClick.observeOn(rxSchedulers.androidUI())
            .subscribe({ item ->
                categorySelected = item.categoryName.toLowerCase().capitalize()
            }, {
                Timber.i(it.localizedMessage)
            })
            .disposeBy(compositeDisposable)
    }

    fun saveAction() {
        if (emptyField.value == false) {
            if (saveIncome.value == true) {
                //saveIncomeTransaction()
                Toast.makeText(resourceUtils.getContext(), "Income saved now", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //saveExpenseTransaction()
                Toast.makeText(resourceUtils.getContext(), "Expense saved now", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                resourceUtils.getContext(),
                resourceUtils.getStringResource(R.string.mandatory_fields),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}