package com.example.data.model

enum class PropertyType(val displayName: String) {
    VILLA("Luxury Villa"),
    PENTHOUSE("Sky Penthouse"),
    APARTMENT("Designer Residence"),
    COMMERCIAL("Commercial Tower"),
    WATERFRONT("Waterfront Estate"),
    MANSION("Mega Mansion")
}

enum class ListingCategory(val displayName: String) {
    ALL("All Listings"),
    BUY("Buy"),
    RENT("Rent"),
    COMMERCIAL("Commercial"),
    LUXURY_VILLAS("Luxury Villas"),
    APARTMENTS("Apartments")
}

enum class Amenity(val displayName: String, val iconName: String) {
    INFINITY_POOL("Infinity Pool", "pool"),
    PRIVATE_DOCK("Private Yacht Dock", "anchor"),
    HELIPAD("Helipad", "flight"),
    WINE_CELLAR("Sommelier Wine Cellar", "wine_bar"),
    HOME_CINEMA("Dolby Cinema Room", "movie"),
    SPA_WELLNESS("Private Spa & Sauna", "spa"),
    SMART_HOME("Crestron Home Automation", "home_iot"),
    TENNIS_COURT("Championship Tennis Court", "sports_tennis"),
    PRIVATE_ELEVATOR("Private High-speed Elevator", "elevator"),
    CONCIERGE("24/7 VIP Concierge", "room_service")
}

data class TourHotspot(
    val id: String,
    val title: String,
    val description: String,
    val xPercent: Float, // 0f to 1f
    val yPercent: Float, // 0f to 1f
    val material: String,
    val highlight: String
)

data class TourRoom(
    val id: String,
    val name: String,
    val floor: String,
    val description: String,
    val dimensions: String,
    val ceilingHeight: String,
    val hotspots: List<TourHotspot>,
    val ambientType: String = "Sunset Amber"
)

data class Agent(
    val id: String,
    val name: String,
    val title: String,
    val phone: String,
    val email: String,
    val whatsapp: String,
    val rating: Float,
    val dealsClosed: Int,
    val volumeSold: String,
    val bio: String,
    val avatarColorHex: Long = 0xFFD4AF37
)

data class Property(
    val id: String,
    val title: String,
    val tagLine: String,
    val priceUsd: Double,
    val rentMonthlyUsd: Double? = null,
    val category: ListingCategory,
    val propertyType: PropertyType,
    val city: String,
    val address: String,
    val country: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val sqft: Int,
    val yearBuilt: Int,
    val heroDrawableRes: Int,
    val secondaryDrawableRes: Int? = null,
    val description: String,
    val amenities: List<Amenity>,
    val agent: Agent,
    val tourRooms: List<TourRoom>,
    val isFeatured: Boolean = false,
    val isHotDeal: Boolean = false,
    val walkScore: Int = 94,
    val viewsCount: Int = 1420,
    val inquiriesCount: Int = 38
)

data class VisitBooking(
    val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val dateString: String,
    val timeSlot: String,
    val visitorName: String,
    val visitorEmail: String,
    val visitorPhone: String,
    val privateChauffeur: Boolean = true,
    val champagneReception: Boolean = true,
    val status: String = "Confirmed",
    val notes: String = ""
)

data class SavedSearch(
    val id: String,
    val title: String,
    val category: ListingCategory,
    val maxPrice: Double,
    val minBedrooms: Int,
    val timestamp: Long = System.currentTimeMillis()
)

enum class UserRole(val displayName: String) {
    CUSTOMER("VIP Client"),
    AGENT("Licensed Broker"),
    ADMIN("Prime Admin")
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole = UserRole.CUSTOMER,
    val phone: String = "+1 (800) 774-6337",
    val membershipTier: String = "Diamond Black VIP",
    val favoritePropertyIds: Set<String> = emptySet(),
    val viewedPropertyIds: List<String> = emptyList()
)

enum class Currency(val code: String, val symbol: String, val rateToUsd: Double) {
    USD("USD", "$", 1.0),
    EUR("EUR", "€", 0.92),
    GBP("GBP", "£", 0.78),
    AED("AED", "AED ", 3.67),
    JPY("JPY", "¥", 152.0)
}

data class BlogPost(
    val id: String,
    val title: String,
    val excerpt: String,
    val category: String,
    val readTime: String,
    val date: String,
    val author: String
)

data class Testimonial(
    val id: String,
    val clientName: String,
    val title: String,
    val quote: String,
    val rating: Float,
    val propertyAcquired: String,
    val city: String
)

data class CompanyStat(
    val label: String,
    val targetNumber: Int,
    val prefix: String = "",
    val suffix: String = ""
)

data class AiRecommendationMatch(
    val property: Property,
    val matchPercentage: Int,
    val matchReason: String,
    val matchedSignals: List<String>
)
