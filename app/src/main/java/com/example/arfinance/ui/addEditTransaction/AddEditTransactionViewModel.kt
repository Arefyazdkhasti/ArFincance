package com.example.arfinance.ui.addEditTransaction

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.TransactionDao
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class AddEditTransactionViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val transaction = state.get<Transactions>("transaction")

    var transactionTitle = state.get<String>("transactionTitle") ?: transaction?.title ?: ""
        set(value) {
            field = value
            state.set("transactionTitle", value)
        }

    var transactionType = state.get<TransactionType>("transactionType") ?: transaction?.type
    ?: TransactionType.Expense
        set(value) {
            field = value
            state.set("transactionType", value)
        }

    var transactionAmount = state.get<Long>("transactionAmount") ?: transaction?.amount ?: 0
        set(value) {
            field = value
            state.set("transactionAmount", value)
        }

    var transactionCategory =
        state.get<Int>("transactionCategoryID") ?: transaction?.categoryId ?: 1
        set(value) {
            field = value
            state.set("transactionCategoryID", value)
        }

    var transactionPaymentType =
        state.get<PaymentType>("transactionPaymentType") ?: transaction?.paymentType
        ?: PaymentType.CreditCard
        set(value) {
            field = value
            state.set("transactionPaymentType", value)
        }

    var transactionNote =
        state.get<String>("transactionNote") ?: transaction?.note ?: ""
        set(value) {
            field = value
            state.set("transactionNote", value)
        }

    var transactionDate =
        state.get<String>("transactionDate") ?: transaction?.date ?: ""
        set(value) {
            field = value
            state.set("transactionDate", value)
        }


    fun onSaveClick() {
        val newTransaction = Transactions(
            0,
            transactionTitle,
            transactionAmount,
            transactionType,
            transactionCategory,
            transactionPaymentType,
            transactionNote,
            transactionDate
        )
        createTransaction(newTransaction)
    }

    private fun createTransaction(transaction: Transactions) = viewModelScope.launch {
        transactionDao.insertTransaction(transaction)
    }

}