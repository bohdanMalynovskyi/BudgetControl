package com.example.budgetcontrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.R
import com.example.budgetcontrol.views.GaussNumberView
import kotlinx.android.synthetic.main.fifty_fifty_fragment.*

class FiftyFiftyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fifty_fifty_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        fillGaussNumberLayout()
    }

    private fun fillGaussNumberLayout() {
        gaussNumbersFlexBoxLayout.removeAllViews()
        for (number in 1..100) {
            val gaussNumber = GaussNumberView(context)
            gaussNumber.apply {
                setGaussNumberText(number.toString())
                setOnClickListener {

                }
            }
            gaussNumbersFlexBoxLayout.addView(gaussNumber)
        }
    }
}