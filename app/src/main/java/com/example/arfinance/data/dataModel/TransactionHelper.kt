package com.example.arfinance.data.dataModel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType
import kotlinx.parcelize.Parcelize

data class TransactionsHelper(
    var amount: Long,
    var categoryName: String
)