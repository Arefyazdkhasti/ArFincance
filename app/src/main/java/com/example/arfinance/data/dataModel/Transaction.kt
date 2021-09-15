package com.example.arfinance.data.dataModel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType
import kotlinx.parcelize.Parcelize

@Entity(tableName = "transaction_table")
@Parcelize
data class Transactions(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var amount: Long,
    var type: TransactionType,
    var categoryName: String,
    var categoryIcon: Int,
    var paymentType: PaymentType,
    var note: String,
    var date: String
) : Parcelable