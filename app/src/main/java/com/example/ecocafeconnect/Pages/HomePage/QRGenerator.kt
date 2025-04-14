package com.example.ecocafeconnect.Pages.HomePage

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QRGenerator : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve USER_ID from intent
        val userId = intent.getStringExtra("USER_ID") ?: "UNKNOWN"

        // Show a toast message with the user ID
        Toast.makeText(this, "User ID: $userId", Toast.LENGTH_LONG).show()

        setContent {
            QRGeneratorScreen(userId)
        }
    }
}

@Composable
fun QRGeneratorScreen(userId: String) {
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        qrBitmap?.let { bitmap ->
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Generated QR Code")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                qrBitmap = generateQRCode(userId) // Use USER_ID in the QR code
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Generate QR")
        }
    }
}

fun generateQRCode(text: String): Bitmap? {
    return try {
        val size = 1000
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
