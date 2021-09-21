package com.example.arfinance.util

import androidx.room.TypeConverter
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType

class Converter {
    @TypeConverter
    fun toTransactionType(value: String) = enumValueOf<TransactionType>(value)

    @TypeConverter
    fun fromTransactionType(value: TransactionType) = value.name

    @TypeConverter
    fun toPaymentType(value: String) = enumValueOf<PaymentType>(value)

    @TypeConverter
    fun fromPaymentType(value: PaymentType) = value.name

    @TypeConverter
    fun toString(str: String?): String {

        val sampleString = "Unknown"

        return str ?: sampleString

    }

}