package com.example.arfinance.data.dataModel

import android.os.Parcelable
import com.example.arfinance.util.enumerian.CategoryType
import kotlinx.parcelize.Parcelize

data class Category(
     var id: Int,
     var categoryName: CategoryType,
     var categoryIcon: Int
)