package com.example.budgetcontrol

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.budgetcontrol.enum.Record
import com.example.budgetcontrol.db.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

class DBAdapter private constructor(context: Context) {

    companion object {
        const val DB_NAME = "budget_db"

        const val TABLE_TRANSACTIONS = "TABLE_TRANSACTIONS"
        const val TABLE_GAUSS_NUMBERS = "TABLE_GAUSS_NUMBERS"
        const val TABLE_TARGETS = "TABLE_GOALS"

        const val ID = "ID"
        const val AMOUNT = "AMOUNT"
        const val DATE = "DATE"
        const val COMMENT = "COMMENT"
        const val VALUE = "VALUE"
        const val IS_COLLECTED = "IS_COLLECTED"
        const val TARGET_DESCRIPTION = "GOAL_DESCRIPTION"
        const val TARGET_AMOUNT = "SUM"
        const val COLLECTED_AMOUNT = "IS_COLLECTED"

        const val GAUSS_NUMBERS_TARGET_DESCRIPTION = "GAUSS_NUMBERS_TARGET_DESCRIPTION"

        const val CREATE_TRANSACTIONS_TABLE = "CREATE TABLE $TABLE_TRANSACTIONS(" +
                "$ID integer primary key autoincrement," +
                "$AMOUNT decimal," +
                "$DATE date" +
                "$COMMENT text" + ");"

        const val CREATE_TARGETS_TABLE = "CREATE TABLE $TABLE_TARGETS (" +
                "$ID integer primary key autoincrement," +
                "$TARGET_DESCRIPTION text," +
                "$TARGET_AMOUNT decimal," +
                "$COLLECTED_AMOUNT decimal" + ");"

        const val CREATE_GAUSS_NUMBERS_TABLE = "CREATE TABLE $TABLE_GAUSS_NUMBERS (" +
                "$ID integer primary key autoincrement," +
                "$VALUE +integer," +
                "$IS_COLLECTED integer default 0);"
    }

    // TODO read more why singleton
    private var instance: DBAdapter? = null
    private lateinit var db: SQLiteDatabase
    private var dbHelper: SQLiteOpenHelper = DBHelper(context)

    fun getInstance(context: Context): DBAdapter? {
        if (instance == null) {
            instance = DBAdapter(context)
        }
        return instance
    }

    private fun openDB() {
        db = dbHelper.writableDatabase
    }

    //TODO add gauss number target row after target table creation

//    fun addTransaction(transaction: Transaction) {
//        //TODO amount must be with sign
//        openDB()
//
//        val values = ContentValues()
//        values.put(AMOUNT, transaction.amount)
//        values.put(DATE, convertDateToString(transaction.date))
//        values.put(COMMENT, transaction.comment)
//        db.insert(TABLE_TRANSACTIONS, null, values)
//
//        db.close()
//    }

    fun addNewTarget(description: String, targetAmount: Float) {
        openDB()

        val values = ContentValues()
        values.put(TARGET_DESCRIPTION, description)
        values.put(TARGET_AMOUNT, targetAmount)
        values.put(COLLECTED_AMOUNT, 0)

        val isTargetNotSet = getTargetsWithout5050Amount() == 0
        if (isTargetNotSet) {
            db.insert(TABLE_TARGETS, null, values)
        } else {
            db.update(TABLE_TARGETS, values, "$TARGET_DESCRIPTION !=", arrayOf(GAUSS_NUMBERS_TARGET_DESCRIPTION))
        }

        db.close()
    }

    //TODO check if single cell update works
    fun makeContributionForTarget(contributionAmount: Float) {
        openDB()

        val values = ContentValues()
        values.put(COLLECTED_AMOUNT, getTargetCollectedAmount(false) + contributionAmount)
        db.update(TABLE_TARGETS, values, "$TARGET_DESCRIPTION !=", arrayOf(GAUSS_NUMBERS_TARGET_DESCRIPTION))

        db.close()
    }

    fun updateTransaction(transaction: Transaction) {
        //TODO amount must be with sign
        openDB()

        val values = ContentValues()
        values.put(AMOUNT, transaction.amount)
//        values.put(DATE, convertDateToString(transaction.date))
        values.put(COMMENT, transaction.comment)
        db.update(TABLE_TRANSACTIONS, values, "$ID =", arrayOf(transaction.id.toString()))

        db.close()
    }

