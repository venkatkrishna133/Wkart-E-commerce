package com.example.wkart.repository

import com.example.wkart.data.CartDao
import com.example.wkart.model.Product

class ProductStoreRepository(private val cartDao: CartDao) {

    suspend fun addItemToCart(product: Product) {
        cartDao.addItemToCart(product)
    }

    suspend fun getCartItems(): List<Product> {
        return cartDao.getCartItems()
    }

    suspend fun removeItemFromCart(product: Product) {
        cartDao.removeItemFromCart(product)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
