package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.LauncherApp
import com.example.ui.apps.WallpaperType
import com.example.ui.components.IOS4StatusBar
import com.example.ui.dock.GlassDock
import com.example.ui.icons.IOS4Icon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    homeApps: List<LauncherApp>,
    dockApps: List<LauncherApp>,
    wallpaper: WallpaperType,
    isJiggleMode: Boolean,
    onAppClick: (LauncherApp, Rect?) -> Unit,
    onAppLongClick: (LauncherApp) -> Unit,
    onRemoveApp: (String) -> Unit,
    onExitJiggleMode: () -> Unit,
    onOpenSpotlight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "home_jiggle")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -2.2f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(120),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isJiggleMode) onExitJiggleMode()
                    }
                )
            }
    ) {
        // Wallpaper Layer
        WallpaperBackground(wallpaper = wallpaper)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Vintage iOS 4 Status Bar
            IOS4StatusBar(
                isJiggleMode = isJiggleMode,
                onDoneClick = onExitJiggleMode
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4x4 App Grid (Home Screen Icons)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Render up to 4 rows of 4 icons
                val rows = homeApps.chunked(4)
                rows.forEachIndexed { rowIndex, rowApps ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        rowApps.forEachIndexed { colIndex, app ->
                            val appIndex = rowIndex * 4 + colIndex
                            val wiggleAngle = if (isJiggleMode) {
                                if (appIndex % 2 == 0) rotation else -rotation
                            } else 0f
                            var iconBounds by remember { mutableStateOf<Rect?>(null) }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(72.dp)
                                    .rotate(wiggleAngle)
                                    .combinedClickable(
                                        onClick = { onAppClick(app, iconBounds) },
                                        onLongClick = { onAppLongClick(app) }
                                    )
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.onGloballyPositioned { coords ->
                                        iconBounds = coords.boundsInRoot()
                                    }
                                ) {
                                    IOS4Icon(app = app, size = 60.dp)

                                    // Jiggle Mode Delete Badge
                                    if (isJiggleMode) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .offset(x = (-6).dp, y = (-6).dp)
                                                .size(22.dp)
                                                .shadow(2.dp, CircleShape)
                                                .background(Color(0xFF888E96), CircleShape)
                                                .border(1.5.dp, Color.White, CircleShape)
                                                .clickable { onRemoveApp(app.id) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "−",
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.offset(y = (-1).dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = app.title,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.95f),
                                            offset = Offset(0f, 1.5f),
                                            blurRadius = 3f
                                        )
                                    )
                                )
                            }
                        }

                        // Fill remainder of row if fewer than 4 items
                        if (rowApps.size < 4) {
                            for (i in rowApps.size until 4) {
                                Spacer(modifier = Modifier.width(72.dp))
                            }
                        }
                    }
                }
            }

            // Page Indicator (Spotlight Search Icon + Page Dot)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spotlight Magnifying Glass Dot
                Box(
                    modifier = Modifier
                        .clickable { onOpenSpotlight() }
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔍",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Active Page 1 Dot
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(Color.White, CircleShape)
                        .border(0.5.dp, Color.Black.copy(0.3f), CircleShape)
                )
            }

            // 3D Glass Shelf Dock
            GlassDock(
                dockApps = dockApps,
                isJiggleMode = isJiggleMode,
                onAppClick = onAppClick,
                onAppLongClick = onAppLongClick
            )

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun WallpaperBackground(wallpaper: WallpaperType) {
    when (wallpaper) {
        WallpaperType.RAINDROPS -> {
            Image(
                painter = painterResource(id = R.drawable.img_ios4_wallpaper),
                contentDescription = "iOS 4 Water Droplets Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        WallpaperType.DARK_LINEN -> {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF181A1E))
            ) {
                val w = size.width
                val h = size.height
                var x = 0f
                while (x < w) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = 1f
                    )
                    x += 4f
                }
                var y = 0f
                while (y < h) {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.35f),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f
                    )
                    y += 4f
                }
            }
        }
        WallpaperType.EARTH -> {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF03050A),
                                Color(0xFF081220),
                                Color(0xFF102A4C),
                                Color(0xFF1D5A9C)
                            )
                        )
                    )
            ) {
                // Earth glowing curve at bottom
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF4FA0EE), Color(0xFF1A5A9E), Color(0xFF0D2848)),
                        center = Offset(size.width / 2f, size.height * 1.3f),
                        radius = size.width * 1.1f
                    ),
                    radius = size.width * 1.1f,
                    center = Offset(size.width / 2f, size.height * 1.3f)
                )
            }
        }
        WallpaperType.PURPLE_NEBULA -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF5D1D7A), Color(0xFF2A0A38), Color(0xFF0A0210))
                        )
                    )
            )
        }
    }
}