    fun getTargetAmount(): Float {
        openDB()

        val cursor = db.query(
                TABLE_TARGETS,
                arrayOf(TARGET_AMOUNT),
                "$TARGET_DESCRIPTION !=",
                arrayOf(GAUSS_NUMBERS_TARGET_DESCRIPTION),
                null,
                null,
                null
        )
        //TODO check and finish
        val amount = cursor.getFloat(0)

        cursor.close()
        db.close()
        return amount
    }

    fun getTargetCollectedAmount(is5050Target: Boolean): Float {
        openDB()

        val cursor = db.query(
                TABLE_TARGETS,
                arrayOf(COLLECTED_AMOUNT),
                if (is5050Target) "$TARGET_DESCRIPTION =" else "$TARGET_DESCRIPTION !=",
                arrayOf(GAUSS_NUMBERS_TARGET_DESCRIPTION),
                null,
                null,
                null
        )
        //TODO check and finish
        val amount = cursor.getFloat(0)

        cursor.close()
        db.close()
        return amount
    }

//    fun getTotalIncomeOrCosts(requestedRecord: Record): Float {
////        openDB()
////
////        // TODO replace with getTransaction method
////        val cursor = db.query(
////                TABLE_TRANSACTIONS,
////                arrayOf(AMOUNT),
////                null,
////                null,
////                null,
////                null,
////                null
////        )
////        var totalIncome = 0F
////
////        while (cursor.moveToNext()) {
////            val transaction = cursor.getFloat(0)
////            val currentTransactionType = if (transaction > 0) TransactionType.INCOME else TransactionType.COSTS
////            if (requestedTransactionType == TransactionType.INCOME && currentTransactionType == TransactionType.INCOME) {
////                totalIncome += transaction
////            } else if (requestedTransactionType == TransactionType.COSTS && currentTransactionType == TransactionType.COSTS) {
////                totalIncome += abs(transaction)
////            }
////        }
////
////        cursor.close()
////        db.close()
//
//        var totalAmount = 0F
//        val transactionList = getAllTransactions(requestedRecord)
//        transactionList.forEach { transaction ->
//            totalAmount += transaction.amount
//        }
//
//        return totalAmount
//    }

//    fun getAllTransactions(requestedRecord: Record): List<Transaction> {
//        openDB()
//
//        val cursor = db.query(
//                TABLE_TRANSACTIONS,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        )
//        val transactionList = ArrayList<Transaction>()
//        val idColumnIndex = cursor.getColumnIndex(ID)
//        val amountColumnIndex = cursor.getColumnIndex(AMOUNT)
//        val dateColumnIndex = cursor.getColumnIndex(DATE)
//        val commentColumnIndex = cursor.getColumnIndex(COMMENT)
//
//        while (cursor.moveToNext()) {
//            val id = cursor.getInt(idColumnIndex)
//            val amount = cursor.getFloat(amountColumnIndex)
//            val date = convertStringToDate(cursor.getString(dateColumnIndex))
//            val comment = cursor.getString(commentColumnIndex)
//            val transaction = Transaction(id, amount, date, comment)
//
//            //val currentTransactionType = if (transaction.amount > 0) TransactionType.INCOME else TransactionType.COSTS
//            val currentTransactionType = Transaction.getCurrentTrntype(transaction.amount)
//            if (requestedRecord == Record.INCOME_TRANSACTION && currentTransactionType == Record.INCOME_TRANSACTION) {
//                transactionList.add(transaction)
//            } else if (requestedRecord == Record.COSTS_TRANSACTION && currentTransactionType == Record.COSTS_TRANSACTION) {
//                transactionList.add(transaction)
//            }
//        }
//
//        cursor.close()
//        db.close()
//        return transactionList
//    }

    //fun getTransactionByDate()

    private fun convertDateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun convertStringToDate(date: String): Date {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.parse(date)
    }

    private fun getTargetsWithout5050Amount(): Int {
        openDB()

        val cursor = db.query(
                TABLE_TARGETS,
                null,
                "$TARGET_DESCRIPTION =",
                arrayOf(GAUSS_NUMBERS_TARGET_DESCRIPTION),
                null,
                null,
                null
        )
        val amount = cursor.count

        cursor.close()
        db.close()
        return amount
    }

    class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_TRANSACTIONS_TABLE)
            db?.execSQL(CREATE_GAUSS_NUMBERS_TABLE)
            db?.execSQL(CREATE_TARGETS_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            TODO("Not yet implemented")
        }

    }
}