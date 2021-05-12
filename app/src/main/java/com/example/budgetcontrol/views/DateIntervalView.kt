package com.example.budgetcontrol.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.date_interval_view.view.*
import java.util.*

class DateIntervalView : LinearLayout {

    private lateinit var startDate: TextView
    private lateinit var endDate: TextView

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
        getAttributes(context, attributeSet)
    }

    fun setStartDate(startDate: Date) {
        this.startDate.text = startDate.toString()
    }

    fun setEndDate(endDate: Date) {
        this.endDate.text = endDate.toString()
    }

    private fun getAttributes(context: Context, attributeSet: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.DateIntervalView)
        try {
            startDate.text = attributes.getString(R.styleable.DateIntervalView_startDate)
            endDate.text = attributes.getString(R.styleable.DateIntervalView_endDate)
        } finally {
            attributes.recycle()
        }
    }

    private fun init(){
        val view = View.inflate(context, R.layout.date_interval_view, this)
        startDate = view.startDate
        endDate = view.endDate
    }
}