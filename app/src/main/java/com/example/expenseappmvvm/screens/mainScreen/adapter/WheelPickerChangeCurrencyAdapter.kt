package com.example.expenseappmvvm.screens.mainScreen.adapter

import com.example.expenseappmvvm.utils.enums.CurrencyEnum
import com.example.expenseappmvvm.utils.Constants
import com.super_rabbit.wheel_picker.WheelAdapter

class WheelPickerChangeCurrencyAdapter : WheelAdapter {

    //get item value based on item position in wheel
    override fun getValue(position: Int): String {
        return when (position) {
            0 -> CurrencyEnum.RON.name
            1 -> CurrencyEnum.EUR.name
            2 -> CurrencyEnum.USD.name
            3 -> CurrencyEnum.GBP.name
            4 -> CurrencyEnum.CHF.name
            5 -> CurrencyEnum.AUD.name
            else -> Constants.EMPTY_STRING
        }
    }

    //return a string with the approximate longest text width, for supporting WRAP_CONTENT
    override fun getTextWithMaximumLength(): String {
        return CurrencyEnum.RON.name
    }

    //get item position based on item string value
    override fun getPosition(vale: String): Int {
        return 0
    }

    //return the maximum index
    override fun getMaxIndex(): Int {
        return CurrencyEnum.values().size
    }

    //return the minimum index
    override fun getMinIndex(): Int {
        return 0
    }
}