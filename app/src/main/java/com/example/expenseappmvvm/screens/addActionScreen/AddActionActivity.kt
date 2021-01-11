package com.example.expenseappmvvm.screens.addActionScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityAddActionBinding
import com.example.expenseappmvvm.screens.addActionScreen.adapter.CategoryAdapter
import com.example.expenseappmvvm.screens.addActionScreen.adapter.models.CategoryItem
import com.example.expenseappmvvm.screens.addActionScreen.adapter.models.DateTime
import com.example.expenseappmvvm.screens.addActionScreen.dialog.ChangeCurrencyDialogFragment
import com.example.expenseappmvvm.screens.addActionScreen.enums.CategoryEnum
import com.example.expenseappmvvm.screens.mainScreen.HomeActivity
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.models.RecyclerTransaction
import com.example.expenseappmvvm.screens.mainScreen.fragments.expense.dialog.ExpenseDialog
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_add_action.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AddActionActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val addActionViewModel: AddActionViewModel by viewModel()
    private var dateTime = DateTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityAddActionBinding>(this, R.layout.activity_add_action)
            .apply {
                viewModel = this@AddActionActivity.addActionViewModel
                lifecycleOwner = this@AddActionActivity
            }

        initToolbar()
        observeLiveData()
        initCategoryGrid(addActionViewModel.onItemClick, setCategoryData())
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateTime.apply {
            savedDay = dayOfMonth
            savedMonth = month + 1
            savedYear = year
        }
        getDateTimeCalendar()
        TimePickerDialog(this, this, dateTime.hour, dateTime.minute, true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        dateTime.apply {
            savedHour = hourOfDay
            savedMinute = minute
        }

        date_EditText.setText(
            dateTime.savedYear.toString() + "-" +
                    dateTime.savedMonth.toString() + "-" +
                    dateTime.savedDay.toString() + " at " +
                    dateTime.savedHour.toString() + ":" +
                    dateTime.savedMinute.toString()
        )
        addActionViewModel.timestamp = getUTCTimestamp()
    }

    private fun observeLiveData() {
        addActionViewModel.apply {
            openDatePicker.observe(this@AddActionActivity, { initDatePicker() })
            goToHomeScreen.observe(this@AddActionActivity, { HomeActivity.startHome(this@AddActionActivity) })
            openCurrencyDialog.observe(this@AddActionActivity, { showDialog() })
        }
    }

    private fun initToolbar() {
        val toolbar = screenToolbar as Toolbar
        this.run {
            setSupportActionBar(toolbar)
            supportActionBar?.let { it.setDisplayShowTitleEnabled(false) }
            supportActionBar?.let { it.setDisplayHomeAsUpEnabled(true) }
            supportActionBar?.let { it.setDisplayShowHomeEnabled(true) }
        }
        toolbar.run {
            title = this@AddActionActivity.getString(R.string.add_action)
            setTitleTextAppearance(this@AddActionActivity, R.style.HomeToolbarFont)
        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        dateTime.apply {
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)
            hour = cal.get(Calendar.HOUR)
            minute = cal.get(Calendar.MINUTE)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getUTCTimestamp(): Long {
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val dateString = dateTime.savedDay.toString() + "-" +
                dateTime.savedMonth.toString() + "-" +
                dateTime.savedYear.toString() + " " +
                dateTime.savedHour.toString() + ":" +
                dateTime.savedMinute.toString()
        val date = sdf.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }

    private fun initDatePicker() {
        getDateTimeCalendar()
        val datePickerDialog =
            DatePickerDialog(this, this, dateTime.year, dateTime.month, dateTime.day)
        datePickerDialog.apply {
            show()
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    private fun initCategoryGrid(
        listener: PublishSubject<CategoryItem>,
        categoryData: List<CategoryItem>
    ) {
        val numberOfColumns = 4
        grid_recycler.run {
            layoutManager = GridLayoutManager(this@AddActionActivity, numberOfColumns)
            adapter = CategoryAdapter(categoryData, listener)
            setHasFixedSize(true)
        }
    }

    private fun setCategoryData(): List<CategoryItem> {
        val list: MutableList<CategoryItem> = mutableListOf()
        enumValues<CategoryEnum>().forEach {
            val selected = it.getName(this)
                .equals(CategoryEnum.INCOME.getName(this), true)
            list.add(CategoryItem(it.getIcon(this), it.getName(this), selected))
        }
        return list
    }

    private fun showDialog() {
        val dialogFragment = ChangeCurrencyDialogFragment(this)
        dialogFragment.show(supportFragmentManager, "fragmentDialog")
    }

    companion object {
        fun startAddAction(activity: Activity) {
            val intent = Intent(activity, AddActionActivity::class.java)
            activity.startActivity(intent)
        }
    }
}