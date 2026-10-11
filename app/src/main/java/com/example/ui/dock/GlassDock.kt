package com.example.ui.dock

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LauncherApp
import com.example.ui.icons.IOS4Icon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GlassDock(
    dockApps: List<LauncherApp>,
    isJiggleMode: Boolean,
    onAppClick: (LauncherApp, Rect?) -> Unit,
    onAppLongClick: (LauncherApp) -> Unit,
    modifier: Modifier = Modifier
) {
    // Infinite transition for jiggle/wiggle mode
    val infiniteTransition = rememberInfiniteTransition(label = "dock_jiggle")
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
            .fillMaxWidth()
            .height(108.dp)
    ) {
        // 3D Perspective Glass Shelf Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 3D Glass Shelf trapezoid perspective
            val shelfTopY = h * 0.42f
            val shelfBottomY = h * 0.94f
            val insetX = w * 0.04f

            // Shadow under the shelf
            val shadowPath = Path().apply {
                moveTo(insetX, shelfBottomY)
                lineTo(w - insetX, shelfBottomY)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = shadowPath,
                brush = Brush.verticalGradient(
                    listOf(Color(0x77000000), Color(0x22000000))
                )
            )

            // Glass shelf plane
            val shelfPlane = Path().apply {
                moveTo(insetX, shelfTopY)
                lineTo(w - insetX, shelfTopY)
                lineTo(w, shelfBottomY)
                lineTo(0f, shelfBottomY)
                close()
            }
            drawPath(
                path = shelfPlane,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.12f),
                        Color.Black.copy(alpha = 0.25f)
                    ),
                    startY = shelfTopY,
                    endY = shelfBottomY
                )
            )

            // Top specular highlight edge of glass shelf
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.85f),
                        Color.White.copy(alpha = 0.3f)
                    )
                ),
                start = Offset(insetX, shelfTopY),
                end = Offset(w - insetX, shelfTopY),
                strokeWidth = 3f
            )

            // Secondary glass reflection line
            drawLine(
                color = Color.White.copy(alpha = 0.4f),
                start = Offset(insetX * 0.8f, shelfTopY + 2f),
                end = Offset(w - insetX * 0.8f, shelfTopY + 2f),
                strokeWidth = 1f
            )
        }

        // Dock Icons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {
            dockApps.take(4).forEachIndexed { index, app ->
                val wiggleOffset = if (isJiggleMode) {
                    if (index % 2 == 0) rotation else -rotation
                } else 0f
                var iconBounds by remember { mutableStateOf<Rect?>(null) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(72.dp)
                        .rotate(wiggleOffset)
                        .combinedClickable(
                            onClick = { onAppClick(app, iconBounds) },
                            onLongClick = { onAppLongClick(app) }
                        )
                ) {
                    // Main App Icon
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            iconBounds = coordinates.boundsInRoot()
                        }
                    ) {
                        IOS4Icon(
                            app = app,
                            size = 58.dp
                        )
                    }

                    // Iconic iOS 4 Mirror Reflection on the glass dock shelf
                    // Inverted mirror with semi-transparent gradient mask fading to transparent
                    Box(
                        modifier = Modifier
                            .width(58.dp)
                            .height(28.dp)
                            .offset(y = (-1).dp)
                            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                                alpha = 0.42f
                                scaleY = -0.92f
                            }
                            .drawWithContent {
                                drawContent()
                                // Fade reflection gracefully from top to bottom (inverted icon)
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0.0f to Color.Black.copy(alpha = 0.85f),
                                            0.25f to Color.Black.copy(alpha = 0.55f),
                                            0.65f to Color.Black.copy(alpha = 0.15f),
                                            1.0f to Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = size.height
                                    ),
                                    blendMode = BlendMode.DstIn
                                )
                                // Subtle glossy glass tint over reflection
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.12f),
                                            Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = size.height * 0.5f
                                    ),
                                    blendMode = BlendMode.Plus
                                )
                            }
                    ) {
                        IOS4Icon(
                            app = app,
                            size = 58.dp,
                            showGloss = false
                        )
                    }

                    // Icon Title
                    Text(
                        text = app.title,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 1.dp),
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
        }
    }
}
