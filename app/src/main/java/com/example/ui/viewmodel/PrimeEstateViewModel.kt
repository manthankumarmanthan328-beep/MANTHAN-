package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiRecommendationService
import com.example.data.model.*
import com.example.data.repository.PropertyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NavTab(val title: String, val iconName: String) {
    HOME("Home", "home"),
    PROPERTIES("Estates", "apartment"),
    AGENTS("Brokers", "person"),
    DASHBOARD("Portfolio", "dashboard"),
    MORE("Advisory", "menu")
}

class PrimeEstateViewModel(
    val repository: PropertyRepository = PropertyRepository(),
    val aiService: AiRecommendationService = AiRecommendationService()
) : ViewModel() {

    val properties: StateFlow<List<Property>> = repository.properties
    val wishlistIds: StateFlow<Set<String>> = repository.wishlistIds
    val viewedPropertyIds: StateFlow<List<String>> = repository.viewedPropertyIds
    val savedSearches: StateFlow<List<SavedSearch>> = repository.savedSearches
    val visitBookings: StateFlow<List<VisitBooking>> = repository.visitBookings
    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val activeCurrency: StateFlow<Currency> = repository.activeCurrency
    val isDarkTheme: StateFlow<Boolean> = repository.isDarkTheme

    // Navigation and Modal States
    private val _currentNavTab = MutableStateFlow(NavTab.HOME)
    val currentNavTab: StateFlow<NavTab> = _currentNavTab.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow(ListingCategory.ALL)
    val selectedCategoryFilter: StateFlow<ListingCategory> = _selectedCategoryFilter.asStateFlow()

    private val _selectedProperty = MutableStateFlow<Property?>(null)
    val selectedProperty: StateFlow<Property?> = _selectedProperty.asStateFlow()

    private val _activeVirtualTourProperty = MutableStateFlow<Property?>(null)
    val activeVirtualTourProperty: StateFlow<Property?> = _activeVirtualTourProperty.asStateFlow()

    private val _activeMortgageCalcProperty = MutableStateFlow<Property?>(null)
    val activeMortgageCalcProperty: StateFlow<Property?> = _activeMortgageCalcProperty.asStateFlow()

    private val _activeScheduleVisitProperty = MutableStateFlow<Property?>(null)
    val activeScheduleVisitProperty: StateFlow<Property?> = _activeScheduleVisitProperty.asStateFlow()

    private val _propertiesToCompare = MutableStateFlow<List<Property>>(emptyList())
    val propertiesToCompare: StateFlow<List<Property>> = _propertiesToCompare.asStateFlow()

    private val _showAiConcierge = MutableStateFlow(false)
    val showAiConcierge: StateFlow<Boolean> = _showAiConcierge.asStateFlow()

    private val _brochureDownloadMessage = MutableStateFlow<String?>(null)
    val brochureDownloadMessage: StateFlow<String?> = _brochureDownloadMessage.asStateFlow()

    // Dynamic AI Recommendations Flow computed from user signals
    val aiRecommendations: StateFlow<List<AiRecommendationMatch>> = combine(
        properties,
        viewedPropertyIds,
        wishlistIds,
        savedSearches
    ) { props, viewed, wishlist, searches ->
        aiService.computePersonalizedRecommendations(props, viewed, wishlist, searches)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setNavTab(tab: NavTab) {
        _currentNavTab.value = tab
        if (tab != NavTab.PROPERTIES) {
            _selectedProperty.value = null
        }
    }

    fun navigateToCategory(category: ListingCategory) {
        _selectedCategoryFilter.value = category
        _currentNavTab.value = NavTab.PROPERTIES
        _selectedProperty.value = null
    }

    fun selectProperty(property: Property) {
        _selectedProperty.value = property
        repository.recordPropertyView(property.id)
    }

    fun clearSelectedProperty() {
        _selectedProperty.value = null
    }

    fun toggleWishlist(propertyId: String) {
        repository.toggleWishlist(propertyId)
    }

    fun start3DTour(property: Property) {
        _activeVirtualTourProperty.value = property
        repository.recordPropertyView(property.id)
    }

    fun dismiss3DTour() {
        _activeVirtualTourProperty.value = null
    }

    fun openMortgageCalc(property: Property) {
        _activeMortgageCalcProperty.value = property
    }

    fun dismissMortgageCalc() {
        _activeMortgageCalcProperty.value = null
    }

    fun openScheduleVisit(property: Property) {
        _activeScheduleVisitProperty.value = property
    }

    fun dismissScheduleVisit() {
        _activeScheduleVisitProperty.value = null
    }

    fun confirmBooking(booking: VisitBooking) {
        repository.bookVisit(booking)
    }

    fun openComparison(properties: List<Property>) {
        _propertiesToCompare.value = properties
    }

    fun dismissComparison() {
        _propertiesToCompare.value = emptyList()
    }

    fun openAiConcierge() {
        _showAiConcierge.value = true
    }

    fun dismissAiConcierge() {
        _showAiConcierge.value = false
    }

    fun setUserRole(role: UserRole) {
        repository.setUserRole(role)
    }

    fun setCurrency(currency: Currency) {
        repository.setCurrency(currency)
    }

    fun toggleTheme() {
        repository.toggleTheme()
    }

    fun addProperty(property: Property) {
        repository.addProperty(property)
    }

    fun deleteProperty(propertyId: String) {
        repository.deleteProperty(propertyId)
        if (_selectedProperty.value?.id == propertyId) {
            _selectedProperty.value = null
        }
    }

    fun updateBookingStatus(bookingId: String, status: String) {
        repository.updateBookingStatus(bookingId, status)
    }

    fun saveSearch(search: SavedSearch) {
        repository.saveSearch(search)
    }

    fun downloadBrochure(property: Property) {
        _brochureDownloadMessage.value = "VIP Architectural Brochure for '${property.title}' generated successfully."
    }

    fun clearBrochureMessage() {
        _brochureDownloadMessage.value = null
    }
}
