package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PropertyRepository {

    private val defaultAgents = listOf(
        Agent(
            id = "agent_1",
            name = "Alexandra Van Der Bilt",
            title = "Managing Director, Ultra-Luxury & Islands",
            phone = "+1 (212) 555-0199",
            email = "alexandra@primeestate.luxury",
            whatsapp = "+12125550199",
            rating = 4.98f,
            dealsClosed = 142,
            volumeSold = "$1.85 Billion",
            bio = "Specializing in discreet high-net-worth acquisitions across Manhattan, Palm Beach, and Monaco for over 16 years."
        ),
        Agent(
            id = "agent_2",
            name = "Marcus Aurelius Sterling",
            title = "Principal Broker, Sky Penthouses & Commercial",
            phone = "+1 (305) 555-0143",
            email = "marcus@primeestate.luxury",
            whatsapp = "+13055550143",
            rating = 4.95f,
            dealsClosed = 118,
            volumeSold = "$1.42 Billion",
            bio = "Global architectural connoisseur leading institutional sales, trophy towers, and waterfront mega-compounds."
        ),
        Agent(
            id = "agent_3",
            name = "Soraya Al-Maktoum",
            title = "Vice President, International & Private Estates",
            phone = "+971 4 555 8899",
            email = "soraya@primeestate.luxury",
            whatsapp = "+97145558899",
            rating = 5.0f,
            dealsClosed = 95,
            volumeSold = "$980 Million",
            bio = "Trusted advisor to royal families and international technology founders expanding portfolios in Dubai, London, and the French Riviera."
        )
    )

    private val sampleHotspotsVillaFoyer = listOf(
        TourHotspot(
            id = "hs_1",
            title = "Calacatta Gold Bookmatched Marble",
            description = "Hand-quarried Italian Calacatta marble slab with continuous golden veins running seamlessly across the grand foyer.",
            xPercent = 0.28f,
            yPercent = 0.65f,
            material = "Quarried in Carrara, Italy",
            highlight = "Acoustically damped subfloor"
        ),
        TourHotspot(
            id = "hs_2",
            title = "Bespoke Venetian Murano Chandelier",
            description = "Custom 48-pendant handblown crystal fixture refracting ambient twilight into shimmering geometric reflections.",
            xPercent = 0.50f,
            yPercent = 0.22f,
            material = "Artisanal Murano Blown Glass",
            highlight = "Automated lowering winch system"
        ),
        TourHotspot(
            id = "hs_3",
            title = "Floating Sculptural Cantilevered Staircase",
            description = "Monolithic blackened steel and smoked bronze glass treads engineered with zero visible vertical supports.",
            xPercent = 0.76f,
            yPercent = 0.48f,
            material = "Aero-grade titanium steel",
            highlight = "Integrated step illumination"
        )
    )

    private val sampleHotspotsPenthouseLiving = listOf(
        TourHotspot(
            id = "hs_p1",
            title = "Floor-to-Ceiling Motorized Switchable Smart Glass",
            description = "Triple-pane acoustic glazing with electrochromic privacy shading and UV-absorbing infrared filtration.",
            xPercent = 0.35f,
            yPercent = 0.38f,
            material = "Swiss Low-Iron Starphire Glass",
            highlight = "Wind-rated for Category 5 gusts"
        ),
        TourHotspot(
            id = "hs_p2",
            title = "Boffi Master Chef Kitchen & Basalt Island",
            description = "Solid volcanic basalt monolithic island outfitted with Gaggenau 400 series induction cooktops and sub-zero wine columns.",
            xPercent = 0.68f,
            yPercent = 0.62f,
            material = "Italian Volcanic Basalt",
            highlight = "Touch-to-open motorized joinery"
        )
    )

    private val sampleHotspotsTerracePool = listOf(
        TourHotspot(
            id = "hs_t1",
            title = "Zero-Edge Heated Saltwater Infinity Pool",
            description = "Vanishing perimeter ledge merging optical lines with horizon waters, equipped with submerged underwater acoustics.",
            xPercent = 0.52f,
            yPercent = 0.72f,
            material = "Bisazza Glass Micro-mosaics",
            highlight = "Geothermal heat exchange system"
        ),
        TourHotspot(
            id = "hs_t2",
            title = "Outdoor Teak Deck & Fire Pavilion",
            description = "Sustainably harvested nautical-grade Burmese teak planking surrounded by an ethanol linear fire trough.",
            xPercent = 0.82f,
            yPercent = 0.55f,
            material = "Grade-A Burmese Teak",
            highlight = "Marine corrosion resistant"
        )
    )

    private val initialProperties = listOf(
        Property(
            id = "prop_1",
            title = "The Celestial Solstice Villa",
            tagLine = "Architectural Masterpiece on Private Promontory",
            priceUsd = 28500000.0,
            rentMonthlyUsd = 125000.0,
            category = ListingCategory.LUXURY_VILLAS,
            propertyType = PropertyType.VILLA,
            city = "Beverly Hills",
            address = "14040 Vista Del Sol Dr, Beverly Hills, CA",
            country = "United States",
            bedrooms = 7,
            bathrooms = 11,
            sqft = 16800,
            yearBuilt = 2024,
            heroDrawableRes = R.drawable.img_hero_villa,
            secondaryDrawableRes = R.drawable.img_waterfront_estate,
            description = "Perched above the city with boundless panoramic views stretching from Downtown LA to Catalina Island. Designed by world-renowned minimalist masters, this trophy estate boasts expansive glass pocket doors, 120-foot infinity pool, underground auto gallery for 10 vehicles, and commercial-grade wellness retreat.",
            amenities = listOf(
                Amenity.INFINITY_POOL,
                Amenity.WINE_CELLAR,
                Amenity.HOME_CINEMA,
                Amenity.SPA_WELLNESS,
                Amenity.SMART_HOME,
                Amenity.CONCIERGE
            ),
            agent = defaultAgents[0],
            tourRooms = listOf(
                TourRoom("room_1", "Grand Arrival Foyer", "Ground Level", "Double-height entrance gallery with bookmatched Italian marble and custom lighting.", "32 ft x 24 ft", "22 ft Ceiling", sampleHotspotsVillaFoyer),
                TourRoom("room_2", "Master Panorama Terrace", "Upper Floor", "Private terrace with cantilevered zero-edge plunge pool and fire lounge.", "45 ft x 28 ft", "Open Sky", sampleHotspotsTerracePool),
                TourRoom("room_3", "Great Room & Chef's Saloon", "Ground Level", "Expansive open living space with disappearing Fleetwood glass wall panels.", "50 ft x 34 ft", "14 ft Ceiling", sampleHotspotsPenthouseLiving)
            ),
            isFeatured = true,
            isHotDeal = true,
            walkScore = 88,
            viewsCount = 3840,
            inquiriesCount = 72
        ),
        Property(
            id = "prop_2",
            title = "The One Sky Penthouse",
            tagLine = "Breathtaking Triplex Crown Over Central Park",
            priceUsd = 42000000.0,
            rentMonthlyUsd = 180000.0,
            category = ListingCategory.APARTMENTS,
            propertyType = PropertyType.PENTHOUSE,
            city = "New York",
            address = "111 W 57th St, Penthouse 72, New York, NY",
            country = "United States",
            bedrooms = 5,
            bathrooms = 7,
            sqft = 9450,
            yearBuilt = 2023,
            heroDrawableRes = R.drawable.img_luxury_penthouse,
            secondaryDrawableRes = R.drawable.img_hero_villa,
            description = "Rising over 1,000 feet in the sky, this full-floor triplex commands unprecedented 360-degree vistas of Central Park and the Manhattan skyline. Features include a private internal bronze elevator, double-height grand salon with 28-foot ceilings, and a private 1,200 sqft loggia.",
            amenities = listOf(
                Amenity.PRIVATE_ELEVATOR,
                Amenity.WINE_CELLAR,
                Amenity.CONCIERGE,
                Amenity.SPA_WELLNESS,
                Amenity.SMART_HOME
            ),
            agent = defaultAgents[1],
            tourRooms = listOf(
                TourRoom("room_p1", "Sky Grand Salon", "72nd Floor", "Double-height soaring living pavilion facing direct Central Park axis.", "48 ft x 30 ft", "28 ft Ceiling", sampleHotspotsPenthouseLiving),
                TourRoom("room_p2", "Master Suite & Spa Oasis", "73rd Floor", "Primary sanctuary with dual custom dressing galleries and heated marble slabs.", "36 ft x 26 ft", "12 ft Ceiling", sampleHotspotsVillaFoyer)
            ),
            isFeatured = true,
            isHotDeal = false,
            walkScore = 99,
            viewsCount = 5120,
            inquiriesCount = 114
        ),
        Property(
            id = "prop_3",
            title = "Villa Azure Oceanfront Sanctuary",
            tagLine = "Direct Coastal Compound with Deepwater Yacht Slip",
            priceUsd = 34500000.0,
            rentMonthlyUsd = 160000.0,
            category = ListingCategory.BUY,
            propertyType = PropertyType.WATERFRONT,
            city = "Miami Beach",
            address = "42 Star Island Drive, Miami Beach, FL",
            country = "United States",
            bedrooms = 8,
            bathrooms = 12,
            sqft = 18500,
            yearBuilt = 2024,
            heroDrawableRes = R.drawable.img_waterfront_estate,
            secondaryDrawableRes = R.drawable.img_hero_villa,
            description = "The pinnacle of South Florida luxury on guarded Star Island. 200 feet of prime bay frontage accommodating mega-yachts up to 130 feet. Impeccable Balinese-modern architecture, private white sand beach cove, rooftop observatory, and staff quarters.",
            amenities = listOf(
                Amenity.PRIVATE_DOCK,
                Amenity.INFINITY_POOL,
                Amenity.HELIPAD,
                Amenity.TENNIS_COURT,
                Amenity.HOME_CINEMA,
                Amenity.SMART_HOME
            ),
            agent = defaultAgents[0],
            tourRooms = listOf(
                TourRoom("room_w1", "Waterfront Infinity Terrace", "Bay Level", "Expansive travertine terrace with cascading reflection pools and yacht mooring.", "65 ft x 40 ft", "Open Waterfront", sampleHotspotsTerracePool),
                TourRoom("room_w2", "Formal Dining & Grand Atrium", "Main Level", "Surrounded by interior koi lagoons and floor-to-ceiling acoustic glass.", "38 ft x 24 ft", "18 ft Ceiling", sampleHotspotsVillaFoyer)
            ),
            isFeatured = true,
            isHotDeal = true,
            walkScore = 76,
            viewsCount = 4210,
            inquiriesCount = 89
        ),
        Property(
            id = "prop_4",
            title = "The Obsidian Global HQ Tower",
            tagLine = "LEED Platinum Trophy Commercial Headquarters",
            priceUsd = 95000000.0,
            rentMonthlyUsd = 450000.0,
            category = ListingCategory.COMMERCIAL,
            propertyType = PropertyType.COMMERCIAL,
            city = "London",
            address = "1 Bishopsgate Plaza, Financial District, London",
            country = "United Kingdom",
            bedrooms = 0,
            bathrooms = 24,
            sqft = 65000,
            yearBuilt = 2022,
            heroDrawableRes = R.drawable.img_commercial_tower,
            secondaryDrawableRes = R.drawable.img_luxury_penthouse,
            description = "A landmark investment asset in the heart of London's financial district. 18 floors of Grade-A hyper-connected office space, executive boardroom pavilion with 360-degree Thames panoramas, private helipad landing rights, and dedicated banking vault facilities.",
            amenities = listOf(
                Amenity.HELIPAD,
                Amenity.PRIVATE_ELEVATOR,
                Amenity.CONCIERGE,
                Amenity.SMART_HOME
            ),
            agent = defaultAgents[1],
            tourRooms = listOf(
                TourRoom("room_c1", "Executive Sky Boardroom", "Top Tier", "Panoramic boardroom with telepresence quantum screens and private bar.", "55 ft x 32 ft", "16 ft Ceiling", sampleHotspotsPenthouseLiving),
                TourRoom("room_c2", "Triple-Volume Marble Lobby", "Ground", "Secured biometric access concourse with art gallery installations.", "70 ft x 50 ft", "30 ft Ceiling", sampleHotspotsVillaFoyer)
            ),
            isFeatured = false,
            isHotDeal = false,
            walkScore = 100,
            viewsCount = 2890,
            inquiriesCount = 45
        ),
        Property(
            id = "prop_5",
            title = "Palais Royal Palm Jumeirah",
            tagLine = "Private Beachfront Palace on the Arabian Gulf",
            priceUsd = 55000000.0,
            rentMonthlyUsd = 240000.0,
            category = ListingCategory.LUXURY_VILLAS,
            propertyType = PropertyType.MANSION,
            city = "Dubai",
            address = "Frond N, Palm Jumeirah, Dubai, UAE",
            country = "United Arab Emirates",
            bedrooms = 9,
            bathrooms = 14,
            sqft = 22000,
            yearBuilt = 2025,
            heroDrawableRes = R.drawable.img_hero_villa,
            secondaryDrawableRes = R.drawable.img_waterfront_estate,
            description = "Gilded in 24-karat gold leaf detailing and custom Lalique chandeliers, this ultra-exclusive beachfront palace features an indoor Roman bath, Olympic-sized private outdoor pool, private beach cabana, and subterranean bowling alley.",
            amenities = listOf(
                Amenity.INFINITY_POOL,
                Amenity.PRIVATE_DOCK,
                Amenity.SPA_WELLNESS,
                Amenity.HOME_CINEMA,
                Amenity.WINE_CELLAR,
                Amenity.CONCIERGE
            ),
            agent = defaultAgents[2],
            tourRooms = listOf(
                TourRoom("room_d1", "Imperial Majlis & Reception", "Main Ground", "Ornate palace reception salon opening to golden sand beach.", "60 ft x 40 ft", "24 ft Ceiling", sampleHotspotsVillaFoyer),
                TourRoom("room_d2", "Beachfront Roman Pool Deck", "Ocean Level", "Lagoon-style pool with swim-up marble bar and cabana lounges.", "80 ft x 50 ft", "Open Air", sampleHotspotsTerracePool)
            ),
            isFeatured = true,
            isHotDeal = true,
            walkScore = 82,
            viewsCount = 6780,
            inquiriesCount = 156
        ),
        Property(
            id = "prop_6",
            title = "Villa Mirador Cap d'Antibes",
            tagLine = "Belle Époque Riviera Villa with Olive Groves",
            priceUsd = 31000000.0,
            rentMonthlyUsd = 135000.0,
            category = ListingCategory.RENT,
            propertyType = PropertyType.VILLA,
            city = "Monaco / French Riviera",
            address = "Boulevard de la Croisette, Cap d'Antibes",
            country = "France",
            bedrooms = 6,
            bathrooms = 8,
            sqft = 12400,
            yearBuilt = 2023,
            heroDrawableRes = R.drawable.img_waterfront_estate,
            secondaryDrawableRes = R.drawable.img_luxury_penthouse,
            description = "Nestled within centuries-old olive groves with unobstructed views over the shimmering Mediterranean Sea. Restored with contemporary French chic, private tennis court, wine cellar holding 4,000 vintage bottles, and private funicular to sea level.",
            amenities = listOf(
                Amenity.TENNIS_COURT,
                Amenity.WINE_CELLAR,
                Amenity.INFINITY_POOL,
                Amenity.SPA_WELLNESS,
                Amenity.CONCIERGE
            ),
            agent = defaultAgents[2],
            tourRooms = listOf(
                TourRoom("room_m1", "Riviera Terrace Salon", "Sea Facing", "Pergola-shaded terrace framed by bougainvillea and sea breezes.", "40 ft x 28 ft", "Open Sea", sampleHotspotsTerracePool)
            ),
            isFeatured = false,
            isHotDeal = false,
            walkScore = 80,
            viewsCount = 3100,
            inquiriesCount = 61
        )
    )

    private val _properties = MutableStateFlow(initialProperties)
    val properties: StateFlow<List<Property>> = _properties.asStateFlow()

    private val _wishlistIds = MutableStateFlow<Set<String>>(setOf("prop_1", "prop_3"))
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    private val _viewedPropertyIds = MutableStateFlow<List<String>>(listOf("prop_1", "prop_2"))
    val viewedPropertyIds: StateFlow<List<String>> = _viewedPropertyIds.asStateFlow()

    private val _savedSearches = MutableStateFlow<List<SavedSearch>>(
        listOf(
            SavedSearch("s_1", "Waterfront Villas under $35M", ListingCategory.LUXURY_VILLAS, 35000000.0, 5),
            SavedSearch("s_2", "New York Sky Penthouses", ListingCategory.APARTMENTS, 50000000.0, 4)
        )
    )
    val savedSearches: StateFlow<List<SavedSearch>> = _savedSearches.asStateFlow()

    private val _visitBookings = MutableStateFlow<List<VisitBooking>>(
        listOf(
            VisitBooking(
                id = "b_1",
                propertyId = "prop_1",
                propertyTitle = "The Celestial Solstice Villa",
                dateString = "Tomorrow, Oct 20",
                timeSlot = "02:00 PM - 04:00 PM",
                visitorName = "Lord Sterling",
                visitorEmail = "sterling@investments.ch",
                visitorPhone = "+41 22 555 9000",
                privateChauffeur = true,
                champagneReception = true,
                status = "Confirmed VIP"
            )
        )
    )
    val visitBookings: StateFlow<List<VisitBooking>> = _visitBookings.asStateFlow()

    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "u_1",
            name = "Lord Manthan Sterling",
            email = "manthankumarmanthan328@gmail.com",
            role = UserRole.CUSTOMER,
            phone = "+1 (800) 774-6337",
            membershipTier = "Black Sovereign VIP",
            favoritePropertyIds = setOf("prop_1", "prop_3")
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _activeCurrency = MutableStateFlow(Currency.USD)
    val activeCurrency: StateFlow<Currency> = _activeCurrency.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Testimonials
    val testimonials = listOf(
        Testimonial(
            id = "t_1",
            clientName = "Sir Jonathan Vance",
            title = "Managing Partner, Sovereign Tech Fund",
            quote = "Prime Estate provided an unmatched private banking caliber experience. Their 3D virtual tour and discrete concierge made acquiring our Miami estate seamless from Zurich.",
            rating = 5.0f,
            propertyAcquired = "Villa Azure Oceanfront ($34.5M)",
            city = "Miami Beach"
        ),
        Testimonial(
            id = "t_2",
            clientName = "Evelyn Du Pont",
            title = "Founder, Paris Haute Horlogerie",
            quote = "The AI property recommendation engine genuinely understood my taste for high ceilings and modern minimalism. Found our dream triplex in Manhattan in under three weeks.",
            rating = 5.0f,
            propertyAcquired = "The One Sky Penthouse ($42.0M)",
            city = "New York"
        ),
        Testimonial(
            id = "t_3",
            clientName = "Sheikh Tariq Bin Rashid",
            title = "Private Office Investments",
            quote = "Supreme professionalism, end-to-end legal discretion, and world-class architectural curation. The gold standard in global luxury real estate.",
            rating = 5.0f,
            propertyAcquired = "Palais Royal Palm ($55.0M)",
            city = "Dubai"
        )
    )

    // Blog & Insights
    val blogPosts = listOf(
        BlogPost(
            id = "blog_1",
            title = "2026 Ultra-Prime Global Wealth & Trophy Real Estate Report",
            excerpt = "Analysis of capital flight into rare architectural masterpieces across New York, London, Dubai, and Monaco.",
            category = "Market Intelligence",
            readTime = "5 min read",
            date = "Oct 18, 2026",
            author = "Prime Estate Advisory"
        ),
        BlogPost(
            id = "blog_2",
            title = "The Rise of Biophilic Mega-Mansions and Wellness Retreats",
            excerpt = "How billionaire buyers are integrating Olympic lap pools, oxygen-enriched suites, and subterranean spas into residential compounds.",
            category = "Architecture & Design",
            readTime = "4 min read",
            date = "Oct 12, 2026",
            author = "Marcus Aurelius Sterling"
        ),
        BlogPost(
            id = "blog_3",
            title = "Tax-Advantaged Waterfront Acquisitions: The Coastal Guide",
            excerpt = "Strategic structuring for deepwater private docks and international marine title deed registrations.",
            category = "Investment Advisory",
            readTime = "6 min read",
            date = "Oct 04, 2026",
            author = "Alexandra Van Der Bilt"
        )
    )

    val companyStats = listOf(
        CompanyStat("Properties Sold", 1850, prefix = "", suffix = "+"),
        CompanyStat("Sales Volume", 4800, prefix = "$", suffix = "M+"),
        CompanyStat("VIP Clients", 980, prefix = "", suffix = "+"),
        CompanyStat("Global Cities", 34, prefix = "", suffix = "")
    )

    fun toggleWishlist(propertyId: String) {
        _wishlistIds.update { current ->
            if (current.contains(propertyId)) current - propertyId else current + propertyId
        }
    }

    fun recordPropertyView(propertyId: String) {
        _viewedPropertyIds.update { current ->
            (listOf(propertyId) + current.filter { it != propertyId }).take(20)
        }
    }

    fun bookVisit(booking: VisitBooking) {
        _visitBookings.update { listOf(booking) + it }
    }

    fun saveSearch(search: SavedSearch) {
        _savedSearches.update { listOf(search) + it }
    }

    fun setUserRole(role: UserRole) {
        _currentUser.update { it.copy(role = role) }
    }

    fun setCurrency(currency: Currency) {
        _activeCurrency.value = currency
    }

    fun toggleTheme() {
        _isDarkTheme.update { !it }
    }

    fun getAgents(): List<Agent> = defaultAgents

    // Admin CRUD operations
    fun addProperty(property: Property) {
        _properties.update { listOf(property) + it }
    }

    fun deleteProperty(propertyId: String) {
        _properties.update { it.filter { p -> p.id != propertyId } }
    }

    fun updateBookingStatus(bookingId: String, status: String) {
        _visitBookings.update { list ->
            list.map { if (it.id == bookingId) it.copy(status = status) else it }
        }
    }
}
