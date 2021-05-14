package com.example.budgetcontrol.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Transaction(
    val amount: Float,
    val date: Date = Date(),
    val comment: String
){
    @PrimaryKey(autoGenerate = true) val id: Int = 0
}
