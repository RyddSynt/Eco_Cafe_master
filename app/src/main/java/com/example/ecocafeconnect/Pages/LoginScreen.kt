package com.example.ecocafeconnect.Pages

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthState
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isNavigating by remember { mutableStateOf(false) }

    // Use collectAsStateWithLifecycle for StateFlow
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Log.d("LoginScreen", "Current authState: $authState")

    // SharedPreferences to save email
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Google Sign-In Launcher
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginScreen", "Google launcher result: ${result.resultCode}")
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                Log.d("LoginScreen", "Google account: ${account?.email}, idToken: ${idToken != null}")
                if (idToken != null) {
                    Log.d("LoginScreen", "Calling signInWithGoogle")
                    authViewModel.signInWithGoogle(idToken)

                    // Show loading indicator
                    Toast.makeText(context, "Signing in with Google...", Toast.LENGTH_SHORT).show()

                    // FIXED: Use CoroutineScope instead of LaunchedEffect
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        if (!isNavigating) {
                            Log.d("LoginScreen", "Fallback navigation triggered")
                            isNavigating = true
                            navController.navigate("home2") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                } else {
                    Log.e("LoginScreen", "No ID token received")
                    Toast.makeText(context, "Google sign in failed: No ID token", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Log.e("LoginScreen", "Google sign in error: ${e.message}")
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("LoginScreen", "Google sign in cancelled")
        }
    }

    // Initialize Google Sign-In
    fun signInWithGoogle() {
        try {
            Log.d("LoginScreen", "Starting Google Sign-In")
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
            val signInIntent = googleSignInClient.signInIntent
            googleLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e("LoginScreen", "Google Sign-In error: ${e.message}")
            Toast.makeText(context, "Google Sign-In error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle authentication state changes
    LaunchedEffect(authState) {
        Log.d("LoginScreen", "LaunchedEffect triggered with authState: $authState")
        when (authState) {
            is AuthState.Authenticated -> {
                if (!isNavigating) {
                    Log.d("LoginScreen", "Authentication successful! Navigating to home2")
                    isNavigating = true
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home2") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            is AuthState.Error -> {
                Log.e("LoginScreen", "Authentication error: ${(authState as AuthState.Error).message}")
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }
            is AuthState.Loading -> {
                Log.d("LoginScreen", "Loading state")
            }
            else -> {
                Log.d("LoginScreen", "Other state: $authState")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = "Login Image",
            modifier = Modifier.size(392.dp)
        )

        Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Login to your account")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email address") },
            enabled = authState != AuthState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = authState != AuthState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                editor.putString("email", email)
                editor.apply()
                authViewModel.login(email, password)
            },
            enabled = authState != AuthState.Loading
        ) {
            if (authState == AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Login")
            }
        }

        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "Don't have an account yet? Signup here")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "facebook",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        Toast.makeText(context, "Facebook login coming soon!", Toast.LENGTH_SHORT).show()
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "google",
                modifier = Modifier
                    .size(60.dp)
                    .clickable(enabled = authState != AuthState.Loading) {
                        signInWithGoogle()
                    }
            )
        }

        // Test button (keep for debugging)
        Button(
            onClick = {
                Log.d("LoginScreen", "Manual navigation triggered")
                navController.navigate("home2") {
                    popUpTo("login") { inclusive = true }
                }
            }
        ) {
            Text("Test Navigation")
        }
    }
}