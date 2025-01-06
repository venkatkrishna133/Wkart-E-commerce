package com.example.wkart.ux

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wkart.viewmodel.CartViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.wkart.model.Product
import com.example.wkart.navigation.Screens

@Composable
fun CartScreen(cartViewModel: CartViewModel = viewModel(), navController: NavController) {
    // Use a LiveData observer or a flow collector
    val cartItems = remember { mutableStateOf<List<Product>>(emptyList()) }

    // Launch a coroutine to observe cart items from the ViewModel
    LaunchedEffect(Unit) {
        cartViewModel.getCartItems { items ->
            cartItems.value = items
        }
    }

    // Log the cart items whenever the screen is recomposed
    Log.d("CartScreen", "Cart items: ${cartItems.value}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).systemBarsPadding()
    ) {
        // Cart Header
        Text(
            text = "Cart Items",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display cart items or an empty message
        if (cartItems.value.isEmpty()) {
            Log.d("CartScreen", "Your cart is empty.")
            Text(
                text = "Your cart is empty.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            // List of cart items with scrollable behavior
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Takes available space and allows the button below to remain visible
                    .fillMaxWidth()
            ) {
                items(cartItems.value) { product ->
                    Log.d("CartScreen", "Displaying item: ${product.title}")

                    // Cart Item UI
                    CartItem(
                        product = product,
                        onRemove = {
                            cartViewModel.removeItemFromCart(product)
                            // Refresh the list after removal
                            cartViewModel.getCartItems { updatedItems ->
                                cartItems.value = updatedItems
                            }
                        },
                        onBuyNow = {
                            // Handle buy now action, navigate to the UPI selection page here
                            Log.d("BuyNowButton", "Buy Now button clicked")
                            navController.navigate(Screens.UPISelection.route) // Replace with your destination route
                        },
                        navController = navController
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clear Cart Button remains at the bottom of the screen
        if (cartItems.value.isNotEmpty()) {
            Button(
                onClick = {
                    cartViewModel.clearCart()
                    // Refresh the list after clearing
                    cartViewModel.getCartItems { updatedItems ->
                        cartItems.value = updatedItems
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Clear Cart")
            }
        }
    }
}
