package com.example.expenseappmvvm.screens.currencyConverterScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.screens.currencyConverterScreen.adapter.CurrencyAdapter
import com.example.expenseappmvvm.screens.currencyConverterScreen.adapter.models.CurrencyItem
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.rest.CurrencyCoin
import com.example.expenseappmvvm.databinding.ActivityCurrencyConverterBinding
import kotlinx.android.synthetic.main.activity_currency_converter.*
import kotlinx.android.synthetic.main.transaction_toolbar.view.*
import org.koin.android.ext.android.get

class CurrencyConverterActivity : AppCompatActivity() {

    private val currencyConverterViewModel: CurrencyConverterViewModel = get()
    private val currencyObj = CurrencyCoin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityCurrencyConverterBinding>(this, R.layout.activity_currency_converter).apply {
            viewModel = this@CurrencyConverterActivity.currencyConverterViewModel
            lifecycleOwner = this@CurrencyConverterActivity
        }

        currencyConverterViewModel.getCurrency()
        initToolbar()
        initSpinners(currencyObj)
        //localCurrencyChanged(native_currency)
        //foreignCurrencyChanged(foreign_currency)
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

    private fun initSpinners(currencyObj: CurrencyCoin?) {
        if (currencyObj == null) return
        initForeignSpinner(currencyObj)
        initNativeSpinner()
        foreign_currency_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initForeignSpinner(currencyObj: CurrencyCoin) {
        val modelList: MutableList<CurrencyItem> = mutableListOf()
        modelList.run {
            add(CurrencyItem("Currency", 0, 0.0))
            add(CurrencyItem("EUR", R.drawable.ic_euro, currencyObj.EUR))
            add(CurrencyItem("USD", R.drawable.ic_dolar, currencyObj.USD))
            add(CurrencyItem("GBP", R.drawable.ic_pound, currencyObj.GBP))
            add(CurrencyItem("CHF", R.drawable.ic_chf, currencyObj.CHF))
            add(CurrencyItem("AUD", R.drawable.ic_aud, currencyObj.AUD))
        }

        val foreignSpinner = CurrencyAdapter(this, modelList)
        foreign_currency_spinner.adapter = foreignSpinner
    }

    private fun initNativeSpinner() {
        val nativeList: MutableList<CurrencyItem> = mutableListOf()
        nativeList.add(CurrencyItem("RON", R.drawable.ic_flag_ro, 1.0))
        val nativeSpinner = CurrencyAdapter(this, nativeList)
        native_currency_spinner.adapter = nativeSpinner
    }

//    private fun localCurrencyChanged(native_currency: EditText) {
//        native_currency.addTextChangedListener(object :
//            TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {calculateRonToValue(Integer.parseInt(s.toString()).toDouble())}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//    }
//
//    private fun foreignCurrencyChanged(foreign_currency: EditText) {
//        foreign_currency.addTextChangedListener(object :
//            TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {calculateValueToRon(Integer.parseInt(s.toString()).toDouble())}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//    }
//
//
//    fun calculateRonToValue(amount: Double) {
//        val item = foreign_currency_spinner.selectedItem as CurrencyItem
//        foreign_currency.setText((amount * item.currencyValue).toString())
//    }
//
//
//    fun calculateValueToRon(amount: Double) {
//        val item = foreign_currency_spinner.selectedItem as CurrencyItem
//        native_currency.setText((amount / item.currencyValue).toString())
//    }

    companion object {
        fun startCurrencyConverter(activity: Activity) {
            val intent = Intent(activity, CurrencyConverterActivity::class.java)
            activity.startActivity(intent)
        }
    }
}