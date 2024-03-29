package com.example.expenseappmvvm.screens.currencyConverterScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isDigitsOnly
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.rest.CurrencyCoin
import com.example.expenseappmvvm.databinding.ActivityCurrencyConverterBinding
import com.example.expenseappmvvm.screens.currencyConverterScreen.adapter.CurrencyAdapter
import com.example.expenseappmvvm.screens.currencyConverterScreen.adapter.models.CurrencyItem
import com.example.expenseappmvvm.utils.enums.CurrencyEnum
import kotlinx.android.synthetic.main.activity_currency_converter.*
import kotlinx.android.synthetic.main.transaction_toolbar.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CurrencyConverterActivity : AppCompatActivity() {

    private val currencyConverterViewModel: CurrencyConverterViewModel by viewModel()
    val currency: CurrencyCoin = CurrencyCoin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityCurrencyConverterBinding>(
            this,
            R.layout.activity_currency_converter
        ).apply {
            viewModel = this@CurrencyConverterActivity.currencyConverterViewModel
            lifecycleOwner = this@CurrencyConverterActivity
        }

        currencyConverterViewModel.apply {
            getUserCurrencyFromDB()
            nativeCurrencyTextChanged(native_currency)
            foreignCurrencyTextChanged(foreign_currency)
        }
        initToolbar()
        initSpinners()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initToolbar() {
        val toolbar = converter_toolBar as Toolbar
        this@CurrencyConverterActivity.run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        toolbar.run {
            title = this@CurrencyConverterActivity.getString(R.string.converter)
            setTitleTextAppearance(context, R.style.HomeToolbarFont)
        }

        toolbar.button_save.visibility = View.GONE
    }

    private fun initSpinners() {
        initForeignSpinner()
        foreign_currency_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currencyConverterViewModel.getPosition(position)
                    if (isDigitsOnly(native_currency.text) && native_currency.isFocused && native_currency.text.isNotEmpty()) {
                        currencyConverterViewModel.calculateRonToValue()
                    }
                    if (isDigitsOnly(foreign_currency.text) && foreign_currency.isFocused && foreign_currency.text.isNotEmpty()) {
                        currencyConverterViewModel.calculateValueToRon()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initForeignSpinner() {
        val modelList: MutableList<CurrencyItem> = mutableListOf()
        modelList.run {
            add(CurrencyItem("Currency", 0))
            CurrencyEnum.values().forEach{
                add(CurrencyItem(it.getName(this@CurrencyConverterActivity), it.getIcon(this@CurrencyConverterActivity)))
            }

        }
        val foreignSpinnerAdapter = CurrencyAdapter(this, modelList)
        foreign_currency_spinner.adapter = foreignSpinnerAdapter
    }

    companion object {
        fun startCurrencyConverter(activity: Activity) {
            val intent = Intent(activity, CurrencyConverterActivity::class.java)
            activity.startActivity(intent)
        }
    }
}