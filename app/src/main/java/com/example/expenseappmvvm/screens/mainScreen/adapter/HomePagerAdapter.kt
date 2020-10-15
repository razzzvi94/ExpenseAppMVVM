package com.example.expenseappmvvm.screens.mainScreen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.expenseappmvvm.screens.mainScreen.fragments.budget.BudgetFragment
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.ExpensesFragment

class HomePagerAdapter(fa: FragmentActivity, private val numOfTabs: Int): FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                BudgetFragment()
            }
            1 -> {
                ExpensesFragment()
            }
            else -> {
                BudgetFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return numOfTabs
    }
}