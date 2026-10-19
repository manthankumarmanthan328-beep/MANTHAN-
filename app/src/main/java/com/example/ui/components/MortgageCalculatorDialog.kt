package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Currency
import com.example.data.model.Property
import com.example.ui.theme.*
import kotlin.math.pow

@Composable
fun MortgageCalculatorDialog(
    property: Property,
    currency: Currency,
    onDismiss: () -> Unit
) {
    val propertyPrice = property.priceUsd
    var downPaymentPercent by remember { mutableFloatStateOf(20f) }
    var interestRate by remember { mutableFloatStateOf(5.5f) }
    var tenureYears by remember { mutableIntStateOf(30) }

    val downPaymentAmount = propertyPrice * (downPaymentPercent / 100.0)
    val loanAmount = propertyPrice - downPaymentAmount

    // Monthly EMI formula: [P * r * (1 + r)^n] / [(1 + r)^n - 1]
    val monthlyInterestRate = (interestRate / 100.0) / 12.0
    val totalMonths = tenureYears * 12

    val monthlyPayment = if (loanAmount > 0 && monthlyInterestRate > 0) {
        val factor = (1 + monthlyInterestRate).pow(totalMonths.toDouble())
        (loanAmount * monthlyInterestRate * factor) / (factor - 1)
    } else {
        0.0
    }

    val estimatedMonthlyTax = (propertyPrice * 0.0125) / 12.0
    val estimatedMonthlyInsurance = (propertyPrice * 0.0035) / 12.0
    val totalMonthlyOutlay = monthlyPayment + estimatedMonthlyTax + estimatedMonthlyInsurance

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("mortgage_calc_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = LuxuryNavyDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = LuxuryGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mortgage & EMI",
                            color = LuxuryGold,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Text(
                    text = property.title,
                    color = LuxuryTextSecondaryDark,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Monthly Total Highlight Box
                LuxuryGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = LuxuryBlack.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "ESTIMATED TOTAL MONTHLY PAYMENT",
                        color = LuxuryTextSecondaryDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${formatCurrencyPrice(totalMonthlyOutlay, currency)} / mo",
                        color = LuxuryGold,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Breakdown bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight((monthlyPayment / totalMonthlyOutlay).toFloat())
                                .fillMaxHeight()
                                .background(LuxuryGold, RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        )
                        Box(
                            modifier = Modifier
                                .weight((estimatedMonthlyTax / totalMonthlyOutlay).toFloat())
                                .fillMaxHeight()
                                .background(LuxuryRoyalBlue)
                        )
                        Box(
                            modifier = Modifier
                                .weight((estimatedMonthlyInsurance / totalMonthlyOutlay).toFloat())
                                .fillMaxHeight()
                                .background(LuxuryIceBlue, RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "• P&I: ${formatCurrencyPrice(monthlyPayment, currency)}",
                            color = LuxuryGold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "• Tax: ${formatCurrencyPrice(estimatedMonthlyTax, currency)}",
                            color = LuxuryRoyalBlue,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "• Ins: ${formatCurrencyPrice(estimatedMonthlyInsurance, currency)}",
                            color = LuxuryIceBlue,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Down Payment Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Down Payment: ${downPaymentPercent.toInt()}%",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = formatCurrencyPrice(downPaymentAmount, currency),
                        color = LuxuryGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = downPaymentPercent,
                    onValueChange = { downPaymentPercent = it },
                    valueRange = 10f..50f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = LuxuryGold,
                        activeTrackColor = LuxuryGold,
                        inactiveTrackColor = LuxurySurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Interest Rate Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Interest Rate (Annual)",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${String.format("%.2f", interestRate)}%",
                        color = LuxuryGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = interestRate,
                    onValueChange = { interestRate = it },
                    valueRange = 3.0f..9.0f,
                    steps = 12,
                    colors = SliderDefaults.colors(
                        thumbColor = LuxuryGold,
                        activeTrackColor = LuxuryGold,
                        inactiveTrackColor = LuxurySurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Loan Term Selector
                Text(
                    text = "Loan Duration",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 20, 30).forEach { years ->
                        val isSelected = tenureYears == years
                        Surface(
                            color = if (isSelected) LuxuryGold else LuxurySurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) LuxuryGoldLight else Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { tenureYears = years }
                        ) {
                            Text(
                                text = "$years Years",
                                color = if (isSelected) LuxuryBlack else Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                LuxuryButton(
                    text = "Request Private Financing Quote",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
