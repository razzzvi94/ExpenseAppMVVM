package com.example.expenseappmvvm.screens.currencyConverterScreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screens.addActionScreen.AddActionActivity

class CurrencyConverterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
    }

    companion object {
        fun startCurrencyConverter(activity: Activity) {
            val intent = Intent(activity, CurrencyConverterActivity::class.java)
            activity.startActivity(intent)
        }
    }
}