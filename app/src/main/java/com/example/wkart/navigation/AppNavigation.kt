package com.example.wkart.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wkart.ux.CartScreen
import com.example.wkart.ux.HomeScreen
import com.example.wkart.ux.ProductDetailsScreen
import com.example.wkart.ux.UPISelectionScreen


sealed class Screens(val route: String) {
    object Home : Screens("home")
    object ProductDetails : Screens("productDetails/{productId}") {
        fun createRoute(productId: Int) = "productDetails/$productId"
    }
    object CartScreen : Screens("CartScreen")
    object UPISelection : Screens("upiSelection")
}


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(Screens.ProductDetails.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt()
            productId?.let {
                ProductDetailsScreen(navController, it)
            }
        }
        composable(Screens.CartScreen.route) {
            CartScreen(navController = navController)
        }
        composable(Screens.UPISelection.route) {
            UPISelectionScreen(navController)
        }
    }
}