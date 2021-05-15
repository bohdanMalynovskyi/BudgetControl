package com.example.budgetcontrol.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.model.Transaction
import kotlinx.android.synthetic.main.transaction_view.view.*

class TransactionView(context: Context) : LinearLayout(context) {

    private var value: TextView
    private var comment: TextView
    private var date: TextView
    private var editButton: ImageView

    init {
        val view = View.inflate(context, R.layout.transaction_view, this)

        value = view.transactionValue
        comment = view.transactionComment
        date = view.transactionDate
        editButton = view.editButton
    }

    fun setTransaction(transaction: Transaction) {
        this.value.text = transaction.amount.toString()
        this.comment.text = transaction.comment
        this.date.text = transaction.date
    }
}