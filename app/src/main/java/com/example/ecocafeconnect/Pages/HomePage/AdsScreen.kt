package com.example.ecocafeconnect.Pages.HomePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.R

@Composable
fun AdsScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.a), contentDescription = "Settings Image",
                modifier = Modifier.size(392.dp)
                .clickable { navController.navigate("loyalty")})


            Text(text = "Picture To Promote Loyalty Program, Click To Proceed", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("loyalty")})

    }
}
}
