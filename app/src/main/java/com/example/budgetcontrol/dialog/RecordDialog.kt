package com.example.budgetcontrol.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.db.model.Target
import com.example.budgetcontrol.db.model.Transaction
import com.example.budgetcontrol.enum.BudgetComponent
import com.example.budgetcontrol.enum.Record
import kotlinx.android.synthetic.main.record_dialog.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecordDialog(
        context: Context,
        private val recordType: Record
) : Dialog(context) {

    private lateinit var header: TextView
    private lateinit var amountEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var recordButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setup()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

    private fun setup() {
        header.text = when (recordType) {
            Record.INCOME_TRANSACTION -> context.getString(R.string.income)
            Record.COSTS_TRANSACTION -> context.getString(R.string.costs)
            Record.NEW_TARGET -> context.getString(R.string.target)
        }
        descriptionEditText.hint = when (recordType) {
            Record.INCOME_TRANSACTION -> context.getString(R.string.source)
            Record.COSTS_TRANSACTION -> context.getString(R.string.costs_edit_text_hint)
            Record.NEW_TARGET -> context.getString(R.string.target_description)
        }
        recordButton.setOnClickListener {
            when (recordType) {
                Record.INCOME_TRANSACTION -> recordTransaction(BudgetComponent.INCOME)
                Record.COSTS_TRANSACTION -> recordTransaction(BudgetComponent.COSTS)
                Record.NEW_TARGET -> recordTarget()
            }
        }
    }

    private fun recordTarget() {
        val target = Target(
                Target.MAIN_TARGET_ID,
                getDescription(),
                getAmount(),
                0
        )

        GlobalScope.launch {
            BudgetControlDB.getInstance(context)
                    .targetDao()
                    .insert(target)
            dismiss()
        }
    }

    private fun recordTransaction(budgetComponent: BudgetComponent) {
        val amount = when (budgetComponent) {
            BudgetComponent.INCOME -> getAmount()
            BudgetComponent.COSTS -> (-getAmount())
        }
        val description = getDescription()
        val transaction = Transaction(amount, description)

        GlobalScope.launch {
            BudgetControlDB.getInstance(context)
                    .transactionDao()
                    .insert(transaction)
            dismiss()
        }
    }

    private fun getAmount(): Float {
        return amountEditText.text.toString().toFloat()
    }

    private fun getDescription(): String {
        return descriptionEditText.text.toString()
    }

    private fun init() {
        val dialogView = layoutInflater.inflate(R.layout.record_dialog, null)
        dialogView.apply {
            header = dialogHeaderTextView
            amountEditText = dialogAmountEditText
            descriptionEditText = dialogDescriptionEditText
            recordButton = dialogRecordButton
        }
        setContentView(dialogView)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}