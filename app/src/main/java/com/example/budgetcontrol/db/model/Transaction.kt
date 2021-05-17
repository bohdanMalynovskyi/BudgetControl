package com.example.budgetcontrol.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetcontrol.view.DatePeriodView.Companion.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val amount: Float,
    val date: String,
    val comment: String
) {
    constructor(amount: Float, comment: String) : this(
        0,
        amount,
        SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date()),
        comment
    )
}
