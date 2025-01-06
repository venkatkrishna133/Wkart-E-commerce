package com.example.wkart.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wkart.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}