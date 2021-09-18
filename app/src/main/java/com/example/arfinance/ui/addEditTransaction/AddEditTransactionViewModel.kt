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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTransactionViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val transaction = state.get<Transactions>("transaction")

    private val transactionEventChannel = Channel<AddEditTransactionEvent>()
    val addEditTransactionEvent = transactionEventChannel.receiveAsFlow()

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

    var transactionCategoryName =
        state.get<String>("transactionCategoryID") ?: transaction?.categoryName ?: ""
        set(value) {
            field = value
            state.set("transactionCategoryID", value)
        }

    var transactionCategoryIcon =
        state.get<String>("transactionCategoryIcon") ?: transaction?.categoryIcon ?: ""
        set(value) {
            field = value
            state.set("transactionCategoryIcon", value)
        }

    var transactionPaymentType =
        state.get<PaymentType>("transactionPaymentType") ?: transaction?.paymentType
        ?: PaymentType.Unknown
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
        if (transactionTitle.isEmpty() || transactionAmount.toInt() == 0 || transactionCategoryName == "" || transactionDate.isEmpty()) {
            showInvalidInputMessage("Input all data validly")
            return
        }
        if(transaction != null){
            val updatedTask = transaction.copy(title = transactionTitle,
                amount = transactionAmount,
                type = transactionType,
                categoryName =  transactionCategoryName,
                categoryIcon = transactionCategoryIcon,
                paymentType = transactionPaymentType,
                note = transactionNote,
                date = transactionDate)
            updateTransaction(updatedTask)
        }else {
            val newTransaction = Transactions(
                title = transactionTitle,
                amount = transactionAmount,
                type = transactionType,
                categoryName =  transactionCategoryName,
                categoryIcon = transactionCategoryIcon,
                paymentType = transactionPaymentType,
                note = transactionNote,
                date = transactionDate
            )
            createTransaction(newTransaction)
        }
    }

    private fun updateTransaction(transaction: Transactions) = viewModelScope.launch{
        transactionDao.updateTransaction(transaction)
        showSuccessMessage("Transaction Updated Successfully")
    }

    private fun createTransaction(transaction: Transactions) = viewModelScope.launch {
        transactionDao.insertTransaction(transaction)
        showSuccessMessage("Transaction Added Successfully")
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        transactionEventChannel.send(AddEditTransactionEvent.ShowInvalidInputMessage(msg))
    }
    private fun showSuccessMessage(text: String) = viewModelScope.launch {
        transactionEventChannel.send(AddEditTransactionEvent.ShowSuccessMessage(text))
    }

    fun onSelectCategoryClick() = viewModelScope.launch {
        transactionEventChannel.send(AddEditTransactionEvent.NavigateToSelectCategory)
    }

    sealed class AddEditTransactionEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTransactionEvent()
        data class ShowSuccessMessage(val msg:String): AddEditTransactionEvent()
        object NavigateToSelectCategory: AddEditTransactionEvent()
    }
}