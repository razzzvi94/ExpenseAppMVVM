package com.example.expenseappmvvm.screens.mainScreen.fragments.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentBudgetBinding

class BudgetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBudgetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false)
        return binding.root
    }
}