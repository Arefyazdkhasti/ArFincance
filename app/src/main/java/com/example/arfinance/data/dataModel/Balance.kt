package com.example.arfinance.data.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arfinance.util.enumerian.BalanceTime

@Entity(tableName = "balance_table")
data class Balance(
    @PrimaryKey(autoGenerate = false) var id:Int,
    var balanceTime: BalanceTime,
    var income: Long,
    var expense: Long
){
    fun getBalance():Long = income - expense
}
