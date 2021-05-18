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

    //todo save dates sorting untill app closes
    //todo disable setting startdate newer than enddate

    private lateinit var startDate: TextView
    private lateinit var endDate: TextView

    private var onDateSetListener: (() -> Unit?)? = null

    init {
        init()
        setDateTextViewOnClickListener(startDate)
        setDateTextViewOnClickListener(endDate)
    }

    fun setDatePickerOnDateSetListener(listener: () -> Unit){
        onDateSetListener = listener
    }

    fun getStartDate(): String{
        return startDate.text.toString()
    }

    fun getEndDate(): String{
        return endDate.text.toString()
    }

    private fun setDate(view: TextView, date: Calendar) {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        view.text = dateFormat.format(date.time)
    }

    private fun setDateTextViewOnClickListener(textView: TextView) {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            setDate(textView, calendar)
            onDateSetListener?.invoke()
        }

        textView.setOnClickListener {
            DatePickerDialog(
                context,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            ).show()
        }
    }

    private fun init() {
        val view = View.inflate(context, R.layout.date_period_view, this)
        startDate = view.startDate
        endDate = view.endDate

        setDefaultDates()
    }

    private fun setDefaultDates(){
        val oneMonthAgoDate = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        val previousMonth = currentDate.get(Calendar.MONTH) - 1

        oneMonthAgoDate.set(
                currentDate.get(Calendar.YEAR),
                previousMonth,
                currentDate.get(Calendar.DAY_OF_MONTH)
        )

        setDate(startDate, oneMonthAgoDate)
        setDate(endDate, currentDate)
    }
}