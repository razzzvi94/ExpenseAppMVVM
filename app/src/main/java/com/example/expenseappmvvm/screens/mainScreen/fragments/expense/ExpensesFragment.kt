package com.example.expenseappmvvm.screens.mainScreen.fragments.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentExpenseBinding
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.TransactionAdapter
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.models.RecyclerTransaction
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.dialog.ExpenseDialog
import com.example.expenseappmvvm.utils.DateUtils
import com.trinnguyen.SegmentView
import kotlinx.android.synthetic.main.fragment_expense.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class ExpensesFragment : Fragment(), SegmentView.OnSegmentItemSelectedListener {

    private val expenseFragmentViewModel: ExpenseFragmentViewModel by viewModel()
    private lateinit var adapter: TransactionAdapter

    override fun onSegmentItemSelected(index: Int) {
        when (index) {
            0 -> {
                expenseFragmentViewModel.getWeekExpense(expenseFragmentViewModel.startWeek)
            }
            1 -> {
                val startMonth = DateUtils.getEndDate(Calendar.MONTH)
                expenseFragmentViewModel.getWeekExpense(startMonth)
            }
            2 -> {
                val startYear = DateUtils.getEndDate(Calendar.YEAR)
                expenseFragmentViewModel.getWeekExpense(startYear)
            }
        }
    }

    override fun onSegmentItemReselected(index: Int) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSegmentComponents()
        interval_segment_view.onSegmentItemSelectedListener = this
        initAdapter()
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentExpenseBinding>(
            inflater,
            R.layout.fragment_expense,
            container,
            false
        ).apply {
            viewModel = this@ExpensesFragment.expenseFragmentViewModel
            lifecycleOwner = this@ExpensesFragment
        }.root
    }

    private fun initObservers() {
        expenseFragmentViewModel.orderedList.observe(viewLifecycleOwner, {
            adapter.updateAdapter(it.toMutableList())
        })
    }

    private fun initSegmentComponents() {
        interval_segment_view.apply {
            setText(0, resources.getString(R.string.this_week))
            setText(1, resources.getString(R.string.this_month))
            setText(2, resources.getString(R.string.this_year))
        }
    }

    private fun initAdapter() {
        context?.let { context ->
            TransactionAdapter(context).run {
                transactions_recycler.adapter = this@run
                adapter = this@run
                adapter.transactionAdapterListener
                    .subscribe({
                        showDialog(it)
                    }, {
                        Timber.e(it.localizedMessage)
                    })
            }
        }
        transactions_recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun showDialog(
        transaction: RecyclerTransaction,
    ) {
        val fm: FragmentManager = this.childFragmentManager
        val dialogFragment = ExpenseDialog(transaction, expenseFragmentViewModel.onItemDelete, expenseFragmentViewModel.onItemEdit)
        dialogFragment.show(fm, "fragmentDialog")
    }
}

