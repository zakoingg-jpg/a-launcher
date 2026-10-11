package com.example.ui.icons

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppIconType
import com.example.model.LauncherApp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun IOS4Icon(
    app: LauncherApp,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    showGloss: Boolean = true
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(size * 0.22f),
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clip(RoundedCornerShape(size * 0.22f))
            .border(
                width = 0.75.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.45f),
                        Color.White.copy(alpha = 0.1f),
                        Color.Black.copy(alpha = 0.4f)
                    )
                ),
                shape = RoundedCornerShape(size * 0.22f)
            )
    ) {
        // Icon base artwork
        IconContent(app = app, size = size)

        // Signature iOS 4 curved glossy shine overlay
        if (showGloss && app.iconType != AppIconType.UTILITIES_FOLDER) {
            IOS4GlossOverlay(size = size)
        }

        // Notification Badge if present
        if (app.badgeCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(20.dp)
                    .shadow(3.dp, CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFFF3B30), Color(0xFFD61810))
                        ),
                        shape = CircleShape
                    )
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (app.badgeCount > 99) "99+" else app.badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun IOS4GlossOverlay(size: Dp) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = this.size.width
        val h = this.size.height

        // Top-half oval arc clip
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h * 0.42f)
            // Curving arc across the center
            cubicTo(
                w * 0.7f, h * 0.55f,
                w * 0.3f, h * 0.55f,
                0f, h * 0.42f
            )
            close()
        }

        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.70f),
                    Color.White.copy(alpha = 0.35f),
                    Color.White.copy(alpha = 0.05f)
                ),
                startY = 0f,
                endY = h * 0.55f
            )
        )
    }
}

@Composable
private fun IconContent(app: LauncherApp, size: Dp) {
    when (app.iconType) {
        AppIconType.MESSAGES -> MessagesIcon()
        AppIconType.CALENDAR -> CalendarIcon(size = size)
        AppIconType.PHOTOS -> PhotosIcon()
        AppIconType.CAMERA -> CameraIcon()
        AppIconType.YOUTUBE -> YouTubeIcon()
        AppIconType.STOCKS -> StocksIcon()
        AppIconType.MAPS -> MapsIcon()
        AppIconType.WEATHER -> WeatherIcon()
        AppIconType.NOTES -> NotesIcon(size = size)
        AppIconType.UTILITIES_FOLDER -> UtilitiesFolderIcon(app.folderApps, size)
        AppIconType.ITUNES -> ITunesIcon()
        AppIconType.APP_STORE -> AppStoreIcon()
        AppIconType.GAME_CENTER -> GameCenterIcon()
        AppIconType.SETTINGS -> SettingsIcon()
        AppIconType.PHONE -> PhoneIcon()
        AppIconType.MAIL -> MailIcon()
        AppIconType.SAFARI -> SafariIcon()
        AppIconType.IPOD -> IPodIcon()
        AppIconType.CLOCK -> ClockIcon()
        AppIconType.CALCULATOR -> CalculatorIcon()
        AppIconType.COMPASS -> CompassIcon()
        AppIconType.VOICE_MEMOS -> VoiceMemosIcon()
        AppIconType.INSTALLED -> InstalledAppIcon(app)
    }
}

// -------------------------------------------------------------
// INDIVIDUAL SKEUOMORPHIC ICONS
// -------------------------------------------------------------

@Composable
private fun MessagesIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF75DB5B), Color(0xFF4CB832), Color(0xFF389725))
                )
            )
    ) {
        val w = size.width
        val h = size.height
        // Diagonal pinstripes texture
        for (i in -10..20) {
            val offset = i * (w / 8f)
            drawLine(
                color = Color.White.copy(alpha = 0.07f),
                start = Offset(offset, 0f),
                end = Offset(offset + w, h),
                strokeWidth = 2f
            )
        }

        // White speech bubble
        val bubblePath = Path().apply {
            val left = w * 0.18f
            val top = h * 0.22f
            val right = w * 0.82f
            val bottom = h * 0.72f
            val radius = w * 0.20f
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = left, top = top, right = right, bottom = bottom,
                    cornerRadius = CornerRadius(radius, radius)
                )
            )
            // Speech tail
            moveTo(left + w * 0.15f, bottom - 2f)
            lineTo(left + w * 0.05f, bottom + h * 0.12f)
            lineTo(left + w * 0.32f, bottom)
            close()
        }

        // Drop shadow for bubble
        drawPath(
            path = bubblePath,
            color = Color(0x33000000)
        )
        // White bubble with inner gradient
        drawPath(
            path = bubblePath,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFFFFFF), Color(0xFFEBEBEB))
            )
        )
    }
}

@Composable
private fun CalendarIcon(size: Dp) {
    val cal = Calendar.getInstance()
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.time)
    val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH).toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFFFFF), Color(0xFFE8E8E8))
                )
            )
    ) {
        // Red top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size * 0.32f)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE73831), Color(0xFFB81D17))
                    )
                )
                .border(
                    width = 0.5.dp,
                    color = Color(0xFF8B120D)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfWeek,
                color = Color.White,
                fontSize = (size.value * 0.14f).sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }

        // White paper with big bold day of month
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth,
                color = Color(0xFF222222),
                fontSize = (size.value * 0.44f).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PhotosIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF7F8FA), Color(0xFFD4D8DE))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height * 0.52f
        val petalLen = size.width * 0.32f

        // Draw sunflower petals
        val numPetals = 16
        for (i in 0 until numPetals) {
            val angle = (i * 360f / numPetals) * (PI / 180f).toFloat()
            val px = cx + cos(angle) * petalLen * 0.65f
            val py = cy + sin(angle) * petalLen * 0.65f

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0xFFFFDF38), Color(0xFFE08E00)),
                    center = Offset(px, py),
                    radius = petalLen * 0.45f
                ),
                radius = petalLen * 0.28f,
                center = Offset(px, py)
            )
        }

        // Sunflower dark center
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF4A3423), Color(0xFF241408)),
                center = Offset(cx, cy),
                radius = size.width * 0.2f
            ),
            radius = size.width * 0.18f,
            center = Offset(cx, cy)
        )
        // Center texture dots
        drawCircle(
            color = Color(0xFF6B4E36),
            radius = size.width * 0.12f,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )
    }
}

@Composable
private fun CameraIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE6E8EA), Color(0xFFB0B6BD))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width * 0.38f

        // Outer metallic bezel
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFE0E0E0), Color(0xFF70757B)),
                center = Offset(cx - r * 0.3f, cy - r * 0.3f),
                radius = r
            ),
            radius = r,
            center = Offset(cx, cy)
        )
        // Inner black lens body
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF2A2D32), Color(0xFF0F1113)),
                center = Offset(cx, cy),
                radius = r * 0.85f
            ),
            radius = r * 0.82f,
            center = Offset(cx, cy)
        )
        // Deep lens aperture with blue tint reflection
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF1E3C58), Color(0xFF08121C)),
                center = Offset(cx, cy),
                radius = r * 0.55f
            ),
            radius = r * 0.55f,
            center = Offset(cx, cy)
        )
        // Concentric aperture rings
        drawCircle(
            color = Color(0xFF445060),
            radius = r * 0.38f,
            center = Offset(cx, cy),
            style = Stroke(width = 1.5f)
        )
        // Specular cyan/white lens glare
        drawCircle(
            color = Color(0x99A8DCFF),
            radius = r * 0.15f,
            center = Offset(cx - r * 0.22f, cy - r * 0.22f)
        )
    }
}

@Composable
private fun YouTubeIcon() {
    // Retro CRT Television!
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFB57D47), Color(0xFF6B4219), Color(0xFF45270A))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Outer bezel lines (vintage wood grain)
        drawRect(
            color = Color(0x33000000),
            topLeft = Offset(0f, 0f),
            size = Size(w, h),
            style = Stroke(width = 3f)
        )

        // TV screen surround
        val screenLeft = w * 0.12f
        val screenTop = h * 0.16f
        val screenW = w * 0.58f
        val screenH = h * 0.68f

        // Screen bezel
        drawRoundRect(
            color = Color(0xFF2C241D),
            topLeft = Offset(screenLeft - 2f, screenTop - 2f),
            size = Size(screenW + 4f, screenH + 4f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // CRT Screen glass gradient
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF8C939E), Color(0xFF3F4650), Color(0xFF1E2228))
            ),
            topLeft = Offset(screenLeft, screenTop),
            size = Size(screenW, screenH),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // CRT screen scanlines
        for (y in 0 until (screenH / 4).toInt()) {
            drawLine(
                color = Color.Black.copy(alpha = 0.15f),
                start = Offset(screenLeft, screenTop + y * 4f),
                end = Offset(screenLeft + screenW, screenTop + y * 4f),
                strokeWidth = 1f
            )
        }

        // TV Control Panel on right
        val knobX = w * 0.83f
        // Top Knob
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFE8D099), Color(0xFF7A5826)),
                center = Offset(knobX - 2f, h * 0.32f - 2f),
                radius = w * 0.08f
            ),
            radius = w * 0.075f,
            center = Offset(knobX, h * 0.32f)
        )
        // Bottom Knob
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFE8D099), Color(0xFF7A5826)),
                center = Offset(knobX - 2f, h * 0.56f - 2f),
                radius = w * 0.08f
            ),
            radius = w * 0.075f,
            center = Offset(knobX, h * 0.56f)
        )
        // Speaker grille slots
        for (i in 0..2) {
            drawLine(
                color = Color(0xFF2C1908),
                start = Offset(w * 0.77f, h * 0.72f + i * 5f),
                end = Offset(w * 0.89f, h * 0.72f + i * 5f),
                strokeWidth = 2f
            )
        }
    }
}

@Composable
private fun StocksIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1C2738), Color(0xFF0D141F))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Stock coordinate grid
        val gridStep = w / 5f
        for (i in 1..4) {
            drawLine(
                color = Color(0x334B6B94),
                start = Offset(i * gridStep, 0f),
                end = Offset(i * gridStep, h),
                strokeWidth = 1f
            )
            drawLine(
                color = Color(0x334B6B94),
                start = Offset(0f, i * gridStep),
                end = Offset(w, i * gridStep),
                strokeWidth = 1f
            )
        }

        // Luminous stock line chart
        val points = listOf(
            Offset(0f, h * 0.75f),
            Offset(w * 0.15f, h * 0.68f),
            Offset(w * 0.32f, h * 0.78f),
            Offset(w * 0.48f, h * 0.38f),
            Offset(w * 0.65f, h * 0.46f),
            Offset(w * 0.82f, h * 0.28f),
            Offset(w, h * 0.35f)
        )

        val fillPath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (p in points) {
                lineTo(p.x, p.y)
            }
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                listOf(Color(0x663399FF), Color(0x11112244)),
                startY = h * 0.25f,
                endY = h
            )
        )

        // Line with neon glow
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (p in points) {
                lineTo(p.x, p.y)
            }
        }
        drawPath(
            path = linePath,
            color = Color(0xFF66B3FF),
            style = Stroke(width = 4.5f, cap = StrokeCap.Round)
        )
        drawPath(
            path = linePath,
            color = Color.White,
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun MapsIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE8E5DC), Color(0xFFD6D0C2))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Paper map folding creases
        drawLine(
            color = Color.Black.copy(alpha = 0.15f),
            start = Offset(w * 0.33f, 0f),
            end = Offset(w * 0.33f, h),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.3f),
            start = Offset(w * 0.33f + 1f, 0f),
            end = Offset(w * 0.33f + 1f, h),
            strokeWidth = 1f
        )
        drawLine(
            color = Color.Black.copy(alpha = 0.15f),
            start = Offset(w * 0.66f, 0f),
            end = Offset(w * 0.66f, h),
            strokeWidth = 2f
        )

        // Yellow highway road
        val roadPath = Path().apply {
            moveTo(0f, h * 0.55f)
            cubicTo(
                w * 0.4f, h * 0.5f,
                w * 0.6f, h * 0.3f,
                w, h * 0.25f
            )
        }
        drawPath(
            path = roadPath,
            color = Color(0xFFE08D16),
            style = Stroke(width = 16f)
        )
        drawPath(
            path = roadPath,
            color = Color(0xFFFFD447),
            style = Stroke(width = 12f)
        )

        // Interstate 280 shield
        val shieldX = w * 0.65f
        val shieldY = h * 0.28f
        val sW = w * 0.26f
        val sH = h * 0.22f

        drawRoundRect(
            color = Color(0xFF1E5BB5),
            topLeft = Offset(shieldX - sW / 2, shieldY - sH / 2),
            size = Size(sW, sH),
            cornerRadius = CornerRadius(6f, 6f)
        )
        // Red top of shield
        drawRoundRect(
            color = Color(0xFFD62929),
            topLeft = Offset(shieldX - sW / 2, shieldY - sH / 2),
            size = Size(sW, sH * 0.4f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Red Pushpin
        val pinX = w * 0.38f
        val pinY = h * 0.58f

        // Pin shadow
        drawCircle(
            color = Color.Black.copy(alpha = 0.35f),
            radius = 6f,
            center = Offset(pinX + 7f, pinY + 12f)
        )
        // Pin needle
        drawLine(
            color = Color(0xFFC0C0C0),
            start = Offset(pinX, pinY),
            end = Offset(pinX + 5f, pinY + 10f),
            strokeWidth = 2.5f
        )
        // Pin head (red glossy bulb)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFFF5252), Color(0xFFB71C1C)),
                center = Offset(pinX - 2f, pinY - 2f),
                radius = 12f
            ),
            radius = 9f,
            center = Offset(pinX, pinY)
        )
    }
}

@Composable
private fun WeatherIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF3888E0), Color(0xFF6AB6F8), Color(0xFF98D1FF))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Golden Sun
        val sunX = w * 0.55f
        val sunY = h * 0.42f
        val sunR = w * 0.22f

        // Sun glow rays
        for (i in 0 until 12) {
            val a = (i * 30f) * (PI / 180f).toFloat()
            val x1 = sunX + cos(a) * (sunR * 1.15f)
            val y1 = sunY + sin(a) * (sunR * 1.15f)
            val x2 = sunX + cos(a) * (sunR * 1.45f)
            val y2 = sunY + sin(a) * (sunR * 1.45f)
            drawLine(
                color = Color(0xFFFFE066).copy(alpha = 0.8f),
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        // Sun disc
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFFFEE70), Color(0xFFFFAE19)),
                center = Offset(sunX - sunR * 0.3f, sunY - sunR * 0.3f),
                radius = sunR
            ),
            radius = sunR,
            center = Offset(sunX, sunY)
        )

        // Fluffy cloud
        val cloudY = h * 0.65f
        drawCircle(
            color = Color.White,
            radius = w * 0.16f,
            center = Offset(w * 0.4f, cloudY)
        )
        drawCircle(
            color = Color.White,
            radius = w * 0.22f,
            center = Offset(w * 0.6f, cloudY - 4f)
        )
        drawCircle(
            color = Color.White,
            radius = w * 0.17f,
            center = Offset(w * 0.8f, cloudY + 2f)
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.28f, cloudY),
            size = Size(w * 0.58f, w * 0.20f),
            cornerRadius = CornerRadius(10f, 10f)
        )
    }

    // Live temperature overlay "23°"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 6.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "23°",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = Offset(0f, 2f),
                    blurRadius = 3f
                )
            )
        )
    }
}

@Composable
private fun NotesIcon(size: Dp) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9B8)) // Yellow legal pad
    ) {
        val w = this.size.width
        val h = this.size.height

        // Ruled horizontal blue lines
        val lineSpacing = h * 0.13f
        var curY = h * 0.36f
        while (curY < h) {
            drawLine(
                color = Color(0x554287F5),
                start = Offset(0f, curY),
                end = Offset(w, curY),
                strokeWidth = 1.5f
            )
            curY += lineSpacing
        }

        // Red vertical left margin line
        drawLine(
            color = Color(0x88E53935),
            start = Offset(w * 0.22f, h * 0.25f),
            end = Offset(w * 0.22f, h),
            strokeWidth = 1.5f
        )

        // Brown leather top binding
        val bindingH = h * 0.25f
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF5C3615), Color(0xFF381F0A))
            ),
            topLeft = Offset(0f, 0f),
            size = Size(w, bindingH)
        )

        // White stitches on leather binding
        val stitchY = bindingH * 0.65f
        var sx = w * 0.08f
        while (sx < w * 0.92f) {
            drawLine(
                color = Color(0xCCDDD2B8),
                start = Offset(sx, stitchY),
                end = Offset(sx + 5f, stitchY),
                strokeWidth = 2f
            )
            sx += 9f
        }
    }
}

@Composable
private fun UtilitiesFolderIcon(folderApps: List<LauncherApp>, size: Dp) {
    // Dark woven linen folder preview
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF30343A), Color(0xFF1A1C20))
                )
            )
            .padding(size * 0.12f),
        contentAlignment = Alignment.Center
    ) {
        // 2x2 grid of mini preview icons
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                MiniIconPreview(Color(0xFF2C2C2E), "⏱")
                MiniIconPreview(Color(0xFFE8912D), "±")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                MiniIconPreview(Color(0xFF1C1C1E), "🧭")
                MiniIconPreview(Color(0xFF32363E), "🎙")
            }
        }
    }
}

@Composable
private fun MiniIconPreview(color: Color, symbol: String) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(0.5.dp, Color.White.copy(0.2f), RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ITunesIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFBE3EAF), Color(0xFF7E1C7A), Color(0xFF4A0A48))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width

        // Radial burst rays
        val numRays = 24
        for (i in 0 until numRays) {
            val a1 = (i * (360f / numRays)) * (PI / 180f).toFloat()
            val a2 = ((i + 0.5f) * (360f / numRays)) * (PI / 180f).toFloat()
            val p = Path().apply {
                moveTo(cx, cy)
                lineTo(cx + cos(a1) * w, cy + sin(a1) * w)
                lineTo(cx + cos(a2) * w, cy + sin(a2) * w)
                close()
            }
            drawPath(
                path = p,
                color = Color.White.copy(alpha = if (i % 2 == 0) 0.12f else 0.02f)
            )
        }

        // White musical notes (double eighth note)
        val noteR = w * 0.085f
        val note1X = cx - w * 0.16f
        val note1Y = cy + w * 0.12f
        val note2X = cx + w * 0.16f
        val note2Y = cy + w * 0.04f

        // Note heads
        drawCircle(color = Color.White, radius = noteR, center = Offset(note1X, note1Y))
        drawCircle(color = Color.White, radius = noteR, center = Offset(note2X, note2Y))

        // Stems
        drawLine(
            color = Color.White,
            start = Offset(note1X + noteR * 0.8f, note1Y),
            end = Offset(note1X + noteR * 0.8f, cy - w * 0.22f),
            strokeWidth = 4f
        )
        drawLine(
            color = Color.White,
            start = Offset(note2X + noteR * 0.8f, note2Y),
            end = Offset(note2X + noteR * 0.8f, cy - w * 0.30f),
            strokeWidth = 4f
        )

        // Beam
        val beamPath = Path().apply {
            moveTo(note1X + noteR * 0.8f - 2f, cy - w * 0.22f)
            lineTo(note2X + noteR * 0.8f + 2f, cy - w * 0.30f)
            lineTo(note2X + noteR * 0.8f + 2f, cy - w * 0.22f)
            lineTo(note1X + noteR * 0.8f - 2f, cy - w * 0.14f)
            close()
        }
        drawPath(path = beamPath, color = Color.White)
    }
}

@Composable
private fun AppStoreIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF389EF2), Color(0xFF1A6ECC), Color(0xFF0F4E99))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width

        // Radial burst rays
        val numRays = 28
        for (i in 0 until numRays) {
            val a1 = (i * (360f / numRays)) * (PI / 180f).toFloat()
            val a2 = ((i + 0.5f) * (360f / numRays)) * (PI / 180f).toFloat()
            val p = Path().apply {
                moveTo(cx, cy)
                lineTo(cx + cos(a1) * w, cy + sin(a1) * w)
                lineTo(cx + cos(a2) * w, cy + sin(a2) * w)
                close()
            }
            drawPath(
                path = p,
                color = Color.White.copy(alpha = if (i % 2 == 0) 0.10f else 0.02f)
            )
        }

        // Circular badge surround
        drawCircle(
            color = Color.White.copy(alpha = 0.18f),
            radius = w * 0.36f,
            center = Offset(cx, cy),
            style = Stroke(width = 3f)
        )

        // Crossing Pencil, Ruler, Brush ("A" logo)
        // Left stick
        drawLine(
            color = Color.White,
            start = Offset(cx - w * 0.22f, cy + w * 0.22f),
            end = Offset(cx + w * 0.08f, cy - w * 0.24f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Right stick
        drawLine(
            color = Color.White,
            start = Offset(cx + w * 0.22f, cy + w * 0.22f),
            end = Offset(cx - w * 0.08f, cy - w * 0.24f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Crossbar
        drawLine(
            color = Color.White,
            start = Offset(cx - w * 0.16f, cy + w * 0.08f),
            end = Offset(cx + w * 0.16f, cy + w * 0.08f),
            strokeWidth = 4.5f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun GameCenterIcon() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            // Quadrant 1: Brown wood / Chess Knight
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFF8D5B28)),
                contentAlignment = Alignment.Center
            ) {
                Text("♞", color = Color.White, fontSize = 16.sp)
            }
            // Quadrant 2: Green turf / Baseball bat
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFF43A047)),
                contentAlignment = Alignment.Center
            ) {
                Text("⚾", color = Color.White, fontSize = 15.sp)
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            // Quadrant 3: Blue space / Rocket
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFF1E88E5)),
                contentAlignment = Alignment.Center
            ) {
                Text("🚀", color = Color.White, fontSize = 15.sp)
            }
            // Quadrant 4: Orange / Target
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFFFB8C00)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎯", color = Color.White, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun SettingsIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF383C42), Color(0xFF1E2024), Color(0xFF121316))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width

        // Brushed mechanical gear teeth
        val numTeeth = 10
        val rOuter = w * 0.36f
        val rInner = w * 0.28f

        for (i in 0 until numTeeth) {
            val a = (i * 36f) * (PI / 180f).toFloat()
            val tx = cx + cos(a) * rOuter
            val ty = cy + sin(a) * rOuter

            drawCircle(
                color = Color(0xFFC0C4C8),
                radius = w * 0.075f,
                center = Offset(tx, ty)
            )
        }

        // Silver gear body
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFE6EAEE), Color(0xFF888E96)),
                center = Offset(cx - rInner * 0.2f, cy - rInner * 0.2f),
                radius = rInner
            ),
            radius = rInner,
            center = Offset(cx, cy)
        )

        // Center hub
        drawCircle(
            color = Color(0xFF22252A),
            radius = w * 0.12f,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color(0xFF80868E),
            radius = w * 0.05f,
            center = Offset(cx, cy)
        )
    }
}

// -------------------------------------------------------------
// DOCK ICONS (PHONE, MAIL, SAFARI, IPOD)
// -------------------------------------------------------------

@Composable
private fun PhoneIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF67DE52), Color(0xFF39B524), Color(0xFF208C10))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Iconic diagonal texture stripes
        for (i in -10..20) {
            val offset = i * (w / 7f)
            drawLine(
                color = Color.White.copy(alpha = 0.09f),
                start = Offset(offset, 0f),
                end = Offset(offset + w, h),
                strokeWidth = 2.5f
            )
        }

        // White telephone handset
        val cx = w * 0.48f
        val cy = h * 0.50f

        // Classic handset silhouette
        val receiverPath = Path().apply {
            moveTo(cx - w * 0.18f, cy - h * 0.22f)
            // earpiece
            cubicTo(
                cx - w * 0.26f, cy - h * 0.14f,
                cx - w * 0.26f, cy - h * 0.04f,
                cx - w * 0.16f, cy + h * 0.04f
            )
            // handle
            cubicTo(
                cx - w * 0.06f, cy + h * 0.12f,
                cx + w * 0.04f, cy + h * 0.20f,
                cx + w * 0.16f, cy + h * 0.22f
            )
            // mouthpiece
            cubicTo(
                cx + w * 0.26f, cy + h * 0.14f,
                cx + w * 0.24f, cy + h * 0.04f,
                cx + w * 0.14f, cy + h * 0.06f
            )
            // inner curve
            cubicTo(
                cx + w * 0.04f, cy + h * 0.06f,
                cx - w * 0.04f, cy - h * 0.04f,
                cx - w * 0.06f, cy - h * 0.14f
            )
            close()
        }

        // Handset shadow
        drawPath(
            path = receiverPath,
            color = Color(0x33000000)
        )
        // Handset body
        drawPath(
            path = receiverPath,
            brush = Brush.verticalGradient(
                listOf(Color.White, Color(0xFFEBEBEB))
            )
        )
    }
}

@Composable
private fun MailIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4FA5F5), Color(0xFF2879D8), Color(0xFF135AB5))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Soft cloud in background
        drawCircle(
            color = Color.White.copy(alpha = 0.25f),
            radius = w * 0.26f,
            center = Offset(w * 0.5f, h * 0.35f)
        )

        // Envelope dimensions
        val envL = w * 0.15f
        val envT = h * 0.30f
        val envW = w * 0.70f
        val envH = h * 0.46f

        // Envelope shadow
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.35f),
            topLeft = Offset(envL + 2f, envT + 4f),
            size = Size(envW, envH),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Envelope base
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFFFFFF), Color(0xFFE6ECF2))
            ),
            topLeft = Offset(envL, envT),
            size = Size(envW, envH),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Blue border flap lines
        val flapPath = Path().apply {
            moveTo(envL, envT)
            lineTo(envL + envW / 2f, envT + envH * 0.62f)
            lineTo(envL + envW, envT)
        }
        drawPath(
            path = flapPath,
            color = Color(0xFF90A8C0),
            style = Stroke(width = 2f)
        )

        val bottomCrease = Path().apply {
            moveTo(envL, envT + envH)
            lineTo(envL + envW * 0.38f, envT + envH * 0.48f)
            moveTo(envL + envW, envT + envH)
            lineTo(envL + envW * 0.62f, envT + envH * 0.48f)
        }
        drawPath(
            path = bottomCrease,
            color = Color(0xFFB4C4D4),
            style = Stroke(width = 1.5f)
        )
    }
}

@Composable
private fun SafariIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF5BA3E8), Color(0xFF2368BD), Color(0xFF10488F))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width
        val r = w * 0.38f

        // Outer silver compass bezel
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFEAEAEA), Color(0xFF707882)),
                center = Offset(cx - r * 0.3f, cy - r * 0.3f),
                radius = r
            ),
            radius = r,
            center = Offset(cx, cy)
        )

        // Navy blue compass dial
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF1E528E), Color(0xFF0C2B54)),
                center = Offset(cx, cy),
                radius = r * 0.88f
            ),
            radius = r * 0.88f,
            center = Offset(cx, cy)
        )

        // Compass tick marks
        for (i in 0 until 36) {
            val a = (i * 10f) * (PI / 180f).toFloat()
            val len = if (i % 9 == 0) r * 0.18f else if (i % 3 == 0) r * 0.12f else r * 0.07f
            val x1 = cx + cos(a) * (r * 0.84f - len)
            val y1 = cy + sin(a) * (r * 0.84f - len)
            val x2 = cx + cos(a) * (r * 0.84f)
            val y2 = cy + sin(a) * (r * 0.84f)
            drawLine(
                color = Color.White.copy(alpha = if (i % 9 == 0) 0.9f else 0.5f),
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = if (i % 9 == 0) 2.5f else 1.2f
            )
        }

        // Compass needle tilted ~45 degrees
        // North (Red)
        val northNeedle = Path().apply {
            moveTo(cx, cy)
            lineTo(cx - r * 0.15f, cy + r * 0.10f)
            lineTo(cx + r * 0.65f, cy - r * 0.65f)
            close()
        }
        drawPath(
            path = northNeedle,
            brush = Brush.linearGradient(
                listOf(Color(0xFFFF3B30), Color(0xFFB51010))
            )
        )

        // South (White)
        val southNeedle = Path().apply {
            moveTo(cx, cy)
            lineTo(cx + r * 0.15f, cy - r * 0.10f)
            lineTo(cx - r * 0.65f, cy + r * 0.65f)
            close()
        }
        drawPath(
            path = southNeedle,
            brush = Brush.linearGradient(
                listOf(Color(0xFFFFFFFF), Color(0xFFC0C0C0))
            )
        )

        // Center pivot pin
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFE8D099), Color(0xFF6B4E1B)),
                center = Offset(cx - 1f, cy - 1f),
                radius = w * 0.06f
            ),
            radius = w * 0.05f,
            center = Offset(cx, cy)
        )
    }
}

@Composable
private fun IPodIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFB338), Color(0xFFFA8719), Color(0xFFD95000))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width
        val h = size.height

        // Radial burst rays in background
        val numRays = 24
        for (i in 0 until numRays) {
            val a1 = (i * (360f / numRays)) * (PI / 180f).toFloat()
            val a2 = ((i + 0.5f) * (360f / numRays)) * (PI / 180f).toFloat()
            val p = Path().apply {
                moveTo(cx, cy)
                lineTo(cx + cos(a1) * w, cy + sin(a1) * w)
                lineTo(cx + cos(a2) * w, cy + sin(a2) * w)
                close()
            }
            drawPath(
                path = p,
                color = Color.White.copy(alpha = if (i % 2 == 0) 0.12f else 0.02f)
            )
        }

        // Classic White iPod silhouette
        val podW = w * 0.48f
        val podH = h * 0.72f
        val podL = cx - podW / 2f
        val podT = cy - podH / 2f

        // iPod drop shadow
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.35f),
            topLeft = Offset(podL + 2f, podT + 4f),
            size = Size(podW, podH),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // iPod White/Silver Body
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFFFFFF), Color(0xFFE2E4E8))
            ),
            topLeft = Offset(podL, podT),
            size = Size(podW, podH),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // iPod Screen
        val scrW = podW * 0.82f
        val scrH = podH * 0.38f
        val scrL = podL + (podW - scrW) / 2f
        val scrT = podT + podH * 0.08f
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF6A7480), Color(0xFF384048))
            ),
            topLeft = Offset(scrL, scrT),
            size = Size(scrW, scrH),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // iPod Click Wheel
        val wheelY = podT + podH * 0.72f
        val wheelR = podW * 0.32f
        drawCircle(
            color = Color(0xFFE8E8E8),
            radius = wheelR,
            center = Offset(cx, wheelY)
        )
        // Click Wheel center button
        drawCircle(
            color = Color(0xFFFFFFFF),
            radius = wheelR * 0.38f,
            center = Offset(cx, wheelY)
        )
    }
}

// -------------------------------------------------------------
// UTILITIES FOLDER APPS
// -------------------------------------------------------------

@Composable
private fun ClockIcon() {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR)
    val minute = cal.get(Calendar.MINUTE)
    val second = cal.get(Calendar.SECOND)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF282828), Color(0xFF141414))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width * 0.42f

        // White bezel ring
        drawCircle(
            color = Color.White,
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = 3f)
        )

        // 12 Tick marks
        for (i in 0 until 12) {
            val a = (i * 30f - 90f) * (PI / 180f).toFloat()
            val len = if (i % 3 == 0) r * 0.20f else r * 0.10f
            val x1 = cx + cos(a) * (r - len)
            val y1 = cy + sin(a) * (r - len)
            val x2 = cx + cos(a) * (r - 2f)
            val y2 = cy + sin(a) * (r - 2f)
            drawLine(
                color = Color.White,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = if (i % 3 == 0) 3f else 1.5f
            )
        }

        // Hour hand
        val hourAngle = ((hour + minute / 60f) * 30f - 90f) * (PI / 180f).toFloat()
        drawLine(
            color = Color.White,
            start = Offset(cx, cy),
            end = Offset(cx + cos(hourAngle) * r * 0.5f, cy + sin(hourAngle) * r * 0.5f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Minute hand
        val minAngle = (minute * 6f - 90f) * (PI / 180f).toFloat()
        drawLine(
            color = Color.White,
            start = Offset(cx, cy),
            end = Offset(cx + cos(minAngle) * r * 0.72f, cy + sin(minAngle) * r * 0.72f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )

        // Second hand (orange/red)
        val secAngle = (second * 6f - 90f) * (PI / 180f).toFloat()
        drawLine(
            color = Color(0xFFFF5252),
            start = Offset(cx - cos(secAngle) * r * 0.15f, cy - sin(secAngle) * r * 0.15f),
            end = Offset(cx + cos(secAngle) * r * 0.82f, cy + sin(secAngle) * r * 0.82f),
            strokeWidth = 1.5f
        )
        drawCircle(color = Color(0xFFFF5252), radius = 3.5f, center = Offset(cx, cy))
    }
}

@Composable
private fun CalculatorIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF383838), Color(0xFF222222))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Calculator display at top
        drawRoundRect(
            color = Color(0xFF1E241E),
            topLeft = Offset(w * 0.12f, h * 0.12f),
            size = Size(w * 0.76f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        // Green numeric segments glow
        drawRect(
            color = Color(0xAA98FB98),
            topLeft = Offset(w * 0.62f, h * 0.18f),
            size = Size(w * 0.20f, h * 0.10f)
        )

        // Round Buttons 3x3 grid
        val btnR = w * 0.08f
        val startX = w * 0.25f
        val startY = h * 0.48f
        val step = w * 0.25f

        for (row in 0..2) {
            for (col in 0..2) {
                val bx = startX + col * step
                val by = startY + row * step
                val isOperator = col == 2
                drawCircle(
                    color = if (isOperator) Color(0xFFFA8C16) else Color(0xFF555555),
                    radius = btnR,
                    center = Offset(bx, by)
                )
            }
        }
    }
}

@Composable
private fun CompassIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF222222), Color(0xFF0A0A0A))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width * 0.38f

        // Outer white tick circle
        drawCircle(
            color = Color.White.copy(alpha = 0.8f),
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )

        // Compass crosshairs
        drawLine(
            color = Color.White.copy(alpha = 0.3f),
            start = Offset(cx, cy - r),
            end = Offset(cx, cy + r),
            strokeWidth = 1f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.3f),
            start = Offset(cx - r, cy),
            end = Offset(cx + r, cy),
            strokeWidth = 1f
        )

        // North needle (white with red tip)
        val needlePath = Path().apply {
            moveTo(cx, cy)
            lineTo(cx - 5f, cy + 8f)
            lineTo(cx, cy - r * 0.75f)
            lineTo(cx + 5f, cy + 8f)
            close()
        }
        drawPath(path = needlePath, color = Color.White)
        drawCircle(color = Color(0xFFFF3B30), radius = 4f, center = Offset(cx, cy - r * 0.75f))
    }
}

@Composable
private fun VoiceMemosIcon() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF424854), Color(0xFF23272F))
                )
            )
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width

        // Vintage Microphone Capsule
        drawRoundRect(
            color = Color(0xFFC0C5CC),
            topLeft = Offset(cx - w * 0.16f, cy - w * 0.28f),
            size = Size(w * 0.32f, w * 0.42f),
            cornerRadius = CornerRadius(w * 0.16f, w * 0.16f)
        )
        // Mic Grille Lines
        for (i in 0..4) {
            drawLine(
                color = Color(0xFF3B4048),
                start = Offset(cx - w * 0.14f, cy - w * 0.20f + i * 8f),
                end = Offset(cx + w * 0.14f, cy - w * 0.20f + i * 8f),
                strokeWidth = 1.5f
            )
        }
        // Audio sound wave level meter
        val waveW = w * 0.7f
        val waveL = cx - waveW / 2f
        val waveY = cy + w * 0.26f
        for (i in 0..8) {
            val barH = if (i == 4) 14f else if (i in 3..5) 10f else 6f
            drawLine(
                color = Color(0xFFFF5252),
                start = Offset(waveL + i * (waveW / 8f), waveY - barH),
                end = Offset(waveL + i * (waveW / 8f), waveY + barH),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun InstalledAppIcon(app: LauncherApp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF0F2F5), Color(0xFFD6DBE2))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (app.appDrawable != null) {
            AsyncImage(
                model = app.appDrawable,
                contentDescription = app.title,
                modifier = Modifier.fillMaxSize(0.82f)
            )
        } else {
            // Fallback letter avatar
            Text(
                text = app.title.take(1).uppercase(),
                color = Color(0xFF1E528E),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
