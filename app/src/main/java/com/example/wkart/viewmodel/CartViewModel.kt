package com.example.wkart.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wkart.data.DatabaseInstance
import com.example.wkart.model.Product
import com.example.wkart.repository.ProductStoreRepository
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val productStoreRepository: ProductStoreRepository

    init {
        val cartDao = DatabaseInstance.getDatabase(application).cartDao()
        productStoreRepository = ProductStoreRepository(cartDao)
    }

    fun addItemToCart(product: Product, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existingItems = productStoreRepository.getCartItems()
            val productExists = existingItems.any { it.id == product.id }

            if (!productExists) {
                // If product is not in cart, add it
                productStoreRepository.addItemToCart(product)
                onComplete(true)  // Successfully added the item to the cart
            } else {
                onComplete(false) // Product is already in cart
            }
        }
    }

    fun getCartItems(onComplete: (List<Product>) -> Unit) {
        viewModelScope.launch {
            val items = productStoreRepository.getCartItems()
            onComplete(items)
        }
    }

    fun removeItemFromCart(product: Product) {
        viewModelScope.launch {
            productStoreRepository.removeItemFromCart(product)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            productStoreRepository.clearCart()
        }
    }
}
