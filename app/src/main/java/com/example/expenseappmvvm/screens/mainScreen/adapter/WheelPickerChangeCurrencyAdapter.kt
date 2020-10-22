package com.example.expenseappmvvm.screens.mainScreen.adapter

import com.super_rabbit.wheel_picker.WheelAdapter

class WheelPickerChangeCurrencyAdapter : WheelAdapter {

    //get item value based on item position in wheel
    override fun getValue(position: Int): String {
        if (position == 0)
            return "RON"
        if (position == 1)
            return "EUR"
        if (position == 2)
            return "USD"
        if (position == 3)
            return "GBP"
        if (position == 4)
            return "CHF"
        if (position == 5)
            return "AUD"

        return ""
    }

    //return a string with the approximate longest text width, for supporting WRAP_CONTENT
    override fun getTextWithMaximumLength(): String {
        return "RON"
    }

    //get item position based on item string value
    override fun getPosition(vale: String): Int {
        return 0
    }

    //return the maximum index
    override fun getMaxIndex(): Int {
        return 5
    }

    //return the minimum index
    override fun getMinIndex(): Int {
        return 0
    }
}