package com.example.wkart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.wkart.model.Product

@Dao
interface CartDao {

    @Insert
    suspend fun addItemToCart(product: Product)

    @Query("SELECT * FROM cart_items")
    suspend fun getCartItems(): List<Product> // Returns list of products in the cart

    @Delete
    suspend fun removeItemFromCart(product: Product)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
