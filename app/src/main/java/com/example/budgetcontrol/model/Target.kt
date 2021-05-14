package com.example.budgetcontrol.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Target(
    @PrimaryKey val id: Int,
    val description: String,
    val amount: Float,
    @ColumnInfo(name = "collected_amount") val collectedAmount: Float
){
    //todo move to fragment class if it can be used only there
    companion object{
        const val GAUSS_NUMBER_TARGET_ID = 0
        const val MAIN_TARGET_ID = 1
    }
}
