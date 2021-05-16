package com.example.budgetcontrol.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gauss_numbers")
data class GaussNumber(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val value: Float,
    @ColumnInfo(name = "is_collected") val isCollected: Boolean
){
    constructor(value: Float, isCollected: Boolean) : this(0, value, isCollected)
}
