package com.example.arfinance.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arfinance.data.dataModel.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(Category: Category)

    @Delete
    suspend fun deleteCategory(Category: Category)

    @Update
    suspend fun updateCategory(Category: Category)

    @Query("SELECT count(id) FROM category_table")
    fun getCategoryCount(): Flow<Int>

    @Query("SELECT * FROM Category_table")
    fun getAllCategories(): Flow<List<Category>>

    
}