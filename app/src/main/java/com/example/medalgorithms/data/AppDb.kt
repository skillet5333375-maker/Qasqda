package com.example.medalgorithms.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TemplateEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
}
