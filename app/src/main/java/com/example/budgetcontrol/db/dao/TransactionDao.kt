package com.example.budgetcontrol.db.dao

import androidx.room.*
import com.example.budgetcontrol.db.model.Transaction

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Query("select amount from `Transaction` where amount > 0")
    fun getAllIncomeAmounts(): List<Float>

    @Query("select amount from `Transaction` where amount < 0")
    fun getAllCostsAmounts(): List<Float>

    @Query("select amount from `transaction` where amount > 0 and date between :startDate and :endDate")
    fun getAllIncomeAmountsByDate(startDate: String, endDate: String): List<Float>

    @Query("select amount from `transaction` where amount < 0 and date between :startDate and :endDate")
    fun getAllCostsAmountsByDate(startDate: String, endDate: String): List<Float>

    @Query("select * from `Transaction` where amount > 0")
    fun getAllIncome(): List<Transaction>

    @Query("select * from `Transaction` where amount < 0")
    fun getAllCosts(): List<Transaction>

    @Query("select * from `transaction` where amount > 0 and date between :startDate and :endDate")
    fun getAllIncomeByDate(startDate: String, endDate: String): List<Transaction>

    @Query("select * from `transaction` where amount < 0 and date between :startDate and :endDate")
    fun getAllCostsByDate(startDate: String, endDate: String): List<Transaction>
}