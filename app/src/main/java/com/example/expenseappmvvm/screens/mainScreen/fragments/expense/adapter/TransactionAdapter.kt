package com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screens.addActionScreen.enums.CategoryEnum
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.models.RecyclerTransaction
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.TimeUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.transaction_item.view.*
import java.text.DecimalFormat

class TransactionAdapter(
    private val context: Context
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val twoDigitsFormatter: DecimalFormat = DecimalFormat(Constants.TWO_DECIMAL_FORMATTER)
    val transactionAdapterListener = PublishSubject.create<RecyclerTransaction>()
    private var arrayList: MutableList<RecyclerTransaction> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)

        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun updateAdapter(toMutableList: MutableList<RecyclerTransaction>) {
        val oldList = arrayList.toMutableList()
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            TransactionItemDiffCallback(
                oldList,
                toMutableList
            )
        )
        arrayList = toMutableList
        diffResult.dispatchUpdatesTo(this)
    }

    class TransactionItemDiffCallback(
        var oldTransactionList: MutableList<RecyclerTransaction>,
        var newTransactionList: MutableList<RecyclerTransaction>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldTransactionList.size
        }

        override fun getNewListSize(): Int {
            return newTransactionList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldTransactionList[oldItemPosition].transactionId == newTransactionList[newItemPosition].transactionId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldObj = oldTransactionList[oldItemPosition]
            val newObj = newTransactionList[newItemPosition]
            return oldObj.category.equals(newObj.category, true) && oldObj.date == newObj.date && oldObj.amount == newObj.amount && oldObj.balance == newObj.balance
        }

    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun setCategoryIcon(category: String, amount: Double) {
            when (category) {
                CategoryEnum.INCOME.getName(context).toLowerCase().capitalize() -> {
                    itemView.run {
                        transaction_category_icon.setImageResource(CategoryEnum.INCOME.getIcon(context))
                        transaction_amount_value_textView.text = "+" + twoDigitsFormatter.format(amount).toString()
                        transaction_amount_value_textView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    }
                }

                CategoryEnum.FOOD.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.FOOD.getIcon(context))
                }
                CategoryEnum.CAR.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.CAR.getIcon(context))
                }
                CategoryEnum.CLOTHES.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.CLOTHES.getIcon(context))
                }
                CategoryEnum.SAVINGS.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.SAVINGS.getIcon(context))
                }
                CategoryEnum.HEALTH.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.HEALTH.getIcon(context))
                }
                CategoryEnum.BEAUTY.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.BEAUTY.getIcon(context))
                }
                CategoryEnum.TRAVEL.getName(context).toLowerCase().capitalize() -> {
                    itemView.transaction_category_icon.setImageResource(CategoryEnum.TRAVEL.getIcon(context))
                }
            }
        }

        fun bind(currentItem: RecyclerTransaction) {
            itemView.run {
                transaction_date_textView.text = TimeUtils.transactionTimeFormat(context, currentItem.date)
                transaction_amount_value_textView.text = "-" + twoDigitsFormatter.format(currentItem.amount).toString()
                transaction_category_name_textView.text = currentItem.category
                if(currentItem.category == context.getString(R.string.income)){
                    transaction_name_textView.text = context.getString(R.string.income)
                }
                else{
                    transaction_name_textView.text = context.getString(R.string.expense)
                }

                balance_value_textView.text = twoDigitsFormatter.format(currentItem.balance).toString()
                if (currentItem.balance < 0) {
                    balance_value_textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    transaction_amount_value_textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
            }

            setCategoryIcon(currentItem.category, currentItem.amount)

            itemView.transaction_item.setOnClickListener { transactionAdapterListener.onNext(currentItem) }
        }
    }
}