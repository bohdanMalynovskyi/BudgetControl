package com.example.budgetcontrol

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.income_costs_counter.view.*

class IncomeCostsCounterView : ConstraintLayout {

    var isIncome: Boolean = true
    lateinit var value: TextView

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
        setParams(context, attributeSet)
    }

    fun setIncomeOrCosts(isIncome: Boolean) {
        this.isIncome = isIncome
        numberSign.text = if (isIncome) "+" else "-"
        incomeCostsTextView.text = resources.getString(if (isIncome) R.string.income else R.string.costs)
    }

    private fun setParams(context: Context, attributeSet: AttributeSet?) {
        val attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.IncomeCostsCounterView)
        try {
            value.text = attributes.getFloat(R.styleable.IncomeCostsCounterView_value, 0F).toString()
            val isIncomeAttribute = attributes.getBoolean(R.styleable.IncomeCostsCounterView_isIncome, true)
            setIncomeOrCosts(isIncomeAttribute)
        } finally {
            attributes.recycle()
        }
    }

    private fun init() {
        val view = View.inflate(context, R.layout.income_costs_counter, this)
        value = view.incomeCostsValue
    }

}