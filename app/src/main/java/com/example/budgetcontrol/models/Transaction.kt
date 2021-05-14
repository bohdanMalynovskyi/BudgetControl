package com.example.budgetcontrol.models

import com.example.budgetcontrol.enums.Record
import java.util.*

data class Transaction(var id: Int = 0, var amount: Float, var date: Date = Date(), var comment: String) {

    //TODO use usual class
    companion object {
        fun getCurrentTrntype(amount: Float): Record {
            return if (amount > 0) Record.INCOME_TRANSACTION else Record.COSTS_TRANSACTION
        }
    }
}
// TODO set id while fetching transaction from DB
