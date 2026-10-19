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
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesScreen(
    properties: List<Property>,
    currency: Currency,
    wishlistIds: Set<String>,
    initialCategory: ListingCategory = ListingCategory.ALL,
    onPropertyClick: (Property) -> Unit,
    on3DTourClick: (Property) -> Unit,
    onWishlistToggle: (String) -> Unit,
    onCompareClick: (List<Property>) -> Unit,
    onSaveSearch: (SavedSearch) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var searchQuery by remember { mutableStateOf("") }
    var minBedrooms by remember { mutableIntStateOf(0) }
    var maxPriceRange by remember { mutableFloatStateOf(100f) } // in Millions USD
    var selectedAmenity by remember { mutableStateOf<Amenity?>(null) }
    var sortByPriceDesc by remember { mutableStateOf(true) }
    var showFilterSheet by remember { mutableStateOf(false) }

    // Multi-select for side-by-side comparison (up to 3)
    var selectedForCompare by remember { mutableStateOf<Set<String>>(emptySet()) }

    val filteredList = remember(
        properties,
        selectedCategory,
        searchQuery,
        minBedrooms,
        maxPriceRange,
        selectedAmenity,
        sortByPriceDesc
    ) {
        val list = properties.filter { prop ->
            val matchesCategory = (selectedCategory == ListingCategory.ALL || prop.category == selectedCategory)
            val matchesSearch = searchQuery.isBlank() ||
                    prop.title.contains(searchQuery, ignoreCase = true) ||
                    prop.city.contains(searchQuery, ignoreCase = true) ||
                    prop.country.contains(searchQuery, ignoreCase = true)
            val matchesBeds = prop.bedrooms >= minBedrooms
            val matchesPrice = (prop.priceUsd / 1_000_000.0) <= maxPriceRange
            val matchesAmenity = selectedAmenity == null || prop.amenities.contains(selectedAmenity)

            matchesCategory && matchesSearch && matchesBeds && matchesPrice && matchesAmenity
        }

        if (sortByPriceDesc) {
            list.sortedByDescending { it.priceUsd }
        } else {
            list.sortedBy { it.priceUsd }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("properties_catalog_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Search & Filters Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LuxuryNavyDark)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search location, estate name...", color = LuxuryTextSecondaryDark) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = LuxuryGold)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuxuryGold,
                            unfocusedBorderColor = LuxuryBorderDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("properties_search_field")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Filter Button
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier
                            .size(48.dp)
                            .background(LuxurySurface, RoundedCornerShape(12.dp))
                            .border(1.dp, LuxuryGold, RoundedCornerShape(12.dp))
                            .testTag("filter_dialog_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filters",
                            tint = LuxuryGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Tabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ListingCategory.values()) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            color = if (isSelected) LuxuryGold else LuxurySurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) LuxuryGoldLight else LuxuryBorderDark
                            ),
                            modifier = Modifier
                                .clickable { selectedCategory = category }
                                .testTag("cat_tab_${category.name}")
                        ) {
                            Text(
                                text = category.displayName,
                                color = if (isSelected) LuxuryBlack else Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Results summary and sorting controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredList.size} Prime Residences Available",
                    color = LuxuryTextSecondaryDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (sortByPriceDesc) "Price: High to Low" else "Price: Low to High",
                        color = LuxuryGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { sortByPriceDesc = !sortByPriceDesc }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (sortByPriceDesc) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "Sort order",
                        tint = LuxuryGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Active filters indicators
            if (minBedrooms > 0 || maxPriceRange < 100f || selectedAmenity != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (minBedrooms > 0) {
                        Surface(
                            color = LuxurySurfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "$minBedrooms+ Beds",
                                color = LuxuryGold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    if (maxPriceRange < 100f) {
                        Surface(
                            color = LuxurySurfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Up to $${maxPriceRange.toInt()}M",
                                color = LuxuryGold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    if (selectedAmenity != null) {
                        Surface(
                            color = LuxurySurfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = selectedAmenity?.displayName ?: "",
                                color = LuxuryGold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Text(
                        text = "Reset",
                        color = LuxuryElectricBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                minBedrooms = 0
                                maxPriceRange = 100f
                                selectedAmenity = null
                            }
                            .padding(vertical = 3.dp)
                    )
                }
            }

            // Property Items List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("properties_list"),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (filteredList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = LuxuryTextSecondaryDark,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Estates Found",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try adjusting your price ceiling, bedrooms, or category filter.",
                                color = LuxuryTextSecondaryDark,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                items(filteredList) { property ->
                    val isCompared = selectedForCompare.contains(property.id)
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Column {
                            PropertyCard(
                                property = property,
                                currency = currency,
                                isWishlisted = wishlistIds.contains(property.id),
                                onWishlistToggle = { onWishlistToggle(property.id) },
                                onClick = { onPropertyClick(property) },
                                on3DTourClick = { on3DTourClick(property) }
                            )

                            // Quick Compare selection toggle bar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, end = 4.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        selectedForCompare = if (isCompared) {
                                            selectedForCompare - property.id
                                        } else {
                                            if (selectedForCompare.size < 3) selectedForCompare + property.id else selectedForCompare
                                        }
                                    }
                                ) {
                                    Checkbox(
                                        checked = isCompared,
                                        onCheckedChange = { checked ->
                                            selectedForCompare = if (checked) {
                                                if (selectedForCompare.size < 3) selectedForCompare + property.id else selectedForCompare
                                            } else {
                                                selectedForCompare - property.id
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = LuxuryGold)
                                    )
                                    Text(
                                        text = if (isCompared) "Selected for Compare" else "Compare Property",
                                        color = if (isCompared) LuxuryGold else LuxuryTextSecondaryDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Compare Bar (when 2 or more selected)
        if (selectedForCompare.size >= 2) {
            Surface(
                color = LuxuryNavyDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 85.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${selectedForCompare.size} Estates Selected",
                            color = LuxuryGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Compare side-by-side specifications",
                            color = LuxuryTextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }

                    Row {
                        TextButton(onClick = { selectedForCompare = emptySet() }) {
                            Text("Clear", color = Color.White, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        LuxuryButton(
                            text = "Compare Now",
                            onClick = {
                                val selectedObjects = properties.filter { selectedForCompare.contains(it.id) }
                                onCompareClick(selectedObjects)
                            }
                        )
                    }
                }
            }
        }

        // Advanced Filter Bottom Sheet Modal
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                containerColor = LuxuryNavyDark,
                dragHandle = { BottomSheetDefaults.DragHandle(color = LuxuryGold) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Advanced Search Filters",
                            color = LuxuryGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showFilterSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Price Slider
                    Text(
                        text = "Maximum Budget: $${maxPriceRange.toInt()} Million USD",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = maxPriceRange,
                        onValueChange = { maxPriceRange = it },
                        valueRange = 10f..100f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = LuxuryGold,
                            activeTrackColor = LuxuryGold,
                            inactiveTrackColor = LuxurySurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Minimum Bedrooms
                    Text(
                        text = "Minimum Bedrooms: ${if (minBedrooms == 0) "Any" else "$minBedrooms+ Beds"}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        listOf(0, 4, 6, 8).forEach { b ->
                            val isSel = minBedrooms == b
                            Surface(
                                color = if (isSel) LuxuryGold else LuxurySurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { minBedrooms = b }
                            ) {
                                Text(
                                    text = if (b == 0) "Any" else "$b+",
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

                    // Amenities Filter
                    Text(
                        text = "Must-Have Trophy Amenity",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(Amenity.values()) { a ->
                            val isSel = selectedAmenity == a
                            Surface(
                                color = if (isSel) LuxuryGold else LuxurySurfaceVariant,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable {
                                    selectedAmenity = if (isSel) null else a
                                }
                            ) {
                                Text(
                                    text = a.displayName,
                                    color = if (isSel) LuxuryBlack else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Save Search Button & Apply Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val search = SavedSearch(
                                    id = "search_${System.currentTimeMillis()}",
                                    title = "${selectedCategory.displayName} under $${maxPriceRange.toInt()}M",
                                    category = selectedCategory,
                                    maxPrice = maxPriceRange * 1_000_000.0,
                                    minBedrooms = minBedrooms
                                )
                                onSaveSearch(search)
                                showFilterSheet = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold)
                        ) {
                            Text("Save Search", color = LuxuryGold, fontSize = 13.sp)
                        }

                        LuxuryButton(
                            text = "Apply Filters",
                            onClick = { showFilterSheet = false },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
