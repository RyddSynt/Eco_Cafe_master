package com.example.ecocafeconnect.Pages.HomePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecocafeconnect.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.zxing.integration.android.IntentIntegrator

class QRScannerActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Firestore instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        // Start the QR code scanner when the activity is created
        val integrator = IntentIntegrator(this).apply {
            setOrientationLocked(true) // Keep scanner in portrait mode
            setPrompt("Scan a QR Code") // Show a prompt message
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // Scan only QR codes
            setBeepEnabled(false) // Disable beep sound (fixes some package name issues)
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val scannedUserId = result.contents // Extracted QR Code Data

                // Show Toast with scanned QR Code value
                Toast.makeText(this, "Scanned QR: $scannedUserId", Toast.LENGTH_LONG).show()

                // Proceed to check and update Firestore user points
                checkAndUpdateUserPoints(scannedUserId)
            }
        }
    }

    private fun checkAndUpdateUserPoints(userId: String) {
        val userRef = db.collection("users").document(userId)

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // User found, update points
                userRef.update("points", FieldValue.increment(5))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Points updated! +5", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating points: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // User not found
                Toast.makeText(this, "User ID not found in database", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error retrieving user: ${e.message}", Toast.LENGTH_SHORT).show()

        }
    }
}