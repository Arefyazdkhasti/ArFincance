package com.example.arfinance.ui.selectCategory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.local.CategoryDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SelectCategoryViewModel @ViewModelInject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    val categories = categoryDao.getAllCategories().asLiveData()

    private val selectCategoryEventChannel = Channel<SelectCategoryListEvent>()
    val selectCategoryEvent = selectCategoryEventChannel.receiveAsFlow()


    fun addNewcategoryClicked() = viewModelScope.launch {
        selectCategoryEventChannel.send(SelectCategoryListEvent.NavigateToAddCategoryScreen)
    }

    sealed class SelectCategoryListEvent {
        object NavigateToAddCategoryScreen : SelectCategoryListEvent()
    }
}