package com.example.budgetcontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity
import com.example.budgetcontrol.MainActivity.Companion.BUDGET_COMPONENT
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.db.model.Transaction
import com.example.budgetcontrol.dialog.RecordDialog
import com.example.budgetcontrol.enum.BudgetComponent
import com.example.budgetcontrol.view.TransactionView
import kotlinx.android.synthetic.main.income_costs_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class IncomeCostsFragment : Fragment() {

    private lateinit var budgetComponent: BudgetComponent
    private lateinit var updatingTransaction: Transaction

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
        (activity as MainActivity).setToolbarTitle(getString(when (budgetComponent) {
            BudgetComponent.INCOME -> R.string.income
            BudgetComponent.COSTS -> R.string.costs
        }))
        fetchData()
        setupCounter()
        incomeCostsDatePeriodView.setDatePickerOnDateSetListener(this::handleDateSet)
    }

    private fun handleDateSet(){
        incomeCostsDatePeriodView.apply {
            fetchData(getStartDate(), getEndDate())
        }
    }

    private fun fetchData(startDate: String? = null, endDate: String? = null) {
        setCounterValue(startDate, endDate)
        fillTransactionContainer(startDate, endDate)
    }

    private fun setCounterValue(startDate: String?, endDate: String?) {
        GlobalScope.launch {
            val amount = abs(getTotalIncomeCostsAmount(budgetComponent, startDate, endDate))
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

    private fun fillTransactionContainer(startDate: String?, endDate: String?) {
        transactionsListLinearLayout.removeAllViews()

        val transactionDao =  BudgetControlDB.getInstance(requireContext()).transactionDao()
        GlobalScope.launch {
            val transactionList = when(budgetComponent){
                BudgetComponent.INCOME ->{
                    if(startDate.isNullOrBlank() || endDate.isNullOrBlank())
                        transactionDao.getAllIncome()
                    else
                        transactionDao.getAllIncomeByDate(startDate, endDate)
                }
                BudgetComponent.COSTS -> {
                    if(startDate.isNullOrBlank() || endDate.isNullOrBlank())
                        transactionDao.getAllCosts()
                    else
                        transactionDao.getAllCostsByDate(startDate, endDate)
                }
            }
            activity?.runOnUiThread {
                transactionList.forEach { transaction ->
                    val transactionView = TransactionView(
                            requireContext(),
                            transaction,
                            this@IncomeCostsFragment::handleEditButtonClick
                    )
                    transactionsListLinearLayout.addView(transactionView, 0)
                }
            }
        }
    }

    private fun setupCounter() {
        incomeCostsYellowCounter.setOnClickListener {
            val dialog = RecordDialog(
                    requireContext(),
                    when (budgetComponent) {
                        BudgetComponent.INCOME -> context?.getString(R.string.income)
                        BudgetComponent.COSTS -> context?.getString(R.string.costs)
                    },
                    when (budgetComponent) {
                        BudgetComponent.INCOME -> context?.getString(R.string.source)
                        BudgetComponent.COSTS -> context?.getString(R.string.costs_edit_text_hint)
                    },
                    this::recordTransaction
            )
            dialog.setOnDismissListener {
                //todo add the lest transaction
            }
            dialog.show()
        }
    }

    private fun recordTransaction(amount: Float, description: String) {
        val transaction = Transaction(
                when (budgetComponent) {
                    BudgetComponent.INCOME -> amount
                    BudgetComponent.COSTS -> (-amount)
                },
                description
        )

        GlobalScope.launch {
            BudgetControlDB.getInstance(requireContext())
                    .transactionDao()
                    .insert(transaction)
            activity?.runOnUiThread {
                fetchData()
            }
        }
    }

    //TODO in what way to prevent code duplication??????
    private fun getTotalIncomeCostsAmount(budgetComponent: BudgetComponent, startDate: String?, endDate: String?): Float {
        var totalAmount = 0f
        val transactionDao = BudgetControlDB.getInstance(requireContext()).transactionDao()
        val allAmounts = when (budgetComponent) {
            BudgetComponent.INCOME -> {
                if (startDate.isNullOrBlank() || endDate.isNullOrBlank())
                    transactionDao.getAllIncomeAmounts()
                else
                    transactionDao.getAllIncomeAmountsByDate(startDate, endDate)
            }
            BudgetComponent.COSTS -> {
                if (startDate.isNullOrBlank() || endDate.isNullOrBlank())
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

    private fun handleEditButtonClick(transaction: Transaction) {
        updatingTransaction = transaction

        val dialog = RecordDialog(
                requireContext(),
                when (budgetComponent) {
                    BudgetComponent.INCOME -> context?.getString(R.string.income)
                    BudgetComponent.COSTS -> context?.getString(R.string.costs)
                },
                when (budgetComponent) {
                    BudgetComponent.INCOME -> context?.getString(R.string.source)
                    BudgetComponent.COSTS -> context?.getString(R.string.costs_edit_text_hint)
                },
                this::updateTransaction
        )
        dialog.apply {
            setOnDismissListener {
                fetchData()
            }
            show()
        }
    }

    private fun updateTransaction(amount: Float, description: String){
        val newTransaction = updatingTransaction.copy(
                amount = when (budgetComponent) {
                    BudgetComponent.INCOME -> amount
                    BudgetComponent.COSTS -> (-amount)
                },
                comment = description
        )

        GlobalScope.launch {
            BudgetControlDB.getInstance(requireContext())
                    .transactionDao()
                    .update(newTransaction)
        }
    }
}