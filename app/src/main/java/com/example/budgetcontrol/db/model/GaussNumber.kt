package com.example.budgetcontrol.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gauss_numbers")
data class GaussNumber(
        val value: Int,
        @ColumnInfo(name = "is_collected") val isCollected: Boolean,
        @PrimaryKey(autoGenerate = true) val id: Int = 0
)
