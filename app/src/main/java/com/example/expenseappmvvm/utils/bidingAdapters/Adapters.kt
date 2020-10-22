package com.example.expenseappmvvm.utils.bidingAdapters

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.expenseappmvvm.R

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
    if(currency == "RON")
        imageView.setImageResource(R.drawable.ic_flag_ro)
    if(currency == "EUR")
        imageView.setImageResource(R.drawable.ic_euro)
    if(currency == "USD")
        imageView.setImageResource(R.drawable.ic_dolar)
    if(currency == "GBP")
        imageView.setImageResource(R.drawable.ic_pound)
    if(currency == "CHF")
        imageView.setImageResource(R.drawable.ic_chf)
    if(currency == "AUD")
        imageView.setImageResource(R.drawable.ic_aud)
}