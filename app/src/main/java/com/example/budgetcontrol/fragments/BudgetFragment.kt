package com.example.budgetcontrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity
import com.example.budgetcontrol.R
import com.example.budgetcontrol.enums.FragmentName
import kotlinx.android.synthetic.main.budget_fragment.*

class BudgetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.budget_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationButtons()
        setupIncomeCostsCounter()
    }

    private fun setupNavigationButtons() {
        targetImageView.setOnClickListener {
            (activity as MainActivity).navigateToFragment(FragmentName.TARGET)
        }
        fiftyFiftyImageView.setOnClickListener {
            (activity as MainActivity).navigateToFragment(FragmentName.FIFTY_FIFTY)
        }
        infoImageView.setOnClickListener {
            (activity as MainActivity).navigateToFragment(FragmentName.INFORMATION)
        }
    }

    private fun setupIncomeCostsCounter(){
        incomeCounter.setOnClickListener {
            (activity as MainActivity).navigateToFragment(FragmentName.INCOME)
        }
        costsCounter.setOnClickListener {
            (activity as MainActivity).navigateToFragment(FragmentName.COSTS)
        }
    }
}