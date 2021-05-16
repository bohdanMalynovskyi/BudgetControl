package com.example.budgetcontrol.view

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.date_period_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class DatePeriodView(
    context: Context,
    attributeSet: AttributeSet?
) : LinearLayout(context, attributeSet) {

    companion object{
        const val DATE_FORMAT = "dd-MM-yyyy"
    }

    private lateinit var startDate: TextView
    private lateinit var endDate: TextView

    init {
        init()
        setDateTextViewOnClickListener(startDate)
        setDateTextViewOnClickListener(endDate)
    }

    private fun setDate(view: TextView, date: Calendar) {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        view.text = dateFormat.format(date.time)
        //todo refresh data in fragment
    }

    private fun setDateTextViewOnClickListener(view: TextView) {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            setDate(view, calendar)
        }

        view.setOnClickListener {
            DatePickerDialog(
                context,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_WEEK),
            ).show()
        }
    }

    private fun init() {
        val view = View.inflate(context, R.layout.date_period_view, this)
        startDate = view.startDate
        endDate = view.endDate
    }
}