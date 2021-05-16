package com.example.budgetcontrol.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgetcontrol.db.dao.GaussNumberDao
import com.example.budgetcontrol.db.dao.TargetDao
import com.example.budgetcontrol.db.dao.TransactionDao
import com.example.budgetcontrol.db.model.GaussNumber
import com.example.budgetcontrol.db.model.Target
import com.example.budgetcontrol.db.model.Transaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Transaction::class, Target::class, GaussNumber::class], version = 1)
abstract class BudgetControlDB : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun targetDao(): TargetDao
    abstract fun gaussNumberDao(): GaussNumberDao

    companion object {
        private val DATABASE = "BudgetControl"

        private var instance: BudgetControlDB? = null

        @Synchronized
        fun getInstance(context: Context): BudgetControlDB {
            if (instance == null) {
                instance = Room.databaseBuilder(context, BudgetControlDB::class.java, DATABASE)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: BudgetControlDB) {
            val gaussNumbersList = ArrayList<GaussNumber>()
            for (value in 1..100) {
                gaussNumbersList.add(GaussNumber(value.toFloat(), false))
            }
            val target = Target(
                Target.GAUSS_NUMBER_TARGET_ID,
                Target.GAUSS_NUMBER_TARGET_DESCRIPTION,
                Target.GAUSS_NUMBER_TARGET_AMOUNT,
                0
            )
            //todo read about coroutines. decide what to use here when have time
            GlobalScope.launch {
                db.gaussNumberDao().insertAll(gaussNumbersList)
                db.targetDao().insert(target)
            }
        }
    }
}