package com.example.budgetcontrol.models

import com.example.budgetcontrol.enums.TransactionType
import java.util.*

data class Transaction(var id: Int = 0, var amount: Float, var date: Date = Date(), var comment: String) {

    //TODO use usual class
    companion object {
        fun getCurrentTrntype(amount: Float): TransactionType {
            return if (amount > 0) TransactionType.INCOME else TransactionType.COSTS
        }
    }
}
// TODO set id while fetching transaction from DB
