package com.example.medalgorithms.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Query("SELECT * FROM templates WHERE category = :category ORDER BY updatedAt DESC")
    fun observeByCategory(category: String): Flow<List<TemplateEntity>>

    @Query("SELECT * FROM templates WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TemplateEntity?

    @Insert
    suspend fun insert(item: TemplateEntity): Long

    @Update
    suspend fun update(item: TemplateEntity)

    @Delete
    suspend fun delete(item: TemplateEntity)
}
