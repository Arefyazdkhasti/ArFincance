package com.example.arfinance.ui.transactionList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.CategoryDao
import com.example.arfinance.data.local.TransactionDao
import com.example.arfinance.util.enumerian.CategoryType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionListViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    val transaction = transactionDao.getAllTransactions().asLiveData()
    val categoryDbSize = categoryDao.getCategoryCount().asLiveData()

    private val transactionsEventChannel = Channel<TransactionListEvent>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()

    fun addNewTransactionClicked() = viewModelScope.launch {
        transactionsEventChannel.send(TransactionListEvent.NavigateToAddTransactionScreen)
    }

    fun addCategoryList()= viewModelScope.launch{

        //category db is empty
        categoryDao.insertCategory(Category(categoryName = CategoryType.accesory.name,categoryIcon = R.drawable.ic_accessory))
        categoryDao.insertCategory(Category(categoryName = CategoryType.book.name,categoryIcon = R.drawable.ic_book))
        categoryDao.insertCategory(Category(categoryName = CategoryType.car.name,categoryIcon = R.drawable.ic_car))
        categoryDao.insertCategory(Category(categoryName = CategoryType.clothes.name,categoryIcon = R.drawable.ic_clothes))
        categoryDao.insertCategory(Category(categoryName = CategoryType.coffee.name,categoryIcon = R.drawable.ic_coffe))
        categoryDao.insertCategory(Category(categoryName = CategoryType.computer.name,categoryIcon = R.drawable.ic_computer))
        categoryDao.insertCategory(Category(categoryName = CategoryType.cosmetic.name,categoryIcon = R.drawable.ic_cosmetic))
        categoryDao.insertCategory(Category(categoryName = CategoryType.drink.name,categoryIcon = R.drawable.ic_drink))
        categoryDao.insertCategory(Category(categoryName = CategoryType.electric.name,categoryIcon = R.drawable.ic_electric))
        categoryDao.insertCategory(Category(categoryName = CategoryType.entertainment.name,categoryIcon = R.drawable.ic_entertainmmment))
        categoryDao.insertCategory(Category(categoryName = CategoryType.fitness.name,categoryIcon = R.drawable.ic_fitness))
        categoryDao.insertCategory(Category(categoryName = CategoryType.food.name,categoryIcon = R.drawable.ic_food))
        categoryDao.insertCategory(Category(categoryName = CategoryType.gift.name,categoryIcon = R.drawable.ic_gift))
        categoryDao.insertCategory(Category(categoryName = CategoryType.grocery.name,categoryIcon = R.drawable.ic_grocery))
        categoryDao.insertCategory(Category(categoryName = CategoryType.home.name,categoryIcon = R.drawable.ic_home))
        categoryDao.insertCategory(Category(categoryName = CategoryType.hotel.name,categoryIcon = R.drawable.ic_hotel))
        categoryDao.insertCategory(Category(categoryName = CategoryType.laundry.name,categoryIcon = R.drawable.ic_laundry))
        categoryDao.insertCategory(Category(categoryName = CategoryType.medical.name,categoryIcon = R.drawable.ic_medical))
        categoryDao.insertCategory(Category(categoryName = CategoryType.oil.name,categoryIcon = R.drawable.ic_oil))
        categoryDao.insertCategory(Category(categoryName = CategoryType.phone.name,categoryIcon = R.drawable.ic_phone))
        categoryDao.insertCategory(Category(categoryName = CategoryType.pill.name,categoryIcon = R.drawable.ic_pill))
        categoryDao.insertCategory(Category(categoryName = CategoryType.restaurant.name,categoryIcon = R.drawable.ic_restaurant))
        categoryDao.insertCategory(Category(categoryName = CategoryType.shopping.name,categoryIcon = R.drawable.ic_shopping))
        categoryDao.insertCategory(Category(categoryName = CategoryType.taxi.name,categoryIcon = R.drawable.ic_taxi))
        categoryDao.insertCategory(Category(categoryName = CategoryType.train.name,categoryIcon = R.drawable.ic_train))
        categoryDao.insertCategory(Category(categoryName = CategoryType.working.name,categoryIcon = R.drawable.ic_working))
        categoryDao.insertCategory(Category(categoryName = CategoryType.other.name,categoryIcon = R.drawable.ic_other))
    }

    sealed class TransactionListEvent {
        object NavigateToAddTransactionScreen : TransactionListEvent()
        data class NavigateToEditTransactionScreen(val transactions: Transactions) : TransactionListEvent()
    }
}