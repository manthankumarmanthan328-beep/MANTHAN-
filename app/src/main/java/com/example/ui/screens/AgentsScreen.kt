package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Agent
import com.example.ui.components.LuxuryButton
import com.example.ui.components.LuxuryGlassCard
import com.example.ui.components.LuxurySectionHeader
import com.example.ui.theme.*

@Composable
fun AgentsScreen(
    agents: List<Agent>,
    onContactAgent: (Agent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("agents_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            LuxurySectionHeader(
                tag = "Private Wealth Advisory",
                title = "Senior Managing Brokers",
                subtitle = "Licensed fiduciary partners orchestrating discreet acquisitions worldwide"
            )
        }

        items(agents) { agent ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                LuxuryGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = LuxuryNavyDark
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(LuxuryGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = agent.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                                color = LuxuryBlack,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = agent.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = agent.title,
                                color = LuxuryGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${agent.rating} (${agent.dealsClosed} closed)",
                                    color = LuxuryTextSecondaryDark,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = agent.bio,
                        color = LuxuryTextPrimaryDark,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stats summary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LuxurySurface, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Transaction Volume", color = LuxuryTextSecondaryDark, fontSize = 10.sp)
                            Text(agent.volumeSold, color = LuxuryGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Advisory Tier", color = LuxuryTextSecondaryDark, fontSize = 10.sp)
                            Text("Senior Partner", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact Action Row (Call, Email, WhatsApp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Phone Call
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${agent.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = LuxuryGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", color = Color.White, fontSize = 12.sp)
                        }

                        // WhatsApp
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${agent.whatsapp.replace("+", "")}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color(0xFF25D366), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", color = Color.White, fontSize = 12.sp)
                        }

                        // Private Consultation
                        LuxuryButton(
                            text = "Consult",
                            onClick = { onContactAgent(agent) },
                            modifier = Modifier.weight(1.2f)
                        )
                    }
                }
            }
        }
    }
}
