package com.example.budgetcontrol.db.dao

import androidx.room.*
import com.example.budgetcontrol.model.Transaction
import java.util.*

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Query("select amount from `Transaction` where amount > 0")
    fun getAllIncomeAmounts(): Transaction

    @Query("select amount from `Transaction` where amount < 0")
    fun getAllCostsAmounts(): Transaction

    @Query("select * from `transaction`")
    fun getAll(): List<Transaction>

    @Query("select * from `transaction` where date > :startDate and date < :endDate")
    fun getAllByDate(startDate: Date, endDate: Date): List<Transaction>
}