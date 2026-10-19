package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.WineBar
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
import com.example.data.model.Property
import com.example.data.model.VisitBooking
import com.example.ui.theme.*

@Composable
fun ScheduleVisitDialog(
    property: Property,
    onDismiss: () -> Unit,
    onConfirmBooking: (VisitBooking) -> Unit
) {
    var selectedDate by remember { mutableStateOf("Tomorrow (2:00 PM)") }
    var selectedSlot by remember { mutableStateOf("Private Sunset Viewing (5:00 PM)") }
    var visitorName by remember { mutableStateOf("Lord Manthan Sterling") }
    var visitorPhone by remember { mutableStateOf("+1 (800) 774-6337") }
    var chauffeurChecked by remember { mutableStateOf(true) }
    var champagneChecked by remember { mutableStateOf(true) }
    var specialRequests by remember { mutableStateOf("Discreet private security escort requested.") }

    val dateOptions = listOf("Tomorrow", "This Saturday", "Next Tuesday", "Custom Date")
    val timeSlots = listOf("Morning (10:30 AM)", "Afternoon (2:00 PM)", "Twilight Sunset (5:30 PM)")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("schedule_visit_dialog"),
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
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = LuxuryGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Schedule VIP Visit",
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

                // Date Selection Chips
                Text(
                    text = "Select Preferred Date",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    dateOptions.take(3).forEach { d ->
                        val isSel = selectedDate.startsWith(d)
                        Surface(
                            color = if (isSel) LuxuryGold else LuxurySurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) LuxuryGoldLight else Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedDate = d }
                        ) {
                            Text(
                                text = d,
                                color = if (isSel) LuxuryBlack else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Time Slots
                Text(
                    text = "Viewing Time Slot",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    timeSlots.forEach { slot ->
                        val isSel = selectedSlot == slot
                        Surface(
                            color = if (isSel) LuxuryGold.copy(alpha = 0.2f) else LuxurySurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) LuxuryGold else LuxuryBorderDark
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSlot = slot }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSel,
                                    onClick = { selectedSlot = slot },
                                    colors = RadioButtonDefaults.colors(selectedColor = LuxuryGold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = slot,
                                    color = if (isSel) LuxuryGold else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // VIP Amenities Concierge Options
                LuxuryGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = LuxuryBlack.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "VIP CONCIERGE ADD-ONS",
                        color = LuxuryGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = LuxuryGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Rolls-Royce Chauffeur Transfer",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = chauffeurChecked,
                            onCheckedChange = { chauffeurChecked = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuxuryGold, checkedTrackColor = LuxurySurfaceVariant)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WineBar,
                                contentDescription = null,
                                tint = LuxuryGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dom Pérignon Champagne Reception",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = champagneChecked,
                            onCheckedChange = { champagneChecked = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuxuryGold, checkedTrackColor = LuxurySurfaceVariant)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = visitorName,
                    onValueChange = { visitorName = it },
                    label = { Text("Client Full Name / Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LuxuryGold,
                        unfocusedBorderColor = LuxuryBorderDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = visitorPhone,
                    onValueChange = { visitorPhone = it },
                    label = { Text("Private Direct Phone") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LuxuryGold,
                        unfocusedBorderColor = LuxuryBorderDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                LuxuryButton(
                    text = "Confirm VIP Appointment",
                    onClick = {
                        val booking = VisitBooking(
                            id = "booking_${System.currentTimeMillis()}",
                            propertyId = property.id,
                            propertyTitle = property.title,
                            dateString = selectedDate,
                            timeSlot = selectedSlot,
                            visitorName = visitorName,
                            visitorEmail = "vip.client@primeestate.luxury",
                            visitorPhone = visitorPhone,
                            privateChauffeur = chauffeurChecked,
                            champagneReception = champagneChecked,
                            status = "Confirmed VIP",
                            notes = specialRequests
                        )
                        onConfirmBooking(booking)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
