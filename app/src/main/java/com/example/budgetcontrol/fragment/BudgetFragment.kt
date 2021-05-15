package com.example.budgetcontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.enum.BudgetComponent
import com.example.budgetcontrol.enum.Screen
import kotlinx.android.synthetic.main.budget_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        fetchData()

        setupNavigationButtonsOnClickListeners()
    }

    private fun setupNavigationButtonsOnClickListeners() {
        setNavigationOnClickListener(targetImageView, Screen.TARGET)
        setNavigationOnClickListener(gaussNumbersImageView, Screen.GAUSS_NUMBERS)
        setNavigationOnClickListener(infoImageView, Screen.INFORMATION)
        setNavigationOnClickListener(incomeCounter, Screen.INCOME)
        setNavigationOnClickListener(costsCounter, Screen.COSTS)
    }

    private fun setNavigationOnClickListener(view: View, screen: Screen) {
        view.setOnClickListener {
            (activity as MainActivity).navigateToFragment(screen)
        }
    }

    private fun fetchData() {
        val income = getTotalIncomeCostsAmount(BudgetComponent.INCOME)
        val costs = getTotalIncomeCostsAmount(BudgetComponent.COSTS)
        val balance = income - costs

        incomeCounter.setValue(income)
        costsCounter.setValue(costs)
        balanceValue.text =
            if (balance < 0) getString(R.string.income_costs_value_placeholder, "-", balance)
            else balance.toString()
    }

    private fun getTotalIncomeCostsAmount(budgetComponent: BudgetComponent): Int {
        var totalAmount = 0
        var allAmounts: List<Int>? = null
        val transactionDao = BudgetControlDB.getInstance(requireContext()).transactionDao()
        GlobalScope.launch {
            allAmounts = when (budgetComponent) {
                BudgetComponent.INCOME -> transactionDao.getAllIncomeAmounts()
                BudgetComponent.COSTS -> transactionDao.getAllCostsAmounts()
            }
        }
        allAmounts?.forEach { value ->
            totalAmount += value
        }

        return totalAmount
    }
}