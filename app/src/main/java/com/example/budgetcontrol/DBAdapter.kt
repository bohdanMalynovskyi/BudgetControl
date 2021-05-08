package com.example.budgetcontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

        const val CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                ID + "integer primary key autoincrement," +
                AMOUNT + "decimal," +
                DATE + "date" +
                COMMENT + "text" + ");"

        const val CREATE_GAUSS_NUMBERS_TABLE = "CREATE TABLE " + TABLE_GAUSS_NUMBERS + "(" +
                ID + "integer primary key autoincrement," +
                VALUE + "integer," +
                IS_COLLECTED + "integer default 0" + ");"

        const val CREATE_TARGETS_TABLE = "CREATE TABLE " + TABLE_TARGETS + "(" +
                ID + "integer primary key autoincrement," +
                TARGET_DESCRIPTION + "text," +
                TARGET_AMOUNT + "decimal," +
                COLLECTED_AMOUNT + "decimal" + ");"
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

    fun addTransaction(amount: Float, date: Date, comment: String) {
        openDB()
//        val query = "INSERT INTO " + TABLE_TRANSACTIONS + " VALUES (" +
//                amount + ", " +
//                date + ", " +
//                comment + ");"
//        val cursor = db.rawQuery(query, null)
//        cursor.close()

        val values = ContentValues()
        values.put(AMOUNT, amount)
        //values.put(DATE, date)
        values.put(COMMENT, comment)
    }

    fun addNewTarget(description: String, targetAmount: Float) {
        openDB()

        val values = ContentValues()
        values.put(TARGET_DESCRIPTION, description)
        values.put(TARGET_AMOUNT, targetAmount)
        values.put(COLLECTED_AMOUNT, 0)

        //val targetCursor = getNot5050Target()
//        if(targetCursor.count == 0){
//            db.insert(TABLE_TARGETS, null, values)
//        } else {
            //db.update(TABLE_TARGETS, values, )
//        }
    }

//    fun getNot5050Target(): Cursor {
//
//    }

    fun makeContributionForTarget(amount: Float) {
        openDB()

        val values = ContentValues()
        values.put(COLLECTED_AMOUNT, amount)
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