package com.example.budgetcontrol.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.model.Transaction
import kotlinx.android.synthetic.main.transaction_view.view.*
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class TransactionView(context: Context, transaction: Transaction) : LinearLayout(context) {

    private var value: TextView
    private var comment: TextView
    private var date: TextView
    private var editButton: ImageView

    init {
        val view = View.inflate(context, R.layout.transaction_view, this)

        value = view.transactionValue
        value.text = context.getString(
                R.string.income_costs_value_placeholder,
                if (transaction.amount > 0) "+" else "-",
                abs(transaction.amount)
        )

        comment = view.transactionComment
        comment.text = transaction.comment

        date = view.transactionDate
        date.text = transaction.date

        editButton = view.editButton
//        editButton.setOnClickListener()
    }
}