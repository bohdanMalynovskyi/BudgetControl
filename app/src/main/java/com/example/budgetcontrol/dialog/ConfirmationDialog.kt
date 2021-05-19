package com.example.budgetcontrol.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
import com.example.budgetcontrol.R
import kotlinx.android.synthetic.main.confirmation_dialog.view.*

class ConfirmationDialog(
        context: Context,
        confirmationText: String
) : Dialog(context) {

    var confirmButton: TextView
    var confirmationTextView: TextView
    var closeButton: ImageView

    init {
        val dialogView = layoutInflater.inflate(R.layout.confirmation_dialog, null)
        dialogView.apply {
            setContentView(this)
            confirmButton = dialogConfirmButton
            confirmationTextView = confirmationDialogTextView
            closeButton = confirmationDialogCloseButton
        }
        confirmationTextView.text = confirmationText
        closeButton.setOnClickListener {
            dismiss()
        }

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}