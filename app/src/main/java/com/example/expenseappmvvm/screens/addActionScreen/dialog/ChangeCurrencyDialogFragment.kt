package com.example.expenseappmvvm.screens.addActionScreen.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.rest.CurrencyCoin
import com.example.expenseappmvvm.screens.currencyConverterScreen.adapter.CurrencyAdapter
import com.example.expenseappmvvm.screens.currencyConverterScreen.adapter.models.CurrencyItem
import kotlinx.android.synthetic.main.dialog_change_currency.view.*

class ChangeCurrencyDialogFragment(private val activity: Context): DialogFragment() {

    private lateinit var layout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.dialog_change_currency, container, false)

        initForeignSpinner()
        return layout
    }

    private fun initForeignSpinner() {
        val modelList: MutableList<CurrencyItem> = mutableListOf()
        modelList.run {
            add(CurrencyItem("Currency", 0, 0.0))
            add(CurrencyItem("RON", R.drawable.ic_flag_ro, 0.0))
            add(CurrencyItem("EUR", R.drawable.ic_euro, 0.0))
            add(CurrencyItem("USD", R.drawable.ic_dolar, 0.0))
            add(CurrencyItem("GBP", R.drawable.ic_pound, 0.0))
            add(CurrencyItem("CHF", R.drawable.ic_chf, 0.0))
            add(CurrencyItem("AUD", R.drawable.ic_aud, 0.0))
        }

        val foreignSpinner = CurrencyAdapter(activity, modelList)
        layout.currency_spinner.adapter = foreignSpinner
    }
}