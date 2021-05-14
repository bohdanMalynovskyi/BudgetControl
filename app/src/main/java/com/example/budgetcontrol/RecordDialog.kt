package com.example.budgetcontrol

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.budgetcontrol.enums.Record
import kotlinx.android.synthetic.main.record_dialog.view.*

class RecordDialog(context: Context, private val recordType: Record) : Dialog(context) {

    private lateinit var headerTextView: TextView
    private lateinit var amountEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var recordButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setup()
    }

    private fun setup() {
        headerTextView.text = when (recordType) {
            Record.INCOME_TRANSACTION -> context.getString(R.string.income)
            Record.COSTS_TRANSACTION -> context.getString(R.string.costs)
            Record.NEW_TARGET -> context.getString(R.string.target)
        }
        descriptionEditText.hint = when (recordType) {
            Record.INCOME_TRANSACTION -> context.getString(R.string.source)
            Record.COSTS_TRANSACTION -> context.getString(R.string.costs_edit_text_hint)
            Record.NEW_TARGET -> context.getString(R.string.target_description)
        }
//        recordButton.setOnClickListener {
//            when (recordType) {
//                //create unit funs and set here
//                Record.INCOME_TRANSACTION ->
//                    Record.COSTS_TRANSACTION
//                ->
//                    Record.NEW_TARGET
//                ->
//            }
//        }
    }

    private fun init() {
        val view = layoutInflater.inflate(R.layout.record_dialog, null)
        headerTextView = view.dialogHeaderTextView
        amountEditText = view.dialogAmountEditText
        descriptionEditText = view.dialogDescriptionEditText
        recordButton = view.dialogRecordButton
    }
}