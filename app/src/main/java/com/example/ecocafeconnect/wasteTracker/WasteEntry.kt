package com.example.ecocafeconnect.wasteTracker

data class WasteEntry(
    var id: String = "",
    val date: String = "",
    val type: String = "",
    val amount: Double = 0.0
){
}