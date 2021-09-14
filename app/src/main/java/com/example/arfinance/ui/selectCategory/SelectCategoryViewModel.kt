package com.example.arfinance.ui.selectCategory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.data.local.CategoryDao
import com.example.arfinance.util.enumerian.CategoryType
import kotlinx.coroutines.launch

class SelectCategoryViewModel @ViewModelInject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    val categories = categoryDao.getAllCategories().asLiveData()

    fun set() = viewModelScope.launch {
        categoryDao.insertCategory(Category(categoryName = CategoryType.accesory,categoryIcon = R.drawable.ic_accessory))
        categoryDao.insertCategory(Category(categoryName = CategoryType.book,categoryIcon = R.drawable.ic_book))
        categoryDao.insertCategory(Category(categoryName = CategoryType.car,categoryIcon = R.drawable.ic_car))
        categoryDao.insertCategory(Category(categoryName = CategoryType.clothes,categoryIcon = R.drawable.ic_clothes))
        categoryDao.insertCategory(Category(categoryName = CategoryType.coffee,categoryIcon = R.drawable.ic_coffe))
        categoryDao.insertCategory(Category(categoryName = CategoryType.computer,categoryIcon = R.drawable.ic_computer))
        categoryDao.insertCategory(Category(categoryName = CategoryType.cosmetic,categoryIcon = R.drawable.ic_cosmetic))
        categoryDao.insertCategory(Category(categoryName = CategoryType.drink,categoryIcon = R.drawable.ic_drink))
        categoryDao.insertCategory(Category(categoryName = CategoryType.electric,categoryIcon = R.drawable.ic_electric))
        categoryDao.insertCategory(Category(categoryName = CategoryType.entertainment,categoryIcon = R.drawable.ic_entertainmmment))
        categoryDao.insertCategory(Category(categoryName = CategoryType.fitness,categoryIcon = R.drawable.ic_fitness))
        categoryDao.insertCategory(Category(categoryName = CategoryType.food,categoryIcon = R.drawable.ic_food))
        categoryDao.insertCategory(Category(categoryName = CategoryType.gift,categoryIcon = R.drawable.ic_gift))
        categoryDao.insertCategory(Category(categoryName = CategoryType.grocery,categoryIcon = R.drawable.ic_grocery))
        categoryDao.insertCategory(Category(categoryName = CategoryType.home,categoryIcon = R.drawable.ic_home))
        categoryDao.insertCategory(Category(categoryName = CategoryType.hotel,categoryIcon = R.drawable.ic_hotel))
        categoryDao.insertCategory(Category(categoryName = CategoryType.laundry,categoryIcon = R.drawable.ic_laundry))
        categoryDao.insertCategory(Category(categoryName = CategoryType.medical,categoryIcon = R.drawable.ic_medical))
        categoryDao.insertCategory(Category(categoryName = CategoryType.oil,categoryIcon = R.drawable.ic_oil))
        categoryDao.insertCategory(Category(categoryName = CategoryType.phone,categoryIcon = R.drawable.ic_phone))
        categoryDao.insertCategory(Category(categoryName = CategoryType.pill,categoryIcon = R.drawable.ic_pill))
        categoryDao.insertCategory(Category(categoryName = CategoryType.restaurant,categoryIcon = R.drawable.ic_restaurant))
        categoryDao.insertCategory(Category(categoryName = CategoryType.shopping,categoryIcon = R.drawable.ic_shopping))
        categoryDao.insertCategory(Category(categoryName = CategoryType.taxi,categoryIcon = R.drawable.ic_taxi))
        categoryDao.insertCategory(Category(categoryName = CategoryType.train,categoryIcon = R.drawable.ic_train))
        categoryDao.insertCategory(Category(categoryName = CategoryType.working,categoryIcon = R.drawable.ic_working))
        categoryDao.insertCategory(Category(categoryName = CategoryType.other,categoryIcon = R.drawable.ic_other))
    }
}