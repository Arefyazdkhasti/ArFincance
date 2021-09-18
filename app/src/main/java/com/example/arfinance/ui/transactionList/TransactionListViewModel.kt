package com.example.arfinance.ui.transactionList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.CategoryDao
import com.example.arfinance.data.local.TransactionDao
import com.example.arfinance.util.enumerian.CategoryType
import com.example.arfinance.util.enumerian.TransactionType
import com.example.arfinance.util.getURLForResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionListViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    //change to state
    val dateQuery = MutableStateFlow("")
    private val transactionFlow = dateQuery.flatMapLatest {
        transactionDao.getTransactionByDate(it)
    }
    val transaction = transactionFlow.asLiveData()

    private val transactionsEventChannel = Channel<TransactionListEvent>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()

    fun addNewTransactionClicked() = viewModelScope.launch {
        transactionsEventChannel.send(TransactionListEvent.NavigateToAddTransactionScreen)
    }

    fun onTransactionSelected(transaction: Transactions) = viewModelScope.launch {
        transactionsEventChannel.send(
            TransactionListEvent.NavigateToEditTransactionScreen(
                transaction
            )
        )
        
    }


    val categoryDbSize = categoryDao.getCategoryCount().asLiveData()
    fun addCategoryList() = viewModelScope.launch {
        //category db is empty
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.accesory.name,
                categoryIcon = getURLForResource(R.drawable.ic_accessory)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.book.name,
                categoryIcon = getURLForResource(R.drawable.ic_book)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.car.name,
                categoryIcon = getURLForResource(R.drawable.ic_car)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.clothes.name,
                categoryIcon = getURLForResource(R.drawable.ic_clothes)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.coffee.name,
                categoryIcon = getURLForResource(R.drawable.ic_coffe)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.computer.name,
                categoryIcon = getURLForResource(R.drawable.ic_computer)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.cosmetic.name,
                categoryIcon = getURLForResource(R.drawable.ic_cosmetic)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.drink.name,
                categoryIcon = getURLForResource(R.drawable.ic_drink)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.electric.name,
                categoryIcon = getURLForResource(R.drawable.ic_electric)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.entertainment.name,
                categoryIcon = getURLForResource(R.drawable.ic_entertainmmment)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.fitness.name,
                categoryIcon = getURLForResource(R.drawable.ic_fitness)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.food.name,
                categoryIcon = getURLForResource(R.drawable.ic_food)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.gift.name,
                categoryIcon = getURLForResource(R.drawable.ic_gift)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.grocery.name,
                categoryIcon = getURLForResource(R.drawable.ic_grocery)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.home.name,
                categoryIcon = getURLForResource(R.drawable.ic_home)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.hotel.name,
                categoryIcon = getURLForResource(R.drawable.ic_hotel)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.laundry.name,
                categoryIcon = getURLForResource(R.drawable.ic_laundry)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.medical.name,
                categoryIcon = getURLForResource(R.drawable.ic_medical)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.oil.name,
                categoryIcon = getURLForResource(R.drawable.ic_oil)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.phone.name,
                categoryIcon = getURLForResource(R.drawable.ic_phone)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.pill.name,
                categoryIcon = getURLForResource(R.drawable.ic_pill)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.restaurant.name,
                categoryIcon = getURLForResource(R.drawable.ic_restaurant)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.shopping.name,
                categoryIcon = getURLForResource(R.drawable.ic_shopping)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.taxi.name,
                categoryIcon = getURLForResource(R.drawable.ic_taxi)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.train.name,
                categoryIcon = getURLForResource(R.drawable.ic_train)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.working.name,
                categoryIcon = getURLForResource(R.drawable.ic_working)
            )
        )
        categoryDao.insertCategory(
            Category(
                categoryName = CategoryType.other.name,
                categoryIcon = getURLForResource(R.drawable.ic_other)
            )
        )
    }

    // balance
    val balanceWeekStartDate = state.getLiveData("balanceWeekStartDate", "")
    val balanceWeekEndDate = state.getLiveData("balanceWeekEndDate", "")

    private val balanceIncomeWeekFlow = combine(
        balanceWeekStartDate.asFlow(),
        balanceWeekEndDate.asFlow()
    )
    { start, end ->
        Pair(start, end)
    }.flatMapLatest { (startDate, endDate) ->
        transactionDao.getTransactionsSumAmountByDate(TransactionType.Income, startDate, endDate)
    }

    private val balanceExpenseWeekFlow = combine(
        balanceWeekStartDate.asFlow(),
        balanceWeekEndDate.asFlow()
    )
    { start, end ->
        Pair(start, end)
    }.flatMapLatest { (startDate, endDate) ->
        transactionDao.getTransactionsSumAmountByDate(TransactionType.Expense, startDate, endDate)
    }

    val balanceIncomeWeek = balanceIncomeWeekFlow.asLiveData()
    val balanceExpenseWeek = balanceExpenseWeekFlow.asLiveData()

    //transaction by category
    private val transactionListDateRangeFlow = combine(
        balanceWeekStartDate.asFlow(),
        balanceWeekEndDate.asFlow()
    )
    { start, end ->
        Pair(start, end)
    }.flatMapLatest { (startDate, endDate) ->
        transactionDao.getTransactionsByDateRange(TransactionType.Expense ,startDate, endDate)
    }
    val transactionListDateRange = transactionListDateRangeFlow.asLiveData()

    sealed class TransactionListEvent {
        object NavigateToAddTransactionScreen : TransactionListEvent()
        data class NavigateToEditTransactionScreen(val transactions: Transactions) : TransactionListEvent()
        // data class SelectDateToShow(val date:String): TransactionListEvent()
    }
}