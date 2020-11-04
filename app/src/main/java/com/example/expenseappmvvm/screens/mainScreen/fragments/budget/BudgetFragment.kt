package com.example.expenseappmvvm.screens.mainScreen.fragments.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentBudgetBinding
import org.koin.android.viewmodel.ext.android.viewModel

class BudgetFragment : Fragment() {

    private val budgetFragmentViewModel: BudgetFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentBudgetBinding>(
            inflater,
            R.layout.fragment_budget,
            container,
            false
        ).apply {
            viewModel = this@BudgetFragment.budgetFragmentViewModel
            lifecycleOwner = this@BudgetFragment
        }.root
    }
}