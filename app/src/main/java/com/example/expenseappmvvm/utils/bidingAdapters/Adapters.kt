package com.example.expenseappmvvm.utils.bidingAdapters

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter

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

//@BindingAdapter("app:errorText")
//fun setErrorMessage(view: TextInputLayout, errorMessage: String) {
//    view.error = errorMessage
//}