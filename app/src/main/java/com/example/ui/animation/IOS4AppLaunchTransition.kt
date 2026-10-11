package com.example.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.model.AppLaunchOrigin
import com.example.ui.icons.IOS4Icon
import kotlin.math.roundToInt

/**
 * Classic iOS 4 App Launch Zooming Transition:
 * In iOS 4, tapping an app icon creates a signature zoom effect:
 * 1. The icon zooms outwards from its exact home screen grid / dock location.
 * 2. It expands across the screen with an authentic spring/cubic bezier curve
 *    (approx 320ms, initial burst expanding into full screen).
 * 3. As it expands, the icon's corner radius flattens into full screen rectangle,
 *    revealing the app's initial launch canvas/screenshot/content underneath.
 * 4. Simultaneously, the background home screen slightly dims and recedes (zoom-in).
 */
@Composable
fun IOS4AppLaunchTransition(
    origin: AppLaunchOrigin,
    containerSize: IntSize,
    onAnimationEnd: () -> Unit
) {
    val density = LocalDensity.current

    // Authentic iOS 4 bezier curve: quick acceleration out of icon, smooth deceleration at fullscreen
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(origin) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 320,
                easing = CubicBezierEasing(0.2f, 0.0f, 0.25f, 1.0f)
            )
        )
        onAnimationEnd()
    }

    val p = animProgress.value

    val startX = origin.bounds.left
    val startY = origin.bounds.top
    val startW = origin.bounds.width
    val startH = origin.bounds.height

    val targetW = containerSize.width.toFloat().coerceAtLeast(startW)
    val targetH = containerSize.height.toFloat().coerceAtLeast(startH)

    // Current interpolated frame dimensions
    val currentX = startX + (0f - startX) * p
    val currentY = startY + (0f - startY) * p
    val currentW = startW + (targetW - startW) * p
    val currentH = startH + (targetH - startH) * p

    // Corner radius transitions from icon (approx 12dp equivalent in px) to 0dp at full screen
    val initialCornerPx = with(density) { 13.dp.toPx() }
    val currentCornerPx = (initialCornerPx * (1f - p)).coerceAtLeast(0f)

    // Dimming backdrop that matches the iOS 4 launch overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = (p * 0.45f).coerceIn(0f, 0.45f)))
    )

    // Expanding App Window Frame
    Box(
        modifier = Modifier
            .offset { IntOffset(currentX.roundToInt(), currentY.roundToInt()) }
            .size(
                width = with(density) { currentW.toDp() },
                height = with(density) { currentH.toDp() }
            )
            .graphicsLayer {
                shadowElevation = (16f * (1f - p)).coerceAtLeast(0f)
                shape = RoundedCornerShape(with(density) { currentCornerPx.toDp() })
                clip = true
            }
            .background(Color(0xFF0F1115))
    ) {
        // App background canvas (default iOS slate grey/black linen or metallic app tint)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF2C323B),
                            Color(0xFF191D24)
                        )
                    )
                )
        )

        // Centered App Icon that remains prominent as the window expands, fading gracefully into content
        if (p < 0.95f) {
            val iconAlpha = (1f - p * 1.05f).coerceIn(0f, 1f)
            val iconScale = 1f + (p * 0.35f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = iconAlpha
                        scaleX = iconScale
                        scaleY = iconScale
                    },
                contentAlignment = Alignment.Center
            ) {
                IOS4Icon(
                    app = origin.app,
                    size = with(density) { startW.toDp() },
                    showGloss = true
                )
            }
        }

        // Classic iOS specular shine sweep across the expanding surface
        if (p < 0.85f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shineAlpha = ((1f - p) * 0.25f).coerceIn(0f, 0.25f)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = shineAlpha),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height * 0.45f
                    ),
                    size = size,
                    cornerRadius = CornerRadius(currentCornerPx, currentCornerPx)
                )
            }
        }
    }
}
