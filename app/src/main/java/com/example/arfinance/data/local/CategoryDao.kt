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

    @Query("SELECT * FROM Category_table WHERE categoryName LIKE '%' || :searchQuery || '%' ORDER BY categoryName")
    fun getAllCategories(searchQuery: String): Flow<List<Category>>

    @Query("SELECT * FROM Category_table WHERE id = :id")
    fun getCategoryById(id:Int): Category

    @Query("SELECT count(*) FROM Category_table WHERE categoryName = :categoryName")
    fun getIsCategoryExisted(categoryName:String) : Int
}