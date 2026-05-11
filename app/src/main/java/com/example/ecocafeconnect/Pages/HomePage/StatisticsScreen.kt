package com.example.ecocafeconnect.Pages.HomePage

import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.wasteTracker.WasteEntry
import com.example.ecocafeconnect.wasteTracker.WasteEntryListViewModel
import java.time.LocalDate
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
        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 12.sp.toPx()
        }
        drawContext.canvas.nativeCanvas.drawText(
            String.format("Total Amount: %.2f", totalAmount),
            totalAmountBarX,
            40f,
            paint
        )

        // Draw total amount bar
        val graphHeight = size.height - 150
        val totalAmountBarHeight = if (maxAmount > 0) {
            ((totalAmount / maxAmount) * graphHeight).toFloat()
        } else 0f
        val totalAmountBarY = size.height - totalAmountBarHeight - 100

        val totalAmountGradient = Brush.linearGradient(
            colors = listOf(Color(0xFF3B0B59), Color(0xFF5C005C)),
            start = Offset(0f, 0f),
            end = Offset(size.width.toFloat(), 0f)
        )

        drawRect(
            brush = totalAmountGradient,
            topLeft = Offset(totalAmountBarX, totalAmountBarY),
            size = Size(barWidth.toPx(), totalAmountBarHeight)
        )

        // Draw total amount label below the bar
        drawContext.canvas.nativeCanvas.drawText(
            "Total Amount",
            totalAmountBarX,
            size.height - 20,
            paint
        )

        // Draw average amount label
        val averageAmountBarX = (barWidth.toPx() + barSpacing.toPx()) + (barSpacing.toPx() / 2)
        drawContext.canvas.nativeCanvas.drawText(
            String.format("Average Amount: %.2f", averageAmount),
            averageAmountBarX,
            40f,
            paint
        )

        // Draw average amount bar
        val averageAmountBarHeight = if (maxAmount > 0) {
            ((averageAmount / maxAmount) * graphHeight).toFloat()
        } else 0f
        val averageAmountBarY = size.height - averageAmountBarHeight - 100

        val averageAmountGradient = Brush.linearGradient(
            colors = listOf(Color(0xFF4B0082), Color(0xFF6c5ce7)),
            start = Offset(0f, 0f),
            end = Offset(size.width.toFloat(), 0f)
        )

        drawRect(
            brush = averageAmountGradient,
            topLeft = Offset(averageAmountBarX, averageAmountBarY),
            size = Size(barWidth.toPx(), averageAmountBarHeight)
        )

        // Draw average amount label below the bar
        drawContext.canvas.nativeCanvas.drawText(
            "Average Amount",
            averageAmountBarX,
            size.height - 20,
            paint
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

        val monthlyAmounts = monthlyWasteEntries.mapValues { it.value.sumOf { entry -> entry.amount } }
        val maxMonthlyAmount = monthlyAmounts.values.maxOrNull() ?: 0.0

        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 12.sp.toPx()
        }

        monthlyAmounts.toList().forEachIndexed { index, (month, amount) ->
            val graphHeight = size.height - 250
            val barHeight = if (maxMonthlyAmount > 0) {
                ((amount / maxMonthlyAmount) * graphHeight).toFloat()
            } else 0f
            // FIXED: Line 162 - Properly convert index to Float
            val barX = (index.toFloat() * (barWidth.toPx() + barSpacing.toPx())) + (barSpacing.toPx() / 2)
            val barY = size.height - barHeight - 200

            val monthlyWasteGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFC7B8EA), Color(0xFF7A288A)),
                start = Offset(0f, 0f),
                end = Offset(size.width.toFloat(), 0f)
            )

            drawRect(
                brush = monthlyWasteGradient,
                topLeft = Offset(barX, barY),
                size = Size(barWidth.toPx(), barHeight)
            )

            // Draw amount label
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.2f", amount),
                barX,
                (barY - 30).toFloat(),
                paint
            )

            // Draw month label
            drawContext.canvas.nativeCanvas.drawText(
                month,
                barX,
                size.height - 120,
                paint
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

        val typeAmounts = typeWasteEntries.mapValues { it.value.sumOf { entry -> entry.amount } }
        val maxTypeAmount = typeAmounts.values.maxOrNull() ?: 0.0

        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 12.sp.toPx()
        }

        typeAmounts.toList().forEachIndexed { index, (type, amount) ->
            val graphHeight = size.height - 250
            val barHeight = if (maxTypeAmount > 0) {
                ((amount / maxTypeAmount) * graphHeight).toFloat()
            } else 0f
            // FIXED: Line 221 - Properly convert index to Float
            val barX = (index.toFloat() * (barWidth.toPx() + barSpacing.toPx())) + (barSpacing.toPx() / 2)
            val barY = size.height - barHeight - 200

            val typeWasteGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFC7B8EA), Color(0xFF7A288A)),
                start = Offset(0f, 0f),
                end = Offset(size.width.toFloat(), 0f)
            )

            drawRect(
                brush = typeWasteGradient,
                topLeft = Offset(barX, barY),
                size = Size(barWidth.toPx(), barHeight)
            )

            // Draw amount label
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.2f", amount),
                barX,
                (barY - 30).toFloat(),
                paint
            )

            // Draw type label
            drawContext.canvas.nativeCanvas.drawText(
                type,
                barX,
                size.height - 120,
                paint
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: WasteEntryListViewModel,
    authViewModel: AuthViewModel
) {
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
                .appendPattern("M/d/yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter()
            monthlyWasteEntries = wasteEntries.groupBy {
                try {
                    val date = LocalDate.parse(it.date, dateFormat)
                    date.month.name
                } catch (e: Exception) {
                    "Unknown"
                }
            }.mapValues { it.value }

            typeWasteEntries = wasteEntries.groupBy { it.type }.mapValues { it.value }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                    Spacer(modifier = Modifier.height(10.dp))

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
                if (monthlyWasteEntries.isNotEmpty()) {
                    monthlyWasteEntries.forEach { (month, entries) ->
                        Text(text = "Monthly Waste for $month:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        MonthlyWasteGraph(
                            modifier = Modifier.fillMaxWidth(),
                            monthlyWasteEntries = mapOf(month to entries)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    Text(text = "No monthly waste data available", textAlign = TextAlign.Center)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (typeWasteEntries.isNotEmpty()) {
                    typeWasteEntries.forEach { (type, entries) ->
                        Text(text = "Waste for $type:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        TypeWasteGraph(
                            modifier = Modifier.fillMaxWidth(),
                            typeWasteEntries = mapOf(type to entries)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    Text(text = "No waste type data available", textAlign = TextAlign.Center)
                }
            }
        }
    }
}