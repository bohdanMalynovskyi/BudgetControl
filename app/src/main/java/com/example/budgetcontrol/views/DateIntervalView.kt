package com.example.budgetcontrol.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.date_interval_view.view.*

class DateIntervalView : LinearLayout {

    lateinit var startDate: TextView
    lateinit var endDate: TextView

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet){
        init()
        setParams(context, attributeSet)
    }

    private fun setParams(context: Context, attributeSet: AttributeSet?){
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