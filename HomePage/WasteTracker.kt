package com.example.ecocafeconnect.Pages.HomePage

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthState
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.wasteTracker.WasteEntry
import com.example.ecocafeconnect.wasteTracker.WasteEntryListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WasteTracker(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: WasteEntryListViewModel,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authstate.observeAsState()
    val wasteEntries by viewModel.wasteEntries.observeAsState(listOf())
    val context = LocalContext.current

    var date by remember { mutableStateOf(LocalDate.now()) }
    var type by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) } // For dropdown menu

    val datePickerState = rememberDatePickerState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Waste Tracker", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Add New Waste Entry:", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        // Date Picker Input
        OutlinedTextField(
            value = "${date.monthValue}/${date.dayOfMonth}/${date.year}",
            onValueChange = { },
            label = { Text("Date") },
            modifier = Modifier.width(225.dp).height(75.dp),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                }
            },
            readOnly = true
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Waste Type Dropdown Menu
        Box(modifier = Modifier.width(225.dp).height(75.dp)) {
            OutlinedTextField(
                value = type,
                onValueChange = { },
                label = { Text("Type of Waste") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Select Type")
                    }
                },
                readOnly = true
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Plastic", "Coffee Grounds", "Food Waste").forEach { wasteType ->
                    DropdownMenuItem(
                        text = { Text(wasteType) },
                        onClick = {
                            type = wasteType
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount in kg") },
            modifier = Modifier.width(225.dp).height(75.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            keyboardActions = KeyboardActions(onDone = { /* optional */ })
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .width(180.dp)
                .height(50.dp),
            onClick = {
                val newWasteEntry = WasteEntry(
                    id = "",
                    date = "${date.monthValue}/${date.dayOfMonth}/${date.year}",
                    type = type,
                    amount = amount.toDoubleOrNull() ?: 0.0
                )
                viewModel.addWasteEntry(newWasteEntry)
                showToast = true
            }
        ) {
            Text(text = "Add Waste Entry", fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Waste Entry List:", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(5.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            items(wasteEntries) { wasteEntry ->
                WasteEntryItem(wasteEntry, onDelete = {
                    viewModel.deleteWasteEntry(wasteEntry.id)
                })
            }
        }

        if (showToast) {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Waste entry added successfully", Toast.LENGTH_SHORT).show()
                showToast = false
            }
        }
    }
}

@Composable
fun WasteEntryItem(wasteEntry: WasteEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Date: ${wasteEntry.date}", fontWeight = FontWeight.Bold)
                Text("Type: ${wasteEntry.type}")
                Text("Amount: ${wasteEntry.amount} kg")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete entry", tint = Color.Red)
            }
        }
    }
}
