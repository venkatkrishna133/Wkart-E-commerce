package com.example.wkart.repository

import com.example.wkart.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    private val productUrl = "https://raw.githubusercontent.com/venkatkrishna133/DataCetere/refs/heads/main/products.json"

    suspend fun fetchProducts(): List<Product>? {
        return withContext(Dispatchers.IO) { // Switch to IO thread for network operations
            try {
                // Log the start of the fetch
                Log.d("ProductRepository", "Fetching products from: $productUrl")

                // Establish connection to the URL
                val url = URL(productUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.inputStream.bufferedReader().use {
                    val response = it.readText()

                    // Parse JSON response into a list of Product objects
                    val listType = object : TypeToken<List<Product>>() {}.type
                    val products = Gson().fromJson<List<Product>>(response, listType)

                    // Log success
                    Log.d("ProductRepository", "Successfully fetched products: $products")
                    products
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error fetching products", e)
                null
            }
        }
    }
}