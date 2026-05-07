package com.example.ecocafeconnect.Pages.HomePage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.wasteTracker.WasteEntry
import com.example.ecocafeconnect.wasteTracker.WasteEntryListViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Composable
fun TotalAmountGraph(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    averageAmount: Double
) {
    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(200.dp)) {
        val barWidth = 50.dp
        val barSpacing = 80.dp
        val maxAmount = maxOf(totalAmount, averageAmount)

        // Draw total amount label
        val totalAmountBarX = barSpacing.toPx() / 2
        drawContext.canvas.nativeCanvas.drawText(
            String.format("Total Amount: %.2f", totalAmount),
            totalAmountBarX,
            40f, // adjust y-coordinate to avoid overlap
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12.sp.toPx()
            }
        )

        // Draw total amount bar
        val totalAmountBarHeight = (totalAmount / maxAmount) * (size.height - 150)
        val totalAmountBarY = size.height - totalAmountBarHeight - 100

        // Define a gradient brush for the total amount bar
        val totalAmountGradient = Brush.linearGradient(
            colors = listOf(Color(0xFF3B0B59), Color(0xFF5C005C)), // Dark Violet gradient
            start = Offset(0f, 0f),
            end = Offset(size.width.toFloat(), 0f)
        )

        drawRect(
            brush = totalAmountGradient,
            topLeft = Offset(totalAmountBarX, totalAmountBarY.toFloat()),
            size = Size(barWidth.toPx(), totalAmountBarHeight.toFloat())
        )

        // Draw total amount label (again, below the bar)
        drawContext.canvas.nativeCanvas.drawText(
            "Total Amount",
            totalAmountBarX,
            size.height - 20,
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12.sp.toPx()
            }
        )

        // Draw average amount label
        val averageAmountBarX = (barWidth + barSpacing).toPx() + barSpacing.toPx() / 2
        drawContext.canvas.nativeCanvas.drawText(
            String.format("Average Amount: %.2f", averageAmount),
            averageAmountBarX,
            40f, // adjust y-coordinate to avoid overlap
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12.sp.toPx()
            }
        )

        // Draw average amount bar
        val averageAmountBarHeight = (averageAmount / maxAmount) * (size.height - 150)
        val averageAmountBarY = size.height - averageAmountBarHeight - 100

        // Define a gradient brush for the average amount bar
        val averageAmountGradient = Brush.linearGradient(
            colors = listOf(Color(0xFF4B0082), Color(0xFF6c5ce7)), // Indigo gradient
            start = Offset(0f, 0f),
            end = Offset(size.width.toFloat(), 0f)
        )

        drawRect(
            brush = averageAmountGradient,
            topLeft = Offset(averageAmountBarX, averageAmountBarY.toFloat()),
            size = Size(barWidth.toPx(), averageAmountBarHeight.toFloat())
        )

        // Draw average amount label (again, below the bar)
        drawContext.canvas.nativeCanvas.drawText(
            "Average Amount",
            averageAmountBarX,
            size.height - 20,
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12.sp.toPx()
            }
        )
    }
}

@Composable
fun MonthlyWasteGraph(
    modifier: Modifier = Modifier,
    monthlyWasteEntries: Map<String, List<WasteEntry>>
) {
    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(450.dp)) {
        val barWidth = 50.dp
        val barSpacing = 50.dp

        val monthlyAmounts = monthlyWasteEntries.mapValues { it.value.sumOf { it.amount } }
        val maxMonthlyAmount = monthlyAmounts.values.maxOfOrNull { it } ?: 0.0

        monthlyAmounts.toList().forEachIndexed { index, (month, amount) ->
            val barHeight = (amount / maxMonthlyAmount) * (size.height - 250)
            val barX = (index * (barWidth + barSpacing)).toPx() + (barSpacing.toPx() / 2)
            val barY = size.height - barHeight - 200

            // Define a gradient brush for the monthly waste bar
            val monthlyWasteGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFC7B8EA), Color(0xFF7A288A)), // Violet gradient
                start = Offset(0f, 0f),
                end = Offset(size.width.toFloat(), 0f)
            )

            drawRect(
                brush = monthlyWasteGradient,
                topLeft = Offset(barX, barY.toFloat()),
                size = Size(barWidth.toPx(), barHeight.toFloat())
            )

            // Draw amount label
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.2f", amount),
                barX,
                (barY - 30).toFloat(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                }
            )

            // Draw month label
            drawContext.canvas.nativeCanvas.drawText(
                month,
                barX,
                size.height - 120,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}

@Composable
fun TypeWasteGraph(
    modifier: Modifier = Modifier,
    typeWasteEntries: Map<String, List<WasteEntry>>
) {
    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(550.dp)) {
        val barWidth = 50.dp
        val barSpacing = 50.dp

        val typeAmounts = typeWasteEntries.mapValues { it.value.sumOf { it.amount } }
        val maxTypeAmount = typeAmounts.values.maxOfOrNull { it } ?: 0.0

        typeAmounts.toList().forEachIndexed { index, (type, amount) ->
            val barHeight = (amount / maxTypeAmount) * (size.height - 250)
            val barX = (index * (barWidth + barSpacing)).toPx() + (barSpacing.toPx() / 2)
            val barY = size.height - barHeight - 200

            // Define a gradient brush for the type waste bar
            val typeWasteGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFC7B8EA), Color(0xFF7A288A)), // Violet gradient
                start = Offset(0f, 0f),
                end = Offset(size.width.toFloat(), 0f)
            )

            drawRect(
                brush = typeWasteGradient,
                topLeft = Offset(barX, barY.toFloat()),
                size = Size(barWidth.toPx(), barHeight.toFloat())
            )

            // Draw amount label
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.2f", amount),
                barX,
                (barY - 30).toFloat(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                }
            )

            // Draw type label
            drawContext.canvas.nativeCanvas.drawText(
                type,
                barX,
                size.height - 120,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: WasteEntryListViewModel, authViewModel: AuthViewModel) {
    val authState by authViewModel.authstate.observeAsState()
    val wasteEntries by viewModel.wasteEntries.observeAsState(emptyList())
    val context = LocalContext.current

    // Compute statistics
    var totalAmount by remember { mutableStateOf(0.0) }
    var averageAmount by remember { mutableStateOf(0.0) }
    var topWasteType by remember { mutableStateOf("") }
    var topWasteAmount by remember { mutableStateOf(0.0) }
    var monthlyWasteEntries by remember { mutableStateOf<Map<String, List<WasteEntry>>>(mapOf()) }
    var typeWasteEntries by remember { mutableStateOf<Map<String, List<WasteEntry>>>(mapOf()) }

    LaunchedEffect(wasteEntries) {
        if (wasteEntries.isNotEmpty()) {
            totalAmount = wasteEntries.sumOf { it.amount }
            averageAmount = totalAmount / wasteEntries.size
            val topWasteEntry = wasteEntries.maxByOrNull { it.amount }
            topWasteType = topWasteEntry?.type ?: ""
            topWasteAmount = topWasteEntry?.amount ?: 0.0

            // Group waste entries by month and type
            val dateFormat = DateTimeFormatterBuilder()
                .appendPattern("M/d/yyyy") // Allows single digit for month and day
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1) // Default month if not provided
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1) // Default day if not provided
                .toFormatter() // Revised code
            monthlyWasteEntries = wasteEntries.groupBy {
                val date = LocalDate.parse(it.date, dateFormat)
                date.month.name
            }.mapValues { it.value }
            typeWasteEntries = wasteEntries.groupBy { it.type }.mapValues { it.value }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Padding around the box
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) // alternate color code, .background(color = Color(0xFFE6E6FA))
                    .padding(16.dp) // Inner padding for the content
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = " ")
                    Text(text = " ")
                    Text(text = " ")
                    Text(text = "Statistics", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Total Amount: ${String.format("%.2f", totalAmount)} kg", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Average Amount: ${String.format("%.2f", averageAmount)} kg", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Top Waste Type: $topWasteType", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Top Waste Amount: ${String.format("%.2f", topWasteAmount)} kg", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }


        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            TotalAmountGraph(
                modifier = Modifier.fillMaxWidth(),
                totalAmount = totalAmount,
                averageAmount = averageAmount
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                monthlyWasteEntries.forEach { (month, entries) ->
                    Text(text = "Monthly Waste for $month:")
                    MonthlyWasteGraph(
                        modifier = Modifier.fillMaxWidth(),
                        monthlyWasteEntries = mapOf(month to entries)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                typeWasteEntries.forEach { (type, entries) ->
                    Text(text = "Waste for $type:")
                    TypeWasteGraph(
                        modifier = Modifier.fillMaxWidth(),
                        typeWasteEntries = mapOf(type to entries)
                    )
                }
            }
        }
    }
}

