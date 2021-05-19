package com.example.budgetcontrol.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.db.model.Transaction
import com.example.budgetcontrol.dialog.RecordDialog
import com.example.budgetcontrol.enum.BudgetComponent
import kotlinx.android.synthetic.main.transaction_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class TransactionView(context: Context, private val transaction: Transaction) : LinearLayout(context) {

    private var budgetComponent: BudgetComponent

    init {
        val view = View.inflate(context, R.layout.transaction_view, this)

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

        transactionEditButton.setOnClickListener(this::handleEditButtonClick)
    }

    private fun handleEditButtonClick(view: View) {
        val dialog = RecordDialog(
                context,
                when (budgetComponent) {
                    BudgetComponent.INCOME -> context?.getString(R.string.income)
                    BudgetComponent.COSTS -> context?.getString(R.string.costs)
                },
                when (budgetComponent) {
                    BudgetComponent.INCOME -> context?.getString(R.string.source)
                    BudgetComponent.COSTS -> context?.getString(R.string.costs_edit_text_hint)
                },
                this::updateTransaction
        )
        dialog.setOnDismissListener {
            //todo add the lest transaction
        }
        dialog.show()
    }

    private fun updateTransaction(amount: Float, description: String) {
        val transaction = this.transaction.copy(
                amount = when (budgetComponent) {
                    BudgetComponent.INCOME -> amount
                    BudgetComponent.COSTS -> (-amount)
                },
                comment = description
        )

        GlobalScope.launch {
            BudgetControlDB.getInstance(context)
                    .transactionDao()
                    .update(transaction)
        }
    }
}