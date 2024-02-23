package com.example.personalproject2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var Instance: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CartDatabase::class.java, "Cart_Database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}