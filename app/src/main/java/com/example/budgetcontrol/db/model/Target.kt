package com.example.budgetcontrol.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target")
data class Target(
    @PrimaryKey val id: Int,
    val description: String,
    val amount: Float,
    @ColumnInfo(name = "collected_amount") val collectedAmount: Int
){
    //todo move to fragment class if it can be used not only there
    companion object{
        const val GAUSS_NUMBER_TARGET_ID = 1
        const val MAIN_TARGET_ID = 2
        const val GAUSS_NUMBER_TARGET_DESCRIPTION = "GAUSS_NUMBER_TARGET_DESCRIPTION"
        const val GAUSS_NUMBER_TARGET_AMOUNT = 5050f
    }
}
