package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.Currency
import com.example.data.model.Property
import com.example.ui.theme.*

@Composable
fun PropertyComparisonDialog(
    propertiesToCompare: List<Property>,
    currency: Currency,
    onDismiss: () -> Unit,
    onSelectProperty: (Property) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .testTag("property_comparison_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = LuxuryNavyDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Compare,
                            contentDescription = null,
                            tint = LuxuryGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Trophy Estate Comparison",
                            color = LuxuryGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Table
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                ) {
                    propertiesToCompare.forEach { prop ->
                        val pricePerSqft = if (prop.sqft > 0) prop.priceUsd / prop.sqft else 0.0

                        Column(
                            modifier = Modifier
                                .width(220.dp)
                                .padding(end = 12.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Image & Title
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = LuxurySurface)
                            ) {
                                Column {
                                    AsyncImage(
                                        model = prop.heroDrawableRes,
                                        contentDescription = prop.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                    )
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = prop.title,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = formatCurrencyPrice(prop.priceUsd, currency),
                                            color = LuxuryGold,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Comparison Attributes
                            ComparisonRow(label = "Type", value = prop.propertyType.displayName)
                            ComparisonRow(label = "Location", value = "${prop.city}, ${prop.country}")
                            ComparisonRow(label = "Bedrooms", value = "${prop.bedrooms} Suites")
                            ComparisonRow(label = "Bathrooms", value = "${prop.bathrooms} Baths")
                            ComparisonRow(label = "Interior Space", value = "${String.format("%,d", prop.sqft)} sqft")
                            ComparisonRow(
                                label = "Price / sqft",
                                value = "${currency.symbol}${String.format("%,.0f", pricePerSqft * currency.rateToUsd)}"
                            )
                            ComparisonRow(label = "Year Built", value = "${prop.yearBuilt}")
                            ComparisonRow(label = "Walk Score", value = "${prop.walkScore} / 100")

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Key Amenities",
                                color = LuxuryGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            prop.amenities.take(4).forEach { amenity ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = LuxuryGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = amenity.displayName,
                                        color = LuxuryTextSecondaryDark,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            LuxuryButton(
                                text = "View Estate",
                                onClick = {
                                    onDismiss()
                                    onSelectProperty(prop)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComparisonRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = label, color = LuxuryTextSecondaryDark, fontSize = 11.sp)
        Text(text = value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(color = LuxuryBorderDark.copy(alpha = 0.3f), modifier = Modifier.padding(top = 4.dp))
    }
}
