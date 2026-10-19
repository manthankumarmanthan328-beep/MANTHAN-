package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BlogPost
import com.example.data.model.Currency
import com.example.data.model.Testimonial
import com.example.ui.components.LuxuryButton
import com.example.ui.components.LuxuryGlassCard
import com.example.ui.components.LuxurySectionHeader
import com.example.ui.theme.*

@Composable
fun MoreInfoScreen(
    blogPosts: List<BlogPost>,
    testimonials: List<Testimonial>,
    activeCurrency: Currency,
    isDarkTheme: Boolean,
    onCurrencyChange: (Currency) -> Unit,
    onThemeToggle: () -> Unit,
    onOpenAiConcierge: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedFaqIndex by remember { mutableIntStateOf(-1) }
    var contactName by remember { mutableStateOf("") }
    var contactMessage by remember { mutableStateOf("") }
    var inquirySubmitted by remember { mutableStateOf(false) }

    val faqs = listOf(
        "How does Prime Estate guarantee client privacy & discretion?" to
                "All acquisitions can be structured through sovereign trust entities, offshore family offices, or private LLCs. We mandate strict NDA protocols prior to revealing off-market architectural plans or owner identities.",
        "What is the process for scheduling a 3D tour or private jet viewing?" to
                "Clients can utilize our in-app interactive 360° virtual tour at any time. For on-site viewings, our VIP Concierge arranges Rolls-Royce chauffeur transfer or private helipad clearance directly to the estate grounds.",
        "How does the AI Recommendation Engine personalize estate discovery?" to
                "Our neural recommendation matrix evaluates architectural preferences (ceiling heights, waterfront berths, smart automation) alongside capital allocations and viewing history to surface high-pedigree matches with minimal browsing friction.",
        "Can foreign non-resident nationals purchase these luxury estates?" to
                "Yes. Our in-house international legal advisory coordinates golden visa residency programs, sovereign title deed registration, and cross-border multi-currency transactions in USD, EUR, GBP, and AED."
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("more_info_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. Settings: Currency & Appearance
        item {
            LuxurySectionHeader(
                tag = "VIP Preferences",
                title = "Global Settings & Currency",
                subtitle = "Select valuation currency and display preferences"
            )

            LuxuryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                backgroundColor = LuxuryNavyDark
            ) {
                // Currency Switcher
                Text(
                    text = "VALUATION CURRENCY",
                    color = LuxuryTextSecondaryDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Currency.values().forEach { cur ->
                        val isSel = cur == activeCurrency
                        Surface(
                            color = if (isSel) LuxuryGold else LuxurySurface,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) LuxuryGoldLight else LuxuryBorderDark
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onCurrencyChange(cur) }
                                .testTag("currency_${cur.code}")
                        ) {
                            Text(
                                text = cur.code,
                                color = if (isSel) LuxuryBlack else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = LuxuryBorderDark.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(14.dp))

                // Theme Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = LuxuryGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isDarkTheme) "Luxury Dark Theme (Active)" else "Classic Ivory Theme (Active)",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Onyx Black, Gold, and Royal Blue palette",
                                color = LuxuryTextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeToggle() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LuxuryGold,
                            checkedTrackColor = LuxurySurfaceVariant
                        )
                    )
                }
            }
        }

        // 2. About Prime Estate
        item {
            Spacer(modifier = Modifier.height(20.dp))
            LuxurySectionHeader(
                tag = "Heritage & Vision",
                title = "About Prime Estate",
                subtitle = "The world's preeminent boutique real estate advisory"
            )

            LuxuryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                backgroundColor = LuxurySurface
            ) {
                Text(
                    text = "Founded on the principles of architectural veneration and unwavering client fiduciary duty, Prime Estate represents the benchmark in trophy asset transactions. Operating across private offices in Beverly Hills, Manhattan, London, Dubai, and Monaco, we bridge visionary architects with the world's most discerning families.",
                    color = LuxuryTextPrimaryDark,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OfficeBadge("New York", "57th St")
                    OfficeBadge("London", "Mayfair")
                    OfficeBadge("Dubai", "DIFC")
                    OfficeBadge("Monaco", "Monte-Carlo")
                }
            }
        }

        // 3. Market Intelligence Blog
        item {
            Spacer(modifier = Modifier.height(20.dp))
            LuxurySectionHeader(
                tag = "Thought Leadership",
                title = "Market Intelligence Reports",
                subtitle = "Deep dives into global capital flows and architectural frontiers"
            )
        }

        items(blogPosts) { post ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                LuxuryGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = LuxuryNavyDark
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = LuxuryGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = post.category.uppercase(),
                                color = LuxuryGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(post.readTime, color = LuxuryTextSecondaryDark, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = post.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = post.excerpt,
                        color = LuxuryTextSecondaryDark,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "By ${post.author} • ${post.date}",
                        color = LuxuryIceBlue,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 4. Expandable FAQ Section
        item {
            Spacer(modifier = Modifier.height(20.dp))
            LuxurySectionHeader(
                tag = "Client Inquiries",
                title = "Frequently Asked Questions",
                subtitle = "Everything regarding our legal, escrow, and viewing protocols"
            )
        }

        items(faqs.indices.toList()) { index ->
            val (question, answer) = faqs[index]
            val isExpanded = expandedFaqIndex == index

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Surface(
                    color = LuxuryNavyDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedFaqIndex = if (isExpanded) -1 else index
                        }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = question,
                                color = if (isExpanded) LuxuryGold else Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = LuxuryGold
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                HorizontalDivider(color = LuxuryBorderDark.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = answer,
                                    color = LuxuryTextPrimaryDark,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Contact Advisory Desk
        item {
            Spacer(modifier = Modifier.height(24.dp))
            LuxurySectionHeader(
                tag = "Confidential Contact",
                title = "Private Advisory Desk",
                subtitle = "Submit an encrypted inquiry for confidential consultations"
            )

            LuxuryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                backgroundColor = LuxuryNavyDark
            ) {
                if (inquirySubmitted) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(44.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Inquiry Transmitted Securely", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "A Managing Partner will reach out via end-to-end encrypted channel within 2 hours.",
                            color = LuxuryTextSecondaryDark,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = contactName,
                        onValueChange = { contactName = it },
                        placeholder = { Text("Your Full Name or Representative Title") },
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
                        value = contactMessage,
                        onValueChange = { contactMessage = it },
                        placeholder = { Text("Describe specific acquisition interest or requirements...") },
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuxuryGold,
                            unfocusedBorderColor = LuxuryBorderDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LuxuryButton(
                        text = "Send Encrypted Message",
                        onClick = { inquirySubmitted = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun OfficeBadge(city: String, address: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(LuxuryNavyDark, CircleShape)
                .border(1.dp, LuxuryGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.LocationCity, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(city, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(address, color = LuxuryTextSecondaryDark, fontSize = 10.sp)
    }
}
