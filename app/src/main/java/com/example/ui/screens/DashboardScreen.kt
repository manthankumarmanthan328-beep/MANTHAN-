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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    properties: List<Property>,
    wishlistIds: Set<String>,
    visitBookings: List<VisitBooking>,
    savedSearches: List<SavedSearch>,
    aiRecommendations: List<AiRecommendationMatch>,
    currency: Currency,
    onRoleChange: (UserRole) -> Unit,
    onPropertyClick: (Property) -> Unit,
    on3DTourClick: (Property) -> Unit,
    onWishlistToggle: (String) -> Unit,
    onAddProperty: (Property) -> Unit,
    onDeleteProperty: (String) -> Unit,
    onUpdateBookingStatus: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreatePropertyDialog by remember { mutableStateOf(false) }

    val wishlistProperties = remember(properties, wishlistIds) {
        properties.filter { wishlistIds.contains(it.id) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen_feed"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. VIP Profile Header & Role Switcher
        item {
            LuxuryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                backgroundColor = LuxuryNavyDark
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(LuxuryGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                            color = LuxuryBlack,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.name,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.Verified, contentDescription = "VIP", tint = LuxuryGold, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = userProfile.membershipTier,
                            color = LuxuryGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = userProfile.email,
                            color = LuxuryTextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = LuxuryBorderDark.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                // Role Switcher Bar (Customer, Agent, Admin)
                Text(
                    text = "ACTIVE ACCESS ROLE (SECURITY CONTROL):",
                    color = LuxuryTextSecondaryDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UserRole.values().forEach { role ->
                        val isSelected = userProfile.role == role
                        Surface(
                            color = if (isSelected) LuxuryGold else LuxurySurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) LuxuryGoldLight else LuxuryBorderDark
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onRoleChange(role) }
                                .testTag("role_btn_${role.name}")
                        ) {
                            Text(
                                text = role.displayName,
                                color = if (isSelected) LuxuryBlack else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }

        // ================= ADMIN PANEL VIEW =================
        if (userProfile.role == UserRole.ADMIN) {
            item {
                LuxurySectionHeader(
                    tag = "Admin Control Center",
                    title = "Executive Management Console",
                    subtitle = "Platform governance, listing oversight, and portfolio analytics",
                    actionText = "+ Add Listing",
                    onActionClick = { showCreatePropertyDialog = true }
                )
            }

            // Key KPI Analytics
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LuxuryGlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = LuxurySurface
                    ) {
                        Text("Total Volume", color = LuxuryTextSecondaryDark, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("$4.80 Billion", color = LuxuryGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("+18.4% YoY Growth", color = Color(0xFF10B981), fontSize = 10.sp)
                    }

                    LuxuryGlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = LuxurySurface
                    ) {
                        Text("Active Listings", color = LuxuryTextSecondaryDark, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("${properties.size} Estates", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("100% Verified Escrow", color = LuxuryIceBlue, fontSize = 10.sp)
                    }
                }
            }

            // Booked VIP Visits Manager (Admin)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LuxurySectionHeader(
                    tag = "Client Concierge Bookings",
                    title = "Scheduled VIP Visits (${visitBookings.size})",
                    subtitle = "Manage private jet, chauffeur, and walkthrough appointments"
                )
            }

            items(visitBookings) { booking ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    LuxuryGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = LuxurySurface
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = booking.propertyTitle,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = if (booking.status.contains("Confirmed")) LuxuryGold.copy(alpha = 0.2f) else LuxuryRoyalBlue.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold)
                            ) {
                                Text(
                                    text = booking.status,
                                    color = LuxuryGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Client: ${booking.visitorName} • ${booking.visitorPhone}",
                            color = LuxuryTextSecondaryDark,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Time: ${booking.dateString} at ${booking.timeSlot}",
                            color = LuxuryIceBlue,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onUpdateBookingStatus(booking.id, "VIP Completed") },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Mark Completed", color = Color.White, fontSize = 11.sp)
                            }
                            Button(
                                onClick = { onUpdateBookingStatus(booking.id, "Chauffeur Dispatched") },
                                colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Dispatch Chauffeur", color = LuxuryBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Listing Management (Admin delete / review)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LuxurySectionHeader(
                    tag = "Catalog Administration",
                    title = "Manage Active Residences",
                    subtitle = "Review escrow status, edit pricing, or archive properties"
                )
            }

            items(properties) { prop ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Surface(
                        color = LuxuryNavyDark,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(prop.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("${prop.city} • ${formatCurrencyPrice(prop.priceUsd, currency)}", color = LuxuryGold, fontSize = 12.sp)
                            }
                            IconButton(
                                onClick = { onDeleteProperty(prop.id) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }

        // ================= CUSTOMER / AGENT DASHBOARD VIEW =================
        else {
            // Customer Wishlist Section
            item {
                LuxurySectionHeader(
                    tag = "Private Portfolio",
                    title = "Saved Estates (${wishlistProperties.size})",
                    subtitle = "Direct access to your favorited trophy properties"
                )
            }

            if (wishlistProperties.isEmpty()) {
                item {
                    Text(
                        text = "Your private portfolio is empty. Explore catalog and tap the heart icon to save residences.",
                        color = LuxuryTextSecondaryDark,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            } else {
                items(wishlistProperties) { prop ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        PropertyCard(
                            property = prop,
                            currency = currency,
                            isWishlisted = true,
                            onWishlistToggle = { onWishlistToggle(prop.id) },
                            onClick = { onPropertyClick(prop) },
                            on3DTourClick = { on3DTourClick(prop) }
                        )
                    }
                }
            }

            // Customer Scheduled Visits
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LuxurySectionHeader(
                    tag = "Calendar",
                    title = "My VIP Viewings (${visitBookings.size})",
                    subtitle = "Upcoming private escorted viewings"
                )
            }

            items(visitBookings) { booking ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    LuxuryGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = LuxurySurface
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(booking.propertyTitle, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Surface(
                                color = LuxuryGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold)
                            ) {
                                Text(
                                    text = booking.status,
                                    color = LuxuryGold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Date: ${booking.dateString} (${booking.timeSlot})", color = LuxuryIceBlue, fontSize = 12.sp)
                        if (booking.privateChauffeur) {
                            Text(text = "✓ Rolls-Royce Private Transfer Requested", color = LuxuryGold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Saved Searches
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LuxurySectionHeader(
                    tag = "Search Alerts",
                    title = "Saved Search Criteria (${savedSearches.size})",
                    subtitle = "Instant alert notifications for matching off-market drops"
                )
            }

            items(savedSearches) { search ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Surface(
                        color = LuxuryNavyDark,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(search.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Max Price: $${String.format("%,.0f", search.maxPrice)} • ${search.category.displayName}", color = LuxuryGold, fontSize = 12.sp)
                            }
                            Icon(Icons.Default.NotificationsActive, contentDescription = "Active alert", tint = LuxuryGold, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }

    // Admin Create Property Dialog
    if (showCreatePropertyDialog) {
        CreatePropertyDialog(
            onDismiss = { showCreatePropertyDialog = false },
            onSave = { newProp ->
                onAddProperty(newProp)
                showCreatePropertyDialog = false
            }
        )
    }
}

@Composable
private fun CreatePropertyDialog(
    onDismiss: () -> Unit,
    onSave: (Property) -> Unit
) {
    var title by remember { mutableStateOf("The Sovereign Crest Villa") }
    var city by remember { mutableStateOf("Monaco") }
    var address by remember { mutableStateOf("Avenue Princesse Grace, Monaco") }
    var priceStr by remember { mutableStateOf("38000000") }
    var bedsStr by remember { mutableStateOf("6") }
    var bathsStr by remember { mutableStateOf("8") }
    var sqftStr by remember { mutableStateOf("14500") }
    var category by remember { mutableStateOf(ListingCategory.LUXURY_VILLAS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LuxuryNavyDark,
        title = {
            Text("Publish New Luxury Listing", color = LuxuryGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Estate Title") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City / Enclave") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Price in USD") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bedsStr,
                        onValueChange = { bedsStr = it },
                        label = { Text("Beds") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = bathsStr,
                        onValueChange = { bathsStr = it },
                        label = { Text("Baths") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            LuxuryButton(
                text = "Publish Listing",
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 25000000.0
                    val beds = bedsStr.toIntOrNull() ?: 5
                    val baths = bathsStr.toIntOrNull() ?: 7
                    val sqft = sqftStr.toIntOrNull() ?: 12000

                    val newProperty = Property(
                        id = "prop_${System.currentTimeMillis()}",
                        title = title,
                        tagLine = "Newly Listed Trophy Architectural Residence",
                        priceUsd = price,
                        category = category,
                        propertyType = PropertyType.VILLA,
                        city = city,
                        address = address,
                        country = "Monaco",
                        bedrooms = beds,
                        bathrooms = baths,
                        sqft = sqft,
                        yearBuilt = 2026,
                        heroDrawableRes = R.drawable.img_hero_villa,
                        description = "An ultra-rare trophy acquisition newly added by Prime Estate Global Advisory.",
                        amenities = listOf(Amenity.INFINITY_POOL, Amenity.WINE_CELLAR, Amenity.SMART_HOME, Amenity.CONCIERGE),
                        agent = Agent("admin_agent", "Marcus Sterling", "Principal Broker", "+1 212 555 0100", "broker@primeestate.luxury", "+12125550100", 5.0f, 100, "$1.5B", "Advisory"),
                        tourRooms = emptyList(),
                        isFeatured = true
                    )
                    onSave(newProperty)
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}
