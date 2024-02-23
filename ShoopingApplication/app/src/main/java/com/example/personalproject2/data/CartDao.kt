package com.example.personalproject2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    // Add a new entry
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(c: CartEntity)

    // Update an existing entry
    @Update
    suspend fun update(c: CartEntity)
    @Delete
    suspend fun delete(c: CartEntity)
    // Delete a existing entry
    @Query("DELETE FROM Cart WHERE id = :id" )
    suspend fun deletecart(id:Int)

    @Query("SELECT * FROM Cart WHERE user = :user")
    fun getCart(user: String): Flow<List<CartEntity>>

    @Query("UPDATE Cart SET num = :num WHERE id = :id")
    suspend fun updateCart(id: Int, num: String)

    @Query("DELETE FROM Cart WHERE user = :user" )
    suspend fun cleancart(user: String)



}