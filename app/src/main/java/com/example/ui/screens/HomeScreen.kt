package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    properties: List<Property>,
    aiRecommendations: List<AiRecommendationMatch>,
    currency: Currency,
    wishlistIds: Set<String>,
    onPropertyClick: (Property) -> Unit,
    on3DTourClick: (Property) -> Unit,
    onWishlistToggle: (String) -> Unit,
    onOpenAiConcierge: () -> Unit,
    onNavigateCategory: (ListingCategory) -> Unit,
    onViewAllClick: () -> Unit,
    testimonials: List<Testimonial>,
    companyStats: List<CompanyStat>,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ListingCategory.ALL) }

    val filteredFeatured = remember(properties, selectedCategory, searchQuery) {
        properties.filter { p ->
            val matchesCategory = (selectedCategory == ListingCategory.ALL || p.category == selectedCategory)
            val matchesSearch = searchQuery.isBlank() ||
                    p.title.contains(searchQuery, ignoreCase = true) ||
                    p.city.contains(searchQuery, ignoreCase = true) ||
                    p.country.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val globalLocations = listOf(
        "Beverly Hills" to "14 Trophy Villas",
        "New York" to "22 Sky Penthouses",
        "Miami Beach" to "9 Waterfront Estates",
        "London" to "16 Commercial & Mansions",
        "Dubai" to "18 Royal Palaces",
        "Monaco" to "7 Riviera Compounds"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_feed"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Full-Bleed Animated Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                AsyncImage(
                    model = R.drawable.img_hero_villa,
                    contentDescription = "Prime Estate Luxury Villa",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dramatic Dark & Gold Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x99080C14),
                                    Color(0x44080C14),
                                    Color(0xF0080C14)
                                )
                            )
                        )
                )

                // Hero Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        color = LuxuryBlack.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold)
                    ) {
                        Text(
                            text = "PRIME ESTATE • PRIVATE CLIENT ADVISORY",
                            color = LuxuryGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Architectural Brilliance.\nUnrivaled Prestige.",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Curating the globe's rarest trophy residences, waterfront sanctuaries, and Grade-A institutional towers.",
                        color = LuxuryTextSecondaryDark,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Smart Search Bar
                    Surface(
                        color = LuxuryNavyDark.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = LuxuryGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = {
                                    Text(
                                        "Search city, architect, or villa...",
                                        color = LuxuryTextSecondaryDark,
                                        fontSize = 13.sp
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Company Stats Counter Row
        item {
            LuxuryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                backgroundColor = LuxuryNavyDark
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    companyStats.forEach { stat ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            AnimatedCounterText(
                                targetNumber = stat.targetNumber,
                                prefix = stat.prefix,
                                suffix = stat.suffix
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stat.label,
                                color = LuxuryTextSecondaryDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // 3. AI Property Recommendation Engine Section
        item {
            LuxurySectionHeader(
                tag = "AI Portfolio Intelligence",
                title = "Curated For Your Profile",
                subtitle = "Algorithmic recommendations matched against your search & architectural taste",
                actionText = "Ask Prime AI",
                onActionClick = onOpenAiConcierge
            )

            // AI Match Card Carousel
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(aiRecommendations) { match ->
                    LuxuryGlassCard(
                        modifier = Modifier
                            .width(300.dp)
                            .clickable { onPropertyClick(match.property) }
                            .testTag("ai_match_card_${match.property.id}"),
                        backgroundColor = LuxurySurface
                    ) {
                        // AI Match Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = LuxuryRoyalBlue,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${match.matchPercentage}% AI Match",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Text(
                                text = formatCurrencyPrice(match.property.priceUsd, currency),
                                color = LuxuryGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Property mini preview
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = match.property.heroDrawableRes,
                                contentDescription = match.property.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = match.property.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = match.matchReason,
                            color = LuxuryTextSecondaryDark,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Match Signals Pills
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            match.matchedSignals.take(2).forEach { signal ->
                                Surface(
                                    color = LuxuryBlack.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = signal,
                                        color = LuxuryGoldLight,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Listing Categories Chips
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LuxurySectionHeader(
                tag = "Explore Catalog",
                title = "Trophy Categories",
                actionText = "View All",
                onActionClick = onViewAllClick
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ListingCategory.values()) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        color = if (isSelected) LuxuryGold else LuxuryNavyDark,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) LuxuryGoldLight else LuxuryBorderDark
                        ),
                        modifier = Modifier
                            .clickable {
                                selectedCategory = cat
                                onNavigateCategory(cat)
                            }
                            .testTag("category_chip_${cat.name}")
                    ) {
                        Text(
                            text = cat.displayName,
                            color = if (isSelected) LuxuryBlack else Color.White,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // 5. Featured Properties Feed
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(filteredFeatured) { prop ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                PropertyCard(
                    property = prop,
                    currency = currency,
                    isWishlisted = wishlistIds.contains(prop.id),
                    onWishlistToggle = { onWishlistToggle(prop.id) },
                    onClick = { onPropertyClick(prop) },
                    on3DTourClick = { on3DTourClick(prop) }
                )
            }
        }

        // 6. Global Luxury Capitals Carousel
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LuxurySectionHeader(
                tag = "International Footprint",
                title = "Prime Global Capitals",
                subtitle = "Active private treaty offices in financial hubs"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(globalLocations) { (city, listingsCount) ->
                    Surface(
                        color = LuxuryNavyDark,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
                        modifier = Modifier
                            .width(180.dp)
                            .clickable { searchQuery = city }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationCity,
                                    contentDescription = null,
                                    tint = LuxuryGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = city,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = listingsCount,
                                color = LuxuryTextSecondaryDark,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // 7. Testimonials & Client Reviews
        item {
            Spacer(modifier = Modifier.height(20.dp))
            LuxurySectionHeader(
                tag = "Client Endorsements",
                title = "Discreet Advisory",
                subtitle = "Trusted by sovereign funds, family offices, and tech visionaries"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(testimonials) { test ->
                    LuxuryGlassCard(
                        modifier = Modifier.width(310.dp),
                        backgroundColor = LuxurySurface
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = LuxuryGold,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "\"${test.quote}\"",
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = test.clientName,
                            color = LuxuryGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${test.title} • ${test.city}",
                            color = LuxuryTextSecondaryDark,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Acquired: ${test.propertyAcquired}",
                            color = LuxuryIceBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 8. Luxury Call To Action
        item {
            Spacer(modifier = Modifier.height(24.dp))
            LuxuryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                backgroundColor = LuxuryNavyDark
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(LuxuryGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = LuxuryGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Discreet Off-Market Portfolio Access",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Over 60% of our ultra-luxury transactions occur off-market. Connect with a licensed Senior Broker for confidential opportunities.",
                        color = LuxuryTextSecondaryDark,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LuxuryButton(
                        text = "Consult Senior Broker",
                        onClick = onOpenAiConcierge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
