package com.example.ecocafeconnect

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecocafeconnect.Pages.HomePage
import com.example.ecocafeconnect.Pages.HomePage.AdsScreen
import com.example.ecocafeconnect.Pages.HomePage.DetailsTips
import com.example.ecocafeconnect.Pages.HomePage.LoyaltyProgram
import com.example.ecocafeconnect.Pages.HomePage.StatisticsScreen
import com.example.ecocafeconnect.Pages.HomePage.WasteTracker
import com.example.ecocafeconnect.Pages.LoginScreen
import com.example.ecocafeconnect.Pages.RSTips.TipsPage
import com.example.ecocafeconnect.Pages.SettingsScreen
import com.example.ecocafeconnect.Pages.SignupPage
import com.example.ecocafeconnect.wasteTracker.WasteEntryListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    // Get the context from the LocalContext
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("settings") {
            // Pass context to SettingsScreen
            SettingsScreen(navController = navController, authViewModel = authViewModel, context = context)
        }
        composable("home2") {
            LearnNavDrawer(navController)
        }
        composable("waste") {
            WasteTracker(modifier, navController, WasteEntryListViewModel(), authViewModel)
        }
        composable("ads") {
            AdsScreen(modifier, navController, authViewModel)
        }
        composable("loyalty") {
            LoyaltyProgram(modifier, navController, authViewModel)
        }
        composable("tips") {
            TipsPage(modifier, navController, authViewModel)
        }
        composable("details") {
            DetailsTips(modifier, navController, authViewModel)
        }
        composable("homepage") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("stats") {
            StatisticsScreen(modifier, navController, WasteEntryListViewModel(), authViewModel)
        }
    }
}
