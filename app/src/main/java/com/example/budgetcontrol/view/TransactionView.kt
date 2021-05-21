package com.example.budgetcontrol.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.model.Transaction
import com.example.budgetcontrol.enum.BudgetComponent
import kotlinx.android.synthetic.main.transaction_view.view.*
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class TransactionView(context: Context, transaction: Transaction, private val editButtonClickHandler: (transaction: Transaction) -> Unit) : LinearLayout(context) {

    private var budgetComponent: BudgetComponent

    init {
        View.inflate(context, R.layout.transaction_view, this)

        budgetComponent = when (transaction.amount > 0) {
            true -> BudgetComponent.INCOME
            false -> BudgetComponent.COSTS
        }

        transactionValue.text = context.getString(
                R.string.income_costs_value_placeholder,
                when (budgetComponent) {
                    BudgetComponent.INCOME -> "+"
                    BudgetComponent.COSTS -> "-"
                },
                abs(transaction.amount)
        )
        transactionComment.text = transaction.comment
        transactionDate.text = transaction.date

        transactionEditButton.setOnClickListener {
            editButtonClickHandler(transaction)
        }
    }


}