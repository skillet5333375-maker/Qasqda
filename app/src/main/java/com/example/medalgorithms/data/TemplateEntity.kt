package com.example.medalgorithms.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val title: String,
    val content: String,
    val updatedAt: Long = System.currentTimeMillis()
)
