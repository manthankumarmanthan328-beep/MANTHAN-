package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Property
import com.example.data.model.TourHotspot
import com.example.data.model.TourRoom
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun Virtual3DTourViewer(
    property: Property,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rooms = property.tourRooms.ifEmpty {
        listOf(
            TourRoom(
                id = "default_room",
                name = "Grand Salon",
                floor = "Main Floor",
                description = "Panoramic open reception salon with panoramic vistas.",
                dimensions = "45 ft x 30 ft",
                ceilingHeight = "18 ft",
                hotspots = emptyList()
            )
        )
    }

    var selectedRoom by remember { mutableStateOf(rooms[0]) }
    var panX by remember { mutableFloatStateOf(0f) }
    var panY by remember { mutableFloatStateOf(0f) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var isNightMode by remember { mutableStateOf(false) }
    var isAutoOrbiting by remember { mutableStateOf(true) }
    var showFloorplan by remember { mutableStateOf(false) }
    var showMeasurements by remember { mutableStateOf(true) }
    var selectedHotspot by remember { mutableStateOf<TourHotspot?>(null) }

    // Auto-orbit loop
    LaunchedEffect(isAutoOrbiting) {
        while (isAutoOrbiting) {
            delay(32)
            panX += 0.8f
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LuxuryBlack)
            .testTag("virtual_3d_tour_screen")
    ) {
        // 3D Canvas Background & Panorama Viewport
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        isAutoOrbiting = false
                        panX += pan.x * 0.4f
                        panY = (panY + pan.y * 0.3f).coerceIn(-120f, 120f)
                        zoomScale = (zoomScale * zoom).coerceIn(0.85f, 2.2f)
                    }
                }
        ) {
            // High-resolution architectural image layer representing room
            AsyncImage(
                model = property.heroDrawableRes,
                contentDescription = selectedRoom.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (panX % 360).dp, y = panY.dp)
            )

            // Dynamic lighting / day-night filter
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isNightMode) {
                            Color(0x99050814)
                        } else {
                            Color(0x22D4AF37)
                        }
                    )
            )

            // Perspective Wireframe & 3D Spatial Grid
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                val centerX = width / 2 + (panX % width) * 0.2f
                val centerY = height / 2 + panY * 0.5f

                // Perspective lines converging to vanishing horizon
                val gridAlpha = if (isNightMode) 0.15f else 0.22f
                val lineColor = LuxuryGold.copy(alpha = gridAlpha)

                // Ceiling & Floor horizon guides
                drawLine(
                    color = lineColor,
                    start = Offset(0f, centerY - 200f * zoomScale),
                    end = Offset(width, centerY - 200f * zoomScale),
                    strokeWidth = 1.5f
                )
                drawLine(
                    color = lineColor,
                    start = Offset(0f, centerY + 240f * zoomScale),
                    end = Offset(width, centerY + 240f * zoomScale),
                    strokeWidth = 1.5f
                )

                // Vanishing perspective rays
                for (i in -4..4) {
                    val spread = i * (width / 5)
                    drawLine(
                        color = lineColor,
                        start = Offset(centerX, centerY),
                        end = Offset(centerX + spread * 2, height),
                        strokeWidth = 1f
                    )
                }

                // Architectural Laser Measurement Overlay
                if (showMeasurements) {
                    val measureColor = LuxuryElectricBlue.copy(alpha = 0.85f)
                    // Floor-to-ceiling measurement vertical line
                    drawLine(
                        color = measureColor,
                        start = Offset(centerX - 100f, centerY - 180f),
                        end = Offset(centerX - 100f, centerY + 220f),
                        strokeWidth = 2f,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                }
            }

            // Interactive Hotspot Pins
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val boxWidth = maxWidth.value
                val boxHeight = maxHeight.value

                selectedRoom.hotspots.forEach { hotspot ->
                    // Calculate spatial offset with pan
                    val posX = (hotspot.xPercent * boxWidth + (panX % 100) * 0.5f).dp
                    val posY = (hotspot.yPercent * boxHeight + panY * 0.3f).dp

                    Box(
                        modifier = Modifier
                            .offset(x = posX, y = posY)
                            .size(44.dp)
                            .clickable {
                                selectedHotspot = hotspot
                            }
                            .testTag("hotspot_${hotspot.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        // Pulsing Gold Outer Ring
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(LuxuryGold.copy(alpha = 0.25f), CircleShape)
                                .border(1.5.dp, LuxuryGold, CircleShape)
                        )
                        // Inner Core Pin
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(LuxuryPureWhite, CircleShape)
                                .border(2.dp, LuxuryGoldDark, CircleShape)
                        )
                    }
                }
            }
        }

        // Top Navigation Bar & Controls HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close / Exit button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(44.dp)
                    .background(LuxuryBlack.copy(alpha = 0.75f), CircleShape)
                    .border(1.dp, LuxuryBorderDark, CircleShape)
                    .testTag("close_3d_tour_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit 3D Tour",
                    tint = Color.White
                )
            }

            // Room Title & Floor HUD
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(LuxuryBlack.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .border(1.dp, LuxuryGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = selectedRoom.name,
                    color = LuxuryGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${selectedRoom.floor} • 360° Walkthrough",
                    color = LuxuryTextSecondaryDark,
                    fontSize = 11.sp
                )
            }

            // Quick HUD Action Buttons (Lighting, Orbit, Measurements)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Day / Night Toggle
                IconButton(
                    onClick = { isNightMode = !isNightMode },
                    modifier = Modifier
                        .size(40.dp)
                        .background(LuxuryBlack.copy(alpha = 0.75f), CircleShape)
                        .border(1.dp, LuxuryBorderDark, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isNightMode) Icons.Default.NightlightRound else Icons.Default.WbSunny,
                        contentDescription = "Lighting toggle",
                        tint = if (isNightMode) LuxuryIceBlue else LuxuryGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Auto Orbit Walkthrough
                IconButton(
                    onClick = { isAutoOrbiting = !isAutoOrbiting },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isAutoOrbiting) LuxuryGold.copy(alpha = 0.25f) else LuxuryBlack.copy(alpha = 0.75f),
                            CircleShape
                        )
                        .border(1.dp, if (isAutoOrbiting) LuxuryGold else LuxuryBorderDark, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isAutoOrbiting) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Auto Orbit",
                        tint = LuxuryGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Real-Time Laser Dimension Badge
        if (showMeasurements) {
            Surface(
                color = LuxuryBlack.copy(alpha = 0.85f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryElectricBlue.copy(alpha = 0.5f)),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 80.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = LuxuryElectricBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Dim: ${selectedRoom.dimensions} | H: ${selectedRoom.ceilingHeight}",
                        color = LuxuryTextPrimaryDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Bottom Controls: Floorplan Minimap Toggle + Room Carousel + Hotspot Sheet
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Hotspot Inspection Modal Sheet
            AnimatedVisibility(
                visible = selectedHotspot != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                selectedHotspot?.let { hs ->
                    LuxuryGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        backgroundColor = LuxuryBlack.copy(alpha = 0.92f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(LuxuryGold, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = hs.title,
                                    color = LuxuryGold,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(
                                onClick = { selectedHotspot = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hs.description,
                            color = LuxuryTextPrimaryDark,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "Spec: ${hs.material}",
                                color = LuxuryTextSecondaryDark,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Feature: ${hs.highlight}",
                                color = LuxuryIceBlue,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Room Switcher Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rooms) { room ->
                    val isSelected = room.id == selectedRoom.id
                    Surface(
                        color = if (isSelected) LuxuryGold else LuxuryBlack.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) LuxuryGoldLight else LuxuryBorderDark
                        ),
                        modifier = Modifier
                            .clickable {
                                selectedRoom = room
                                selectedHotspot = null
                            }
                            .testTag("room_btn_${room.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isSelected) LuxuryBlack else LuxuryGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = room.name,
                                color = if (isSelected) LuxuryBlack else Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
