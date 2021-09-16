package com.example.arfinance.ui.addCategory

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.CategoryDao
import com.example.arfinance.ui.addEditTransaction.AddEditTransactionViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddCategoryViewModel @ViewModelInject constructor(
    private val categoryDao: CategoryDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Category>("category")
    private val categoryEventChannel = Channel<AddCategoryEvent>()
    val addCategoryEvent = categoryEventChannel.receiveAsFlow()

    var categoryTitle = state.get<String>("categoryTitle") ?: category?.categoryName ?: ""
        set(value) {
            field = value
            state.set("categoryTitle", value)
        }

    //todo add category type

    var categoryIcon = state.get<Int>("categoryIcon") ?: category?.categoryIcon ?: 26
        set(value) {
            field = value
            state.set("categoryIcon", value)
        }

    fun onSaveClick() {
        if (categoryTitle == "") {
            showInvalidInputMessage("category title is necessary")
            return
        } else {

            //TODO check if name existed!
            val newCategory =
                Category(categoryName = categoryTitle, categoryIcon = categoryIcon)
            createTransaction(newCategory)
        }
    }

    private fun createTransaction(category: Category) = viewModelScope.launch {
        categoryDao.insertCategory(category)
        showSuccessMessage("Category Added Successfully")
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        categoryEventChannel.send(AddCategoryEvent.ShowInvalidInputMessage(msg))
    }

    private fun showSuccessMessage(text: String) = viewModelScope.launch {
        categoryEventChannel.send(AddCategoryEvent.ShowSuccessMessage(text))
    }

    sealed class AddCategoryEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddCategoryEvent()
        data class ShowSuccessMessage(val msg: String) : AddCategoryEvent()
    }
}