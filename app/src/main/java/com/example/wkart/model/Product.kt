package com.example.wkart.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "cart_items") // Room table name
data class Product(
    @PrimaryKey @SerializedName("id") @ColumnInfo(name = "id") val id: Int,
    @SerializedName("title") @ColumnInfo(name = "title") val title: String,
    @SerializedName("image") @ColumnInfo(name = "image") val image: String,
    @SerializedName("description") @ColumnInfo(name = "description") val description: String,
    @SerializedName("price") @ColumnInfo(name = "price") val price: Double,
    @SerializedName("category") @ColumnInfo(name = "category") val category: String
)
