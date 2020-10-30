package com.example.expenseappmvvm.screens.mainScreen.fragments.expense.dialog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screens.addActionScreen.enums.CategoryEnum
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.models.RecyclerTransaction
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.TimeUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.dialog_transaction_info.*
import kotlinx.android.synthetic.main.dialog_transaction_info.view.*
import java.text.DecimalFormat


class ExpenseDialog(
    private var transaction: RecyclerTransaction,
    private var listenerDelete: PublishSubject<RecyclerTransaction>,
    private var listenerEdit: PublishSubject<RecyclerTransaction>
) : DialogFragment() {
    private lateinit var layout: View
    private val twoDigitsFormatter: DecimalFormat = DecimalFormat(Constants.TWO_DECIMAL_FORMATTER)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.dialog_transaction_info, container, false)

        initComponents(transaction)
        initButtons()
        return layout
    }

    private fun initComponents(transaction: RecyclerTransaction) {
        layout.category_name.text = transaction.category
        layout.categoryImage_section.setBackgroundResource(R.drawable.category_box_shape)
        when (transaction.category) {
            CategoryEnum.INCOME.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.INCOME.getIcon(layout.context))
            }
            CategoryEnum.FOOD.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.FOOD.getIcon(layout.context))
            }
            CategoryEnum.CAR.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.CAR.getIcon(layout.context))
            }
            CategoryEnum.CLOTHES.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.CLOTHES.getIcon(layout.context))
            }
            CategoryEnum.SAVINGS.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.SAVINGS.getIcon(layout.context))
            }
            CategoryEnum.HEALTH.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.HEALTH.getIcon(layout.context))
            }
            CategoryEnum.BEAUTY.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.BEAUTY.getIcon(layout.context))
            }
            CategoryEnum.TRAVEL.getName(layout.context).toLowerCase().capitalize() -> {
                layout.category_image.setImageResource(CategoryEnum.TRAVEL.getIcon(layout.context))
            }
        }
        layout.apply {
            dialog_date_editText.setText(TimeUtils.gmtToLocalTime(transaction.date))
            dialog_amount_editText.setText(twoDigitsFormatter.format(transaction.amount).toString())
            dialog_details_editText.setText(transaction.details)
            if (transaction.imageDetails.isNotEmpty()) {
                dialog_details_ImageView.setImageBitmap(byteArrayToImage(transaction.imageDetails))
            }
        }
    }

    private fun initButtons() {
        layout.apply {
            close_dialog_btn.setOnClickListener { dialog?.dismiss() }
            dialog_delete_textView.setOnClickListener(deleteOnClickListener)
            dialog_edit_textView.setOnClickListener(editOnClickListener)
        }
    }

    private fun byteArrayToImage(bytArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytArray, 0, bytArray.size)
    }

    private val deleteOnClickListener = View.OnClickListener {
        listenerDelete.onNext(transaction)
        dialog?.dismiss()
    }

    private val editOnClickListener = View.OnClickListener {
        val editTransaction = RecyclerTransaction(
            transactionId = transaction.transactionId,
            date = transaction.date,
            amount = dialog_amount_editText.text.toString().toDouble(),
            category = transaction.category,
            name = transaction.name,
            balance = 0.0,
            details = transaction.details,
            imageDetails = transaction.imageDetails
        )
        listenerEdit.onNext(editTransaction)
        dialog?.dismiss()
    }
}