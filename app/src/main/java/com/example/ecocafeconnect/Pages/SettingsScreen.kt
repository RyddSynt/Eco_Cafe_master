package com.example.ecocafeconnect.Pages

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthState
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    // FIXED: Use authState (capital S) instead of authstate
    val authState by authViewModel.authState.collectAsState()

    // SharedPreferences initialization
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // Get the current Activity
    val activity = LocalContext.current as? Activity

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.a),
                contentDescription = "Settings Image",
                modifier = Modifier.size(392.dp)
            )

            Text(text = "Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.size(16.dp))

            TextButton(onClick = {
                // Sign out and clear shared preferences
                authViewModel.signout()

                // Clear SharedPreferences
                sharedPreferences.edit().clear().apply()

                // Close the app
                activity?.finishAffinity()
            }) {
                Text(text = "Sign Out and Exit", fontSize = 18.sp)
            }
        }
    }
}