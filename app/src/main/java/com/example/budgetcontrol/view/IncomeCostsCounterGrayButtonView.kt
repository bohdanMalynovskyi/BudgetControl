package com.example.budgetcontrol.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.income_costs_counter.view.*

class IncomeCostsCounterGrayButtonView : ConstraintLayout {

    private var isIncome: Boolean = true
    private lateinit var value: TextView

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
        getAttributes(context, attributeSet)
    }

    fun setIncomeOrCosts(value: Float) {
        this.value.text = resources.getString(R.string.income_costs_value_placeholder, if (isIncome) "+" else "-", value.toString())
        incomeCostsTextView.text = resources.getString(if (isIncome) R.string.income else R.string.costs)
    }

    private fun getAttributes(context: Context, attributeSet: AttributeSet?) {
        val attributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.IncomeCostsCounterGrayButtonView)
        try {
            val value = attributes.getFloat(R.styleable.IncomeCostsCounterGrayButtonView_value, 0F)
            isIncome = attributes.getBoolean(R.styleable.IncomeCostsCounterGrayButtonView_isIncome, true)
            setIncomeOrCosts(value)
        } finally {
            attributes.recycle()
        }
    }

    private fun init() {
        val view = View.inflate(context, R.layout.income_costs_counter, this)
        value = view.incomeCostsValue
    }

}