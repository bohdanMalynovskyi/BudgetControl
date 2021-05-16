package com.example.budgetcontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity.Companion.BUDGET_COMPONENT
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.dialog.RecordDialog
import com.example.budgetcontrol.enum.BudgetComponent
import com.example.budgetcontrol.enum.Record
import com.example.budgetcontrol.view.TransactionView
import kotlinx.android.synthetic.main.income_costs_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class IncomeCostsFragment : Fragment() {

    private lateinit var budgetComponent: BudgetComponent

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.income_costs_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetComponent = arguments?.getSerializable(BUDGET_COMPONENT) as BudgetComponent
        fetchData()
        setupCounter()
    }

    private fun fetchData() {
        setCounterValue()
        fillTransactionContainer()
    }

    private fun setCounterValue() {
        GlobalScope.launch {
            val amount = abs(getTotalIncomeCostsAmount(budgetComponent))
            activity?.runOnUiThread {
                incomeCostsYellowCounter.text = getString(
                        R.string.income_costs_value_with_grivnas_placeholder,
                        when (budgetComponent) {
                            BudgetComponent.INCOME -> "+"
                            BudgetComponent.COSTS -> "-"
                        },
                        amount
                )
            }
        }
    }

    private fun fillTransactionContainer() {
        transactionsListLinearLayout.removeAllViews()

        val transactionDao =  BudgetControlDB.getInstance(requireContext()).transactionDao()
        GlobalScope.launch {
            val transactionList = when(budgetComponent){
                BudgetComponent.INCOME -> transactionDao.getAllIncome()
                BudgetComponent.COSTS -> transactionDao.getAllCosts()
            }
            activity?.runOnUiThread {
                transactionList.forEach { transaction ->
                    transactionsListLinearLayout.addView(TransactionView(requireContext(), transaction), 0)
                }
            }
        }
    }

    private fun setupCounter() {
        incomeCostsYellowCounter.setOnClickListener {
            val dialog = RecordDialog(
                    requireContext(),
                    when (budgetComponent) {
                        BudgetComponent.INCOME -> Record.INCOME_TRANSACTION
                        BudgetComponent.COSTS -> Record.COSTS_TRANSACTION
                    }
            )
            dialog.setOnDismissListener {
                //todo add the lest transaction
            }
            dialog.show()
        }
    }

    //TODO in what way to prevent code duplication??????
    private fun getTotalIncomeCostsAmount(budgetComponent: BudgetComponent): Float {
        var totalAmount = 0f
        val allAmounts: List<Float>?
        val transactionDao = BudgetControlDB.getInstance(requireContext()).transactionDao()
        allAmounts = when (budgetComponent) {
            BudgetComponent.INCOME -> transactionDao.getAllIncomeAmounts()
            BudgetComponent.COSTS -> transactionDao.getAllCostsAmounts()
        }
        allAmounts.forEach { value ->
            totalAmount += value
        }
        return totalAmount
    }
}