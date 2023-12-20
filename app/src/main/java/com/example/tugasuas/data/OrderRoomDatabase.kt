package com.example.tugasuas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [OrderRoom::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Add this line to use the converters
abstract class OrderRoomDatabase : RoomDatabase(){
    abstract fun orderDao() : OrderDao?
    companion object {
        @Volatile
        private var INSTANCE: OrderRoomDatabase? = null
        fun getDatabase(context: Context) : OrderRoomDatabase ? {
            if (INSTANCE == null) {
                synchronized(OrderRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(context.applicationContext, OrderRoomDatabase::class.java, "order_database").build()
                }
            }
            return INSTANCE
        }
    }
}