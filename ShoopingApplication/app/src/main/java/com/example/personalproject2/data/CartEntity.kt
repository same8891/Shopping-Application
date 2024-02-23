package com.example.personalproject2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Data class to represent the foodOrder table
@Entity(tableName = "Cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    // Used to label a column name
    @ColumnInfo(name = "id")
    var id: Int? = null,
    var user: String? = null,
    var product: String? = null,
    var productid: String? = null,
    var num: String? = null,
    var price: String? = null

)