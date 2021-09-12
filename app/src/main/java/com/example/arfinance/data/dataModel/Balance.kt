package com.example.arfinance.data.dataModel

import com.example.arfinance.util.enumerian.BalanceTime


data class Balance(
    private val balanceTime: BalanceTime,
    private val income: Long,
    private val expense: Long
){
    fun getBalance():Long = income - expense
}
