package com.example.tugasuas.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(orderRoom: OrderRoom )

    @Update
    fun update(orderRoom: OrderRoom)

    @Delete
    fun delete(orderRoom: OrderRoom)

//    @Query("SELECT * from order_table ORDER BY id ASC")
//    fun getAllOrder(): LiveData<List<OrderRoom>>
// Inside OrderDao
    @Query("SELECT * FROM order_table WHERE user = :userEmail ORDER BY id ASC")
    fun getAllOrder(userEmail: String): LiveData<List<OrderRoom>>

}