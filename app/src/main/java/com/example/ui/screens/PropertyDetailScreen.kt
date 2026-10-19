package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Currency
import com.example.data.model.Property
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun PropertyDetailScreen(
    property: Property,
    allProperties: List<Property>,
    currency: Currency,
    isWishlisted: Boolean,
    onBackClick: () -> Unit,
    onWishlistToggle: () -> Unit,
    on3DTourClick: () -> Unit,
    onScheduleVisitClick: () -> Unit,
    onMortgageCalcClick: () -> Unit,
    onSelectProperty: (Property) -> Unit,
    onBrochureDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedImageIndex by remember { mutableIntStateOf(0) }
    val galleryImages = listOfNotNull(property.heroDrawableRes, property.secondaryDrawableRes)

    val similarProperties = remember(property, allProperties) {
        allProperties.filter { it.id != property.id && (it.propertyType == property.propertyType || it.category == property.category) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LuxuryBlack)
            .testTag("property_detail_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            // 1. Gallery Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    val currentImg = if (galleryImages.isNotEmpty()) galleryImages[selectedImageIndex % galleryImages.size] else property.heroDrawableRes
                    AsyncImage(
                        model = currentImg,
                        contentDescription = property.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Scrims
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0x99000000), Color.Transparent, Color(0xCC080C14))
                                )
                            )
                    )

                    // Top action bar (Back, Share, Wishlist)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(42.dp)
                                .background(LuxuryBlack.copy(alpha = 0.7f), CircleShape)
                                .border(1.dp, LuxuryBorderDark, CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Share Button
                            IconButton(
                                onClick = {
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Explore ${property.title} in ${property.city} on Prime Estate: Exclusive Luxury Portfolio."
                                        )
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Estate"))
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(LuxuryBlack.copy(alpha = 0.7f), CircleShape)
                                    .border(1.dp, LuxuryBorderDark, CircleShape)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                            }

                            // Wishlist Button
                            IconButton(
                                onClick = onWishlistToggle,
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(LuxuryBlack.copy(alpha = 0.7f), CircleShape)
                                    .border(1.dp, LuxuryBorderDark, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Save",
                                    tint = if (isWishlisted) Color(0xFFEF4444) else Color.White
                                )
                            }
                        }
                    }

                    // 3D Virtual Tour floating banner
                    Surface(
                        color = LuxuryBlack.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .clickable { on3DTourClick() }
                            .testTag("launch_3d_tour_banner")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = LuxuryGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ENTER 3D TOUR",
                                color = LuxuryGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Image switcher indicator dots
                    if (galleryImages.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            galleryImages.indices.forEach { index ->
                                val isCur = index == selectedImageIndex
                                Box(
                                    modifier = Modifier
                                        .size(if (isCur) 10.dp else 6.dp)
                                        .background(if (isCur) LuxuryGold else Color.White.copy(alpha = 0.5f), CircleShape)
                                        .clickable { selectedImageIndex = index }
                                )
                            }
                        }
                    }
                }
            }

            // 2. Title, Price & Key Stats
            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        color = LuxuryGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = property.propertyType.displayName.uppercase(),
                            color = LuxuryGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = property.title,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = LuxuryElectricBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = property.address,
                            color = LuxuryTextSecondaryDark,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Price Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(text = "ACQUISITION PRICE", color = LuxuryTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = formatCurrencyPrice(property.priceUsd, currency),
                                color = LuxuryGold,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Calculator shortcut
                        Surface(
                            color = LuxurySurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
                            modifier = Modifier.clickable { onMortgageCalcClick() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Calculate, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("EMI / Loan", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Key Specs Card
                    LuxuryGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = LuxuryNavyDark
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            SpecItem(icon = Icons.Default.Bed, label = "Bedrooms", value = "${property.bedrooms}")
                            SpecItem(icon = Icons.Default.Bathtub, label = "Bathrooms", value = "${property.bathrooms}")
                            SpecItem(icon = Icons.Default.SquareFoot, label = "Area", value = "${String.format("%,d", property.sqft)} sqft")
                            SpecItem(icon = Icons.Default.CalendarToday, label = "Built", value = "${property.yearBuilt}")
                        }
                    }
                }
            }

            // 3. Narrative Architectural Description
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(text = "Architectural Synopsis", color = LuxuryGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = property.description,
                        color = LuxuryTextPrimaryDark,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            // 4. Amenities Grid
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(text = "Bespoke Amenities & Infrastructure", color = LuxuryGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        property.amenities.chunked(2).forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                pair.forEach { amenity ->
                                    Surface(
                                        color = LuxurySurface,
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = LuxuryGold,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = amenity.displayName,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                                if (pair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // 5. Neighborhood & Location Intelligence
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(text = "Location & Regional Intelligence", color = LuxuryGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    LuxuryGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = LuxuryNavyDark
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Walkability Index", color = LuxuryTextSecondaryDark, fontSize = 12.sp)
                                Text("${property.walkScore} / 100", color = LuxuryGold, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                Text("Walker's Paradise • Prime Enclave", color = LuxuryIceBlue, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Private Jet Access", color = LuxuryTextSecondaryDark, fontSize = 12.sp)
                                Text("18 Mins", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Dedicated FBO Helipad", color = LuxuryTextSecondaryDark, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // 6. Dedicated Senior Broker Card
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(text = "Dedicated Managing Broker", color = LuxuryGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    LuxuryGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = LuxurySurface
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(LuxuryGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = property.agent.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                                    color = LuxuryBlack,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(text = property.agent.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(text = property.agent.title, color = LuxuryGold, fontSize = 12.sp)
                                Text(text = "Closed: ${property.agent.volumeSold} in transactions", color = LuxuryTextSecondaryDark, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(text = property.agent.bio, color = LuxuryTextPrimaryDark, fontSize = 12.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onBrochureDownload,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Brochure", color = LuxuryGold, fontSize = 12.sp)
                            }

                            LuxuryButton(
                                text = "Schedule VIP Visit",
                                onClick = onScheduleVisitClick,
                                modifier = Modifier.weight(1.3f)
                            )
                        }
                    }
                }
            }

            // 7. Similar Trophy Properties Carousel
            item {
                Spacer(modifier = Modifier.height(24.dp))
                LuxurySectionHeader(
                    tag = "Exclusive Portfolio",
                    title = "Similar Residences",
                    subtitle = "Curated estates sharing comparable architectural pedigree"
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(similarProperties) { sim ->
                        LuxuryGlassCard(
                            modifier = Modifier
                                .width(260.dp)
                                .clickable { onSelectProperty(sim) },
                            backgroundColor = LuxurySurface
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = sim.heroDrawableRes,
                                    contentDescription = sim.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = sim.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(text = "${sim.city}, ${sim.country}", color = LuxuryTextSecondaryDark, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = formatCurrencyPrice(sim.priceUsd, currency), color = LuxuryGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Bottom Sticky Action Bar
        Surface(
            color = LuxuryNavyDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryBorderDark),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onMortgageCalcClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold)
                ) {
                    Text("EMI Calculator", color = LuxuryGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                LuxuryButton(
                    text = "Book Private Visit",
                    onClick = onScheduleVisitClick,
                    modifier = Modifier.weight(1.4f)
                )
            }
        }
    }
}

@Composable
private fun SpecItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = LuxuryTextSecondaryDark, fontSize = 11.sp)
    }
}
