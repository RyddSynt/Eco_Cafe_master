    package com.example.ecocafeconnect.Pages.HomePage

    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.Context
    import android.content.Intent
    import android.os.Build
    import android.util.Log
    import android.widget.Toast
    import androidx.compose.animation.core.animateFloatAsState
    import androidx.compose.animation.core.tween
    import androidx.compose.foundation.Canvas
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.material3.Button
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.DisposableEffect
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.livedata.observeAsState
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.StrokeCap
    import androidx.compose.ui.graphics.drawscope.Stroke
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.Dp
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.core.app.NotificationCompat
    import androidx.navigation.NavController
    import com.example.ecocafeconnect.AuthState
    import com.example.ecocafeconnect.AuthViewModel
    import com.example.ecocafeconnect.MainActivity
    import com.example.ecocafeconnect.R
    import com.example.ecocafeconnect.ui.theme.Blue50
    import com.example.ecocafeconnect.ui.theme.bebasFamily
    import com.google.firebase.Firebase
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FieldValue
    import com.google.firebase.firestore.FirebaseFirestore
    import com.google.firebase.firestore.firestore

    @Composable
    fun LoyaltyProgram(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

        val authState = authViewModel.authstate.observeAsState()
        val context = LocalContext.current
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        var points by remember { mutableStateOf(0) }
        var progress by remember { mutableStateOf(0f) }

        // Retrieve shared preferences
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "") ?: ""

        LaunchedEffect(authState.value) {
            when (authState.value) {
                is AuthState.Unauthenticated -> navController.navigate("login")
                else -> Unit
            }
        }

        DisposableEffect(userId) {
            val listenerRegistration = userId?.let { id ->
                db.collection("users").document(id)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("LoyaltyProgram", "Listen failed", error)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            points = (snapshot.getLong("points") ?: 0L).toInt()
                            progress = points / 100f
                        }
                    }
            }
            onDispose {
                listenerRegistration?.remove()
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loyalty Program",
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                fontFamily = bebasFamily,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                CircularProgressBar(
                    percentage = progress.coerceIn(0f, 1f),
                    radius = 150.dp,
                    animationDuration = 1000
                )
                Text(
                    text = "$points/100%",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF7A288A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (email == "admin@gmail.com") {
                Button(
                    onClick = {
                        val intent = Intent(context, QRScannerActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = "Scan QR")
                }
            } else {
                Button(
                    onClick = {
                        userId?.let { id ->
                            Toast.makeText(context, "User ID: $id", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, QRGenerator::class.java)
                            intent.putExtra("USER_ID", id)
                            context.startActivity(intent)
                        } ?: Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Text(text = "Generate QR")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (points >= 25) {
                Text(text = "Reward: 25 points reached!")
            }
            if (points >= 50) {
                Text(text = "Reward: 50 points reached!")
            }
            if (points >= 75) {
                Text(text = "Reward: 75 points reached!")
            }
            if (points >= 100) {
                Text(text = "Reward: 100 points reached!")
            }
        }
    }

    @Composable
    fun CircularProgressBar(
        percentage: Float,
        radius: Dp = 80.dp,
        animationDuration: Int = 1000,
    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = percentage,
            animationSpec = tween(durationMillis = animationDuration)
        )

        Canvas(modifier = Modifier.size(radius * 2)) {
            drawCircle(
                color = Color.LightGray,
                radius = radius.toPx(),
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF7A288A),
                startAngle = -90f,
                sweepAngle = animatedProgress * 360,
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round),
                size = size
            )
        }
    }
