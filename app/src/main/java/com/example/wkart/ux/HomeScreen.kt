package com.example.wkart.ux


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wkart.model.Product
import com.example.wkart.navigation.Screens
import com.example.wkart.repository.ProductRepository
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip


@Composable
fun HomeScreen(navController: NavController) {
    val products = remember { mutableStateOf<List<Product>?>(null) }
    val repository = ProductRepository()

    // Fetch products when the composable is launched
    LaunchedEffect(Unit) {
        products.value = repository.fetchProducts()
    }

    Scaffold(
        topBar = { CustomTopBar(title = "Home") },
        floatingActionButton = {
            // FloatingActionButton for navigating to CartScreen
            FloatingActionButton(
                onClick = { navController.navigate(Screens.CartScreen.route) },
                modifier = Modifier.padding(bottom = 16.dp) // Add padding if needed
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Go to Cart"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center, // Ensures it is at the bottom center
    )  { innerPadding ->
        if (products.value == null) {
            // Show loading indicator if products are not yet loaded
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Display products in a grid view
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp), // Grid with adaptive column size
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.padding(innerPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products.value!!) { product ->
                    ProductCard(product = product) {
                        navController.navigate(Screens.ProductDetails.createRoute(product.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp), // Correct usage of card elevation
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(6.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            GlideImage(url = product.image, modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // Allocate a fixed height for the image
                .clip(RoundedCornerShape(4.dp)),)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${product.price}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        }
    }
}

