package com.example.expenseappmvvm.screens.mainScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityHomeBinding
import com.example.expenseappmvvm.screens.addActionScreen.AddActionActivity
import com.example.expenseappmvvm.screens.currencyConverterScreen.CurrencyConverterActivity
import com.example.expenseappmvvm.screens.loginScreen.LoginActivity
import com.example.expenseappmvvm.screens.mainScreen.adapter.HomePagerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.get


class HomeActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel = get()
    private var isFABOpen: Boolean = false
    private val fragmentAdapter = HomePagerAdapter(this, 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home).apply {
            viewModel = this@HomeActivity.homeViewModel
            lifecycleOwner = this@HomeActivity
        }

        initFabMenu()
        observeLiveData()
        homeViewModel.getUserName()
        initViewPager()
    }

    private fun initFabMenu() {
        fabMenu.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }
    }

    private fun showFABMenu() {
        isFABOpen = true
        fabAddAction.animate().translationY(-resources.getDimension(R.dimen.space_34dp))
            .translationX(-resources.getDimension(R.dimen.space_74dp))
        fabConverter.animate().translationY(-resources.getDimension(R.dimen.space_74dp))
        fabLogout.animate().translationY(-resources.getDimension(R.dimen.space_34dp))
            .translationX(resources.getDimension(R.dimen.space_74dp))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fabAddAction.animate().translationY(0f).translationX(0f)
        fabConverter.animate().translationY(0f).translationX(0f)
        fabLogout.animate().translationY(0f).translationX(0f)
    }

    private fun observeLiveData() {
        homeViewModel.apply {
            goToAddActionScreen.observe(this@HomeActivity, { AddActionActivity.startAddAction(this@HomeActivity) })
            goToCurrencyConverterScreen.observe(this@HomeActivity, { CurrencyConverterActivity.startCurrencyConverter(this@HomeActivity) })
            goToLogout.observe(this@HomeActivity, { LoginActivity.startLogin(this@HomeActivity) })
            goToBudgetFragment.observe(this@HomeActivity, { fragmentViewPager.currentItem = 0})
            goToExpenseFragment.observe(this@HomeActivity, { fragmentViewPager.currentItem = 1})
        }
    }

    private fun initViewPager() {
        fragmentViewPager.adapter = fragmentAdapter
        fragmentViewPager.currentItem = 0

        fragmentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        homeViewModel.isExpenseTabSelected.value = false
                    }
                    1 ->{
                        homeViewModel.isExpenseTabSelected.value = true
                    }
                }
            }
        })
    }

    companion object {
        fun startHome(activity: Activity) {
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
        }
    }
}