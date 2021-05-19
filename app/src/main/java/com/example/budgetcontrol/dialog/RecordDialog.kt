package com.example.budgetcontrol.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.record_dialog.view.*

class RecordDialog(
        context: Context,
        private val headerText: String?,
        private val descriptionEditTextHint: String?,
        private val recordButtonHandler: (amount: Float, description: String) -> Unit
) : Dialog(context) {
//    todo set text with constructor???

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
        header.text = headerText
        descriptionEditText.hint = descriptionEditTextHint
        recordButton.setOnClickListener {
            if (!amountEditText.text.isNullOrBlank() && !descriptionEditText.text.isNullOrBlank()) {
                recordButtonHandler(getAmount(), getDescription())
                dismiss()
            }
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
            header = recordDialogHeaderTextView
            amountEditText = dialogAmountEditText
            descriptionEditText = dialogDescriptionEditText
            recordButton = dialogRecordButton
        }
        setContentView(dialogView)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}