package com.example.budgetcontrol.db.dao

import androidx.room.*
import com.example.budgetcontrol.db.model.Target

@Dao
interface TargetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(target: Target)

    @Update
    fun update(target: Target)

    @Query("select amount from target where id = :id")
    fun getTargetAmount(id: Int): Int

    @Query("select collected_amount from target where id = :id")
    fun getTargetCollectedAmount(id: Int): Int

    @Query("update target set collected_amount = :amount where id = :id")
    fun updateTargetCollectedAmount(id: Int, amount: Int)
}