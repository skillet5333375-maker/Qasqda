package com.example.medalgorithms

import android.app.Application
import androidx.room.Room
import com.example.medalgorithms.data.AppDb

class App : Application() {
    lateinit var db: AppDb
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDb::class.java, "app.db").build()
    }
}
