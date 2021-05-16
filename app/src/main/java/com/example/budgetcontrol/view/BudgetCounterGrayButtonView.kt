package com.example.budgetcontrol.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.budgetcontrol.R
import com.example.budgetcontrol.enum.BudgetComponent
import kotlinx.android.synthetic.main.income_costs_counter.view.*

class BudgetCounterGrayButtonView(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {

    private lateinit var budgetComponent: BudgetComponent
    private var value: TextView

    init {
        val view = View.inflate(context, R.layout.income_costs_counter, this)
        value = view.incomeCostsValue

        getAttributes(context, attributeSet)
    }

    fun setValue(value: Float) {
        this.value.text = resources.getString(
                R.string.income_costs_value_placeholder,
                when (budgetComponent) {
                    BudgetComponent.INCOME -> "+"
                    BudgetComponent.COSTS -> "-"
                },
                value
        )
    }

    private fun setTitle(budgetComponent: BudgetComponent) {
        incomeCostsTextView.text = resources.getString(
                when (budgetComponent) {
                    BudgetComponent.INCOME -> R.string.income
                    BudgetComponent.COSTS -> R.string.costs
                })
    }

    private fun getAttributes(context: Context, attributeSet: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.BudgetCounterGrayButtonView
        )
        try {
            budgetComponent = when (attributes.getInteger(R.styleable.BudgetCounterGrayButtonView_budget_component, 0)) {
                0 -> BudgetComponent.INCOME
                else -> BudgetComponent.COSTS
            }
            setTitle(budgetComponent)
        } finally {
            attributes.recycle()
        }
    }
}