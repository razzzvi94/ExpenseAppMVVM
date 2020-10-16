package com.example.expenseappmvvm.screens.addActionScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.CategoryItemBinding
import com.example.expenseappmvvm.screens.addActionScreen.adapter.models.CategoryItem
import io.reactivex.subjects.PublishSubject

class CategoryAdapter(
    private val arrayList: List<CategoryItem>,
    private val categoryAdapterListener: PublishSubject<CategoryItem>,
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = DataBindingUtil.inflate<CategoryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.category_item,
            parent,
            false
        )
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(arrayList[position], position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class CategoryViewHolder(itemView: CategoryItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private val bindingHelper = itemView

        fun bind(categoryItem: CategoryItem, position: Int) {
            itemView.run {
                itemView.setOnClickListener {
                    deselectItem()
                    categoryItem.isSelected = !categoryItem.isSelected
                    categoryAdapterListener.onNext(categoryItem)
                    notifyItemChanged(position)
                }

                bindingHelper.apply {
                    this.categoryItem = categoryItem.apply {
                        this.categoryName = categoryName.toLowerCase().capitalize()
                    }
                    executePendingBindings()
                }
            }
        }

        private fun deselectItem() {
            val selectedItem = arrayList.indexOf(arrayList.find { it.isSelected })
            if (selectedItem > -1) {
                arrayList[selectedItem].isSelected = false
                notifyItemChanged(selectedItem)
            }
        }
    }
}