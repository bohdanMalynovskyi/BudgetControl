package com.example.budgetcontrol.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetcontrol.db.model.GaussNumber

@Dao
interface GaussNumberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gaussNumbers: List<GaussNumber>)

    @Query("update gauss_numbers set is_collected = :isCollected where id = :id")
    fun updateStatus(id: Int, isCollected: Int)

    @Query("select is_collected from gauss_numbers")
    fun getStatuses(): List<Int>
}