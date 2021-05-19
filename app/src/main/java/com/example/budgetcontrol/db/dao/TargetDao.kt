package com.example.budgetcontrol.db.dao

import androidx.room.*
import com.example.budgetcontrol.db.model.Target

@Dao
interface TargetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(target: Target)

    @Update
    fun update(target: Target)

    @Query("delete from target where id = :id")
    fun deleteById(id: Int)

    @Query("select amount from target where id = :id")
    fun getAmount(id: Int): Float

    @Query("select collected_amount from target where id = :id")
    fun getCollectedAmount(id: Int): Float

    @Query("update target set collected_amount = :amount where id = :id")
    fun updateCollectedAmount(id: Int, amount: Float)

    @Query("select exists(select * from target where id = :id)")
    fun isAdded(id: Int): Boolean
}