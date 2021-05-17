package com.example.budgetcontrol.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.gauss_number_view.view.*

class GaussNumberView(context: Context?, value: Int) : LinearLayout(context) {

    private var gaussNumber: TextView
    private var isCollected: Boolean = false

    init {
        val view = View.inflate(context, R.layout.gauss_number_view, this)
        gaussNumber = view.gaussNumberTextView
        gaussNumber.text = value.toString()
    }

    fun getValue(): Int {
        return gaussNumber.text.toString().toInt()
    }

    fun setStatus(isCollected: Boolean) {
        collectedGaussNumberImageView.visibility = if (isCollected) VISIBLE else INVISIBLE
        this.isCollected = isCollected
    }

    fun isCollected(): Boolean {
        return isCollected
    }
}