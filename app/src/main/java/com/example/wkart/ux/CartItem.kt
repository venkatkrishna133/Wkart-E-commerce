package com.example.wkart.ux

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.wkart.model.Product


import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.filled.AddCircle // For the + button
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.wkart.R
import com.example.wkart.navigation.Screens


@Composable
fun CartItem(product: Product, onRemove: () -> Unit, onBuyNow: () -> Unit, navController: NavController) {
    var quantity by remember { mutableStateOf(1) } // State for quantity
    val totalPrice = remember { mutableStateOf(product.price * quantity) } // Calculate total price

    // UI for individual cart item
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Image
        Image(
            painter = rememberAsyncImagePainter(product.image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp),
            contentScale = ContentScale.Crop
        )

        // Product Details
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "₹${totalPrice.value}",
                    style = MaterialTheme.typography.h6,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Remove Button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Remove from Cart")
            }
        }

        // Quantity adjustment section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                if (quantity > 1) {
                    quantity -= 1
                    totalPrice.value = product.price * quantity
                }
            }) {
                Image(painter = painterResource(id = R.drawable.minus), contentDescription = "Decrease Quantity")
            }

            Text(
                text = "$quantity",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(onClick = {
                quantity += 1
                totalPrice.value = product.price * quantity
            }) {
                Icon(Icons.Default.AddCircle, contentDescription = "Increase Quantity")
            }
        }

        // Buy Now Icon Button
        BuyNowButton(onBuyNow = onBuyNow, navController = navController)
    }
}

@Composable
fun BuyNowButton(onBuyNow: () -> Unit,navController: NavController) {
    // Apply this fix to your OutlinedButton shape
    OutlinedButton(
        onClick = {
            onBuyNow()
            navController.navigate(Screens.UPISelection.route) // Navigate to UPI selection
        },
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(56.dp), // Adjust the height as per your preference
        shape = MaterialTheme.shapes.small.copy(
            topStart = CornerSize(8.dp), // Rounded corner on top left
            topEnd = CornerSize(8.dp)    // Rounded corner on top right
        ),
        border = BorderStroke(1.dp, Color.Gray) // Border color and thickness
    ) {
        // Icon and Text together inside the OutlinedButton
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Icon
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Buy Now",
                modifier = Modifier.padding(end = 8.dp) // Padding between icon and text
            )

            // Text
            Text(
                text = "Buy Now",
                style = MaterialTheme.typography.button,
                color = Color.Gray // Customize the text color if needed
            )
        }
    }
}