package com.example.wkart.ux


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wkart.navigation.Screens
import coil.compose.rememberAsyncImagePainter
import com.example.wkart.model.Product
import com.example.wkart.repository.ProductRepository
import com.example.wkart.viewmodel.CartViewModel
import kotlinx.coroutines.launch


@Composable
fun ProductDetailsScreen(navController: NavController, productId: Int) {
    val product = remember { mutableStateOf<Product?>(null) }
    val cartViewModel: CartViewModel = viewModel() // Obtain CartViewModel
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Fetch product details
    LaunchedEffect(productId) {
        val repository = ProductRepository()
        product.value = repository.fetchProducts()?.find { it.id == productId }
    }

    product.value?.let { currentProduct ->
        // Display product details

        Column(modifier = Modifier.fillMaxSize().padding(16.dp).systemBarsPadding()) {
            Image(
                painter = rememberAsyncImagePainter(currentProduct.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )

            Text(text = currentProduct.title, style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = currentProduct.description, style = MaterialTheme.typography.body2, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Text(text = "$${currentProduct.price}", style = MaterialTheme.typography.h6, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))

            // "Add to Cart" Button
            Button(
                onClick = {
                    scope.launch {
                        // Check if the product is already in the cart
                        cartViewModel.addItemToCart(currentProduct) { added ->
                            if (added) {
                                // If the item was added to the cart, navigate to the Cart screen
                                Log.d("CartViewModel", "Item added to cart: ${currentProduct.title}")
                                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screens.CartScreen.route) // Navigate to the Cart screen
                            } else {
                                // If the product is already in the cart, show a message
                                Toast.makeText(context, "Product already in cart", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add to Cart")
            }


            // "Buy Now" Button
            Button(
                onClick = {Log.d("BuyNowButton", "Buy Now button clicked")
                    navController.navigate(Screens.UPISelection.route) },


                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Buy Now")
            }
        }
    }
}
