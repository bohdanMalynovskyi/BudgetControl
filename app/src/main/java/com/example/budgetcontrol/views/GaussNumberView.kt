package com.example.budgetcontrol.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.gauss_number_view.view.*

class GaussNumberView(context: Context?) : LinearLayout(context) {

    private var gaussNumber: TextView
    private var isCollected: Boolean = false

    init {
        val view = View.inflate(context, R.layout.gauss_number_view, this)
        gaussNumber = view.gaussNumberTextView
        setup()
    }

    fun setGaussNumberText(number: String) {
        gaussNumber.text = number
    }

    private fun setup() {
        setOnClickListener {
            collectedGaussNumberImageView.visibility = if (!isCollected) VISIBLE else INVISIBLE
            isCollected = !isCollected
            //todo send broadcast
        }
    }
}