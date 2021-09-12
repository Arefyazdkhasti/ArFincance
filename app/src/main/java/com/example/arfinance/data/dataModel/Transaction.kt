package com.example.arfinance.data.dataModel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arfinance.util.enumerian.PaymentType
import kotlinx.parcelize.Parcelize

@Entity(tableName = "transaction_table")
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    private  val id: Int = 0,
    private val title:String,
    private val type: String,
    private val categoryID: Int,
    private val paymentType: String,
    private val note:String,
    private val date : String
):Parcelable