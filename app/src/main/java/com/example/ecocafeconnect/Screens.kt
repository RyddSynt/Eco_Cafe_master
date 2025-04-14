package com.example.ecocafeconnect

sealed class Screens (val screens: String) {
    data object HomePage: Screens("homepage")
    data object SettingsScreen: Screens("settings")
    data object WasteTracker: Screens("waste")
    data object LoyaltyProgram: Screens("loyalty")
    data object TipsPage: Screens("tips")
    data object DetailsTips: Screens("details")
    data object StatisticsScreen : Screens("stats")
    data object AdsScreen: Screens("ads")
    data object SignupPage: Screens("signup")
    data object LoginScreen: Screens("login")

}