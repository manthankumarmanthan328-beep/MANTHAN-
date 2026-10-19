package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.AiRecommendationService
import com.example.data.model.ListingCategory
import com.example.data.model.SavedSearch
import com.example.data.repository.PropertyRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches Prime Estate`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Prime Estate", appName)
    }

    @Test
    fun `property repository initializes with trophy listings`() {
        val repo = PropertyRepository()
        val properties = repo.properties.value
        assertTrue(properties.isNotEmpty())
        assertEquals(6, properties.size)
        assertTrue(properties.any { it.title.contains("Solstice") })
    }

    @Test
    fun `ai recommendation engine computes match percentages and reasons`() {
        val repo = PropertyRepository()
        val aiService = AiRecommendationService()
        val properties = repo.properties.value

        val recommendations = aiService.computePersonalizedRecommendations(
            allProperties = properties,
            viewedPropertyIds = listOf("prop_1", "prop_3"),
            wishlistIds = setOf("prop_3"),
            savedSearches = listOf(
                SavedSearch("s_test", "Villas", ListingCategory.LUXURY_VILLAS, 40000000.0, 5)
            )
        )

        assertTrue(recommendations.isNotEmpty())
        val topMatch = recommendations.first()
        assertTrue(topMatch.matchPercentage in 75..100)
        assertNotNull(topMatch.matchReason)
        assertTrue(topMatch.matchedSignals.isNotEmpty())
    }
}
