package com.example.expenseappmvvm.screens.mainScreen.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screens.mainScreen.adapter.WheelPickerChangeCurrencyAdapter
import com.example.expenseappmvvm.screens.mainScreen.interfaces.BottomSheetListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker
import kotlinx.android.synthetic.main.change_currency_bottom_sheet.view.*

class ChangeCurrencyBottomSheetDialog() : BottomSheetDialogFragment() {

    var currency: String = ""
    var mListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.change_currency_bottom_sheet, container, false)
        // Set rounded wrap enable
        view.currency_wheelPicker.setSelectorRoundedWrapPreferred(true)
        view.currency_wheelPicker.setSelectedTextColor(R.color.color_4_blue)
        view.currency_wheelPicker.setUnselectedTextColor(R.color.color_3_dark_blue)
        view.currency_wheelPicker.setAdapter(WheelPickerChangeCurrencyAdapter())

        view.currency_wheelPicker.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                mListener!!.selectedCurrency(newVal)
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as BottomSheetListener
    }
}