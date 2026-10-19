package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.model.Currency
import com.example.data.model.UserRole
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NavTab
import com.example.ui.viewmodel.PrimeEstateViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: PrimeEstateViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                PrimeEstateApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimeEstateApp(
    viewModel: PrimeEstateViewModel,
    modifier: Modifier = Modifier
) {
    val properties by viewModel.properties.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val savedSearches by viewModel.savedSearches.collectAsStateWithLifecycle()
    val visitBookings by viewModel.visitBookings.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val activeCurrency by viewModel.activeCurrency.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val currentNavTab by viewModel.currentNavTab.collectAsStateWithLifecycle()
    val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val selectedProperty by viewModel.selectedProperty.collectAsStateWithLifecycle()
    val activeVirtualTourProperty by viewModel.activeVirtualTourProperty.collectAsStateWithLifecycle()
    val activeMortgageCalcProperty by viewModel.activeMortgageCalcProperty.collectAsStateWithLifecycle()
    val activeScheduleVisitProperty by viewModel.activeScheduleVisitProperty.collectAsStateWithLifecycle()
    val propertiesToCompare by viewModel.propertiesToCompare.collectAsStateWithLifecycle()
    val showAiConcierge by viewModel.showAiConcierge.collectAsStateWithLifecycle()
    val brochureMessage by viewModel.brochureDownloadMessage.collectAsStateWithLifecycle()
    val aiRecommendations by viewModel.aiRecommendations.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(brochureMessage) {
        brochureMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearBrochureMessage()
        }
    }

    // Handle Back Press appropriately
    BackHandler(enabled = activeVirtualTourProperty != null || selectedProperty != null || currentNavTab != NavTab.HOME) {
        when {
            activeVirtualTourProperty != null -> viewModel.dismiss3DTour()
            selectedProperty != null -> viewModel.clearSelectedProperty()
            currentNavTab != NavTab.HOME -> viewModel.setNavTab(NavTab.HOME)
        }
    }

    // If 3D Virtual Tour is active, display in immersive full-screen
    if (activeVirtualTourProperty != null) {
        Virtual3DTourViewer(
            property = activeVirtualTourProperty!!,
            onDismiss = { viewModel.dismiss3DTour() }
        )
        return
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("prime_estate_root"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, LuxuryGold, RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = R.drawable.img_prime_logo,
                                contentDescription = "Prime Estate Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PRIME ESTATE",
                                color = LuxuryGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "TROPHY RESIDENCES",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                },
                actions = {
                    // Currency Switcher pill
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable {
                                val nextCurrency = when (activeCurrency) {
                                    Currency.USD -> Currency.EUR
                                    Currency.EUR -> Currency.GBP
                                    Currency.GBP -> Currency.AED
                                    Currency.AED -> Currency.JPY
                                    Currency.JPY -> Currency.USD
                                }
                                viewModel.setCurrency(nextCurrency)
                            }
                            .padding(end = 6.dp)
                            .testTag("app_bar_currency_btn")
                    ) {
                        Text(
                            text = "${activeCurrency.symbol} ${activeCurrency.code}",
                            color = LuxuryGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // AI Concierge Magic Button
                    IconButton(
                        onClick = { viewModel.openAiConcierge() },
                        modifier = Modifier
                            .size(38.dp)
                            .background(LuxuryGold.copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, LuxuryGold, CircleShape)
                            .testTag("app_bar_ai_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Prime AI Concierge",
                            tint = LuxuryGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Role indicator badge (Customer, Broker, Admin)
                    if (currentUser.role == UserRole.ADMIN) {
                        Surface(
                            color = Color(0xFFDC2626),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(start = 6.dp, end = 12.dp)
                        ) {
                            Text(
                                text = "ADMIN",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (selectedProperty == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = LuxuryGold,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .border(
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Brush.verticalGradient(
                                    listOf(LuxuryBorderDark, Color.Transparent)
                                )
                            )
                        )
                        .testTag("prime_bottom_nav")
                ) {
                    val tabs = listOf(
                        NavTab.HOME to Icons.Default.Home,
                        NavTab.PROPERTIES to Icons.Default.Apartment,
                        NavTab.AGENTS to Icons.Default.People,
                        NavTab.DASHBOARD to Icons.Default.Dashboard,
                        NavTab.MORE to Icons.Default.Info
                    )

                    tabs.forEach { (tab, icon) ->
                        val isSelected = currentNavTab == tab
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            selected = isSelected,
                            onClick = { viewModel.setNavTab(tab) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = LuxuryBlack,
                                selectedTextColor = LuxuryGold,
                                indicatorColor = LuxuryGold,
                                unselectedIconColor = LuxuryTextSecondaryDark,
                                unselectedTextColor = LuxuryTextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen Content Switcher
            AnimatedContent(
                targetState = Pair(selectedProperty != null, currentNavTab),
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ScreenTransition"
            ) { (hasSelectedProp, tab) ->
                if (hasSelectedProp && selectedProperty != null) {
                    PropertyDetailScreen(
                        property = selectedProperty!!,
                        allProperties = properties,
                        currency = activeCurrency,
                        isWishlisted = wishlistIds.contains(selectedProperty!!.id),
                        onBackClick = { viewModel.clearSelectedProperty() },
                        onWishlistToggle = { viewModel.toggleWishlist(selectedProperty!!.id) },
                        on3DTourClick = { viewModel.start3DTour(selectedProperty!!) },
                        onScheduleVisitClick = { viewModel.openScheduleVisit(selectedProperty!!) },
                        onMortgageCalcClick = { viewModel.openMortgageCalc(selectedProperty!!) },
                        onSelectProperty = { viewModel.selectProperty(it) },
                        onBrochureDownload = { viewModel.downloadBrochure(selectedProperty!!) }
                    )
                } else {
                    when (tab) {
                        NavTab.HOME -> {
                            HomeScreen(
                                properties = properties,
                                aiRecommendations = aiRecommendations,
                                currency = activeCurrency,
                                wishlistIds = wishlistIds,
                                onPropertyClick = { viewModel.selectProperty(it) },
                                on3DTourClick = { viewModel.start3DTour(it) },
                                onWishlistToggle = { viewModel.toggleWishlist(it) },
                                onOpenAiConcierge = { viewModel.openAiConcierge() },
                                onNavigateCategory = { viewModel.navigateToCategory(it) },
                                onViewAllClick = { viewModel.setNavTab(NavTab.PROPERTIES) },
                                testimonials = viewModel.repository.testimonials,
                                companyStats = viewModel.repository.companyStats
                            )
                        }
                        NavTab.PROPERTIES -> {
                            PropertiesScreen(
                                properties = properties,
                                currency = activeCurrency,
                                wishlistIds = wishlistIds,
                                initialCategory = selectedCategoryFilter,
                                onPropertyClick = { viewModel.selectProperty(it) },
                                on3DTourClick = { viewModel.start3DTour(it) },
                                onWishlistToggle = { viewModel.toggleWishlist(it) },
                                onCompareClick = { viewModel.openComparison(it) },
                                onSaveSearch = { viewModel.saveSearch(it) }
                            )
                        }
                        NavTab.AGENTS -> {
                            AgentsScreen(
                                agents = viewModel.repository.getAgents(),
                                onContactAgent = { viewModel.openAiConcierge() }
                            )
                        }
                        NavTab.DASHBOARD -> {
                            DashboardScreen(
                                userProfile = currentUser,
                                properties = properties,
                                wishlistIds = wishlistIds,
                                visitBookings = visitBookings,
                                savedSearches = savedSearches,
                                aiRecommendations = aiRecommendations,
                                currency = activeCurrency,
                                onRoleChange = { viewModel.setUserRole(it) },
                                onPropertyClick = { viewModel.selectProperty(it) },
                                on3DTourClick = { viewModel.start3DTour(it) },
                                onWishlistToggle = { viewModel.toggleWishlist(it) },
                                onAddProperty = { viewModel.addProperty(it) },
                                onDeleteProperty = { viewModel.deleteProperty(it) },
                                onUpdateBookingStatus = { id, status -> viewModel.updateBookingStatus(id, status) }
                            )
                        }
                        NavTab.MORE -> {
                            MoreInfoScreen(
                                blogPosts = viewModel.repository.blogPosts,
                                testimonials = viewModel.repository.testimonials,
                                activeCurrency = activeCurrency,
                                isDarkTheme = isDarkTheme,
                                onCurrencyChange = { viewModel.setCurrency(it) },
                                onThemeToggle = { viewModel.toggleTheme() },
                                onOpenAiConcierge = { viewModel.openAiConcierge() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogs & Modal Sheets
    if (activeMortgageCalcProperty != null) {
        MortgageCalculatorDialog(
            property = activeMortgageCalcProperty!!,
            currency = activeCurrency,
            onDismiss = { viewModel.dismissMortgageCalc() }
        )
    }

    if (activeScheduleVisitProperty != null) {
        ScheduleVisitDialog(
            property = activeScheduleVisitProperty!!,
            onDismiss = { viewModel.dismissScheduleVisit() },
            onConfirmBooking = { booking ->
                viewModel.confirmBooking(booking)
            }
        )
    }

    if (propertiesToCompare.isNotEmpty()) {
        PropertyComparisonDialog(
            propertiesToCompare = propertiesToCompare,
            currency = activeCurrency,
            onDismiss = { viewModel.dismissComparison() },
            onSelectProperty = {
                viewModel.selectProperty(it)
            }
        )
    }

    if (showAiConcierge) {
        AiConciergeBottomSheet(
            properties = properties,
            currency = activeCurrency,
            aiService = viewModel.aiService,
            onDismiss = { viewModel.dismissAiConcierge() },
            onSelectProperty = {
                viewModel.selectProperty(it)
                viewModel.dismissAiConcierge()
            }
        )
    }
}
