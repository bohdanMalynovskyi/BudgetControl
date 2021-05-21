package com.example.budgetcontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.*
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.enum.BudgetComponent
import com.example.budgetcontrol.enum.FragmentType
import kotlinx.android.synthetic.main.budget_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

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
        (activity as MainActivity).setToolbarTitle(getString(R.string.budget))
        fetchData()

        setupNavigationButtonsOnClickListeners()
        budgetFragmentDatePeriodView.setDatePickerOnDateSetListener(this::handleDateSet)
    }

    private fun handleDateSet(){
        budgetFragmentDatePeriodView.apply {
            fetchData(getStartDate(), getEndDate())
        }
    }

    private fun setupNavigationButtonsOnClickListeners() {
        setNavigationOnClickListener(targetImageView, FragmentType.TARGET)
        setNavigationOnClickListener(gaussNumbersImageView, FragmentType.GAUSS_NUMBERS)
        setNavigationOnClickListener(infoImageView, FragmentType.INFORMATION)
        setNavigationOnClickListener(incomeCounter, FragmentType.INCOME)
        setNavigationOnClickListener(costsCounter, FragmentType.COSTS)
    }

    private fun setNavigationOnClickListener(view: View, fragmentType: FragmentType) {
        view.setOnClickListener {
            (activity as MainActivity).navigateToFragment(fragmentType)
        }
    }

    private fun fetchData(startDate: String? = null, endDate: String? = null) {
        GlobalScope.launch {
            val income = getTotalIncomeCostsAmount(BudgetComponent.INCOME, startDate, endDate)
            val costs = abs(getTotalIncomeCostsAmount(BudgetComponent.COSTS, startDate, endDate))
            val balance = income - costs

            activity?.runOnUiThread {
                incomeCounter.setValue(income)
                costsCounter.setValue(costs)
                balanceValue.text = getString(
                        R.string.income_costs_value_placeholder,
                        if (balance < 0) "-" else "",
                        abs(balance)
                )
            }
        }
    }

    private fun getTotalIncomeCostsAmount(budgetComponent: BudgetComponent, startDate: String?, endDate: String?): Float {
        var totalAmount = 0f
        val transactionDao = BudgetControlDB.getInstance(requireContext()).transactionDao()
        val allAmounts = when (budgetComponent) {
            BudgetComponent.INCOME ->{
                if(startDate.isNullOrBlank() || endDate.isNullOrBlank())
                    transactionDao.getAllIncomeAmounts()
                else
                    transactionDao.getAllIncomeAmountsByDate(startDate, endDate)
            }
            BudgetComponent.COSTS -> {
                if(startDate.isNullOrBlank() || endDate.isNullOrBlank())
                    transactionDao.getAllCostsAmounts()
                else
                    transactionDao.getAllCostsAmountsByDate(startDate, endDate)
            }
        }
        allAmounts.forEach { value ->
            totalAmount += value
        }
        return totalAmount
    }
}