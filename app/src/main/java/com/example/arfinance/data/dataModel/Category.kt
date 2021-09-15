package com.example.arfinance.data.dataModel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arfinance.util.enumerian.CategoryType
import kotlinx.parcelize.Parcelize

@Entity(tableName = "category_table")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var categoryName: String,
    var categoryIcon: Int
) : Parcelable