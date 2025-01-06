package com.example.wkart.ux

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


@Composable
fun UPISelectionScreen(navController: NavController) {
    val context = LocalContext.current // Get the context
    val customUPI = remember { mutableStateOf("") } // State for custom UPI ID

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select Payment Option", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(20.dp))

        // GPay Button
        UPIButton("Google Pay") {
            navigateToUPI("com.google.android.apps.nbu.paisa.user", context)
        }

        // Paytm Button
        UPIButton("Paytm") {
            navigateToUPI("net.one97.paytm", context)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Custom UPI Input Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // UPI ID Input Field
            OutlinedTextField(
                value = customUPI.value,
                onValueChange = { newValue ->
                    customUPI.value = newValue
                },
                label = { Text("Enter UPI ID") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )


            // Send Button
            Button(
                onClick = {
                    if (customUPI.value.isNotEmpty()) {
                        sendUPIRequest(customUPI.value, context)
                    } else {
                        Toast.makeText(context, "Please enter a valid UPI ID", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
fun sendUPIRequest(upiId: String, context: Context) {
    try {
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", upiId) // Payee address
            .appendQueryParameter("pn", "Recipient Name") // Payee name
            .appendQueryParameter("mc", "0000") // Merchant code (optional)
            .appendQueryParameter("tid", "1234567890") // Transaction ID (optional)
            .appendQueryParameter("tr", "txn123456") // Transaction reference ID
            .appendQueryParameter("tn", "Payment for order") // Transaction note
            .appendQueryParameter("am", "100.00") // Transaction amount
            .appendQueryParameter("cu", "INR") // Currency code
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Check if any UPI app is available
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Request send To the Upi App Check it !", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Request send To the Upi App Check it !", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun UPIButton(buttonText: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(buttonText)
    }
}

fun navigateToUPI(packageName: String, context: Context) {
    try {
        val uri = Uri.parse("upi://pay?pa=Wkartest@upi&pn=UPI&mc=0000&tid=1234567890&url=https://www.example.com")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(packageName)

        // Check if the UPI app is installed
        if (isPackageInstalled(packageName, context)) {
            context.startActivity(intent)
        } else {
            // If the app is not installed, show a message to the user
            Toast.makeText(context, "$packageName not installed", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle any other exceptions that might occur
    }
}
// Check if a package is installed
fun isPackageInstalled(packageName: String, context: Context): Boolean {
    val pm = context.packageManager
    return try {
        pm.getPackageInfo(packageName, 0)
        true
    } catch (e: Exception) {
        false
    }
}