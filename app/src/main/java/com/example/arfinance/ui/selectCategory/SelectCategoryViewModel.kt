package com.example.arfinance.ui.selectCategory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.local.CategoryDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SelectCategoryViewModel @ViewModelInject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val categoriesFlow = searchQuery.flatMapLatest {
        categoryDao.getAllCategories(it)
    }
    val categories = categoriesFlow.asLiveData()

    private val selectCategoryEventChannel = Channel<SelectCategoryListEvent>()
    val selectCategoryEvent = selectCategoryEventChannel.receiveAsFlow()


    fun addNewCategoryClicked() = viewModelScope.launch {
        selectCategoryEventChannel.send(SelectCategoryListEvent.NavigateToAddCategoryScreen)
    }

    sealed class SelectCategoryListEvent {
        object NavigateToAddCategoryScreen : SelectCategoryListEvent()
    }
}