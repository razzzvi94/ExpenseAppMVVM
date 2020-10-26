package com.example.expenseappmvvm.utils.bidingAdapters

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.utils.enums.CurrencyEnum

@BindingAdapter("changeTextSize")
fun bindTextSize(textView: TextView, size: Int) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
}

@BindingAdapter("changeMarginTop")
fun bindMarginTop(view: View, dimen: Int) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = dimen
    view.layoutParams = layoutParams
}

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("changeIcon")
fun setCurrencyIcon(imageView: ImageView, currency: String) {
    when(currency){
        CurrencyEnum.RON.name -> imageView.setImageResource(R.drawable.ic_flag_ro)
        CurrencyEnum.EUR.name -> imageView.setImageResource(R.drawable.ic_euro)
        CurrencyEnum.USD.name -> imageView.setImageResource(R.drawable.ic_dolar)
        CurrencyEnum.GBP.name -> imageView.setImageResource(R.drawable.ic_pound)
        CurrencyEnum.CHF.name -> imageView.setImageResource(R.drawable.ic_chf)
        CurrencyEnum.AUD.name -> imageView.setImageResource(R.drawable.ic_aud)
    }
}