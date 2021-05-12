package com.example.budgetcontrol.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.transaction_view.view.*

class TransactionView : LinearLayout {

    private lateinit var value: TextView
    private lateinit var comment: TextView
    private lateinit var date: TextView
    private lateinit var editButton: ImageView

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
        getAttributes(context, attributeSet)
    }

    //TODO create Transaction class????
    fun setTransaction(value: Float, comment: String, date: String) {
        this.value.text = value.toString()
        this.comment.text = comment
        this.date.text = date
    }

    private fun getAttributes(context: Context, attributeSet: AttributeSet?) {
        val attributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.TransactionView)
        try {
            value.text = attributes.getString(R.styleable.TransactionView_value2)
            comment.text = attributes.getString(R.styleable.TransactionView_comment)
            date.text = attributes.getString(R.styleable.TransactionView_date)
        } finally {
            attributes.recycle()
        }
    }

    private fun init() {
        val view = View.inflate(context, R.layout.transaction_view, this)
        value = view.transactionValue
        comment = view.transactionComment
        date = view.transactionDate
        editButton = view.editButton
    }

}