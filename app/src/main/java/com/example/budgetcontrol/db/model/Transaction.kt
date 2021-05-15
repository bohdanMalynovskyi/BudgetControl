package com.example.budgetcontrol.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val amount: Int,
    val date: String,
    val comment: String
) {
    constructor(amount: Int, comment: String) : this(
        0,
        amount,
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
        comment
    )
}
