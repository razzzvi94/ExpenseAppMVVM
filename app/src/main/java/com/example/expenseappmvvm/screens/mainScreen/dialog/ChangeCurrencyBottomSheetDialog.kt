package com.example.expenseappmvvm.screens.mainScreen.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screens.mainScreen.HomeViewModel
import com.example.expenseappmvvm.screens.mainScreen.adapter.WheelPickerChangeCurrencyAdapter
import com.example.expenseappmvvm.screens.mainScreen.interfaces.BottomSheetListener
import com.example.expenseappmvvm.utils.enums.CurrencyEnum
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker
import kotlinx.android.synthetic.main.change_currency_bottom_sheet.view.*

class ChangeCurrencyBottomSheetDialog(private val position: Int, private val viewModel: HomeViewModel) : BottomSheetDialogFragment() {

    var currency: String = ""
    var mListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.change_currency_bottom_sheet, container, false)
        view.currency_wheelPicker.apply {
            setSelectorRoundedWrapPreferred(true)          // Set rounded wrap enable
            setSelectedTextColor(R.color.color_4_blue)
            setUnselectedTextColor(R.color.color_3_dark_blue)
            setAdapter(WheelPickerChangeCurrencyAdapter())
            post { view.currency_wheelPicker.smoothScrollTo(position) }
            setOnValueChangeListener(object : OnValueChangeListener {
                override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                    if(newVal == ""){
                        mListener!!.selectedCurrency(CurrencyEnum.RON.name, CurrencyEnum.RON.name)
                    }
                    else{
                        mListener!!.selectedCurrency(oldVal, newVal)
                    }
                }
            })
        }

        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.saveUserPreferredCurrency()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as BottomSheetListener
    }
}