package com.example.budgetcontrol.db.dao

import androidx.room.*
import com.example.budgetcontrol.db.model.Transaction
import java.util.*

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Query("select amount from `Transaction` where amount > 0")
    fun getAllIncomeAmounts(): List<Int>

    @Query("select amount from `Transaction` where amount < 0")
    fun getAllCostsAmounts(): List<Int>

    @Query("select * from `transaction`")
    fun getAll(): List<Transaction>

    @Query("select * from `transaction` where date between :startDate and :endDate")
    fun getAllByDate(startDate: String, endDate: String): List<Transaction>
}