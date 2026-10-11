package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IOS4StatusBar(
    isJiggleMode: Boolean,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember {
        mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            delay(1000)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black.copy(alpha = 0.55f),
                        Color.Black.copy(alpha = 0.35f)
                    )
                )
            )
            .padding(horizontal = 6.dp)
    ) {
        // Left: Carrier, Signal & WiFi
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "No SIM",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(0f, 1f),
                        blurRadius = 1f
                    )
                )
            )

            // Signal bars (5 bars)
            Canvas(modifier = Modifier.size(width = 18.dp, height = 10.dp)) {
                val barW = 2.2f
                val spacing = 1.2f
                for (i in 0 until 5) {
                    val barH = (i + 1) * 2f
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(i * (barW + spacing), size.height - barH),
                        size = Size(barW, barH),
                        cornerRadius = CornerRadius(0.5f, 0.5f)
                    )
                }
            }

            // WiFi icon
            Canvas(modifier = Modifier.size(12.dp)) {
                val cx = size.width / 2f
                val cy = size.height * 0.75f
                // Wifi dot
                drawCircle(color = Color.White, radius = 1.5f, center = Offset(cx, cy))
                // Waves
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(cx, cy),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 9f,
                    center = Offset(cx, cy),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2f)
                )
            }
        }

        // Center: Digital Clock
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(0f, 1f),
                        blurRadius = 1f
                    )
                )
            )
        }

        // Right: Battery + Percentage (or Done button if jiggle mode)
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isJiggleMode) {
                // Classic silver/gray glossy "Done" button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF788494), Color(0xFF38404C))
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .clickable { onDoneClick() }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Done",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = "97%",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.8f),
                            offset = Offset(0f, 1f),
                            blurRadius = 1f
                        )
                    )
                )

                // Battery Glyph
                Canvas(modifier = Modifier.size(width = 22.dp, height = 11.dp)) {
                    val w = size.width - 3f
                    val h = size.height

                    // Battery outer shell
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(0f, 0f),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(2.5f, 2.5f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2f)
                    )

                    // Battery terminal nipple
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w + 0.5f, h * 0.28f),
                        size = Size(2f, h * 0.44f),
                        cornerRadius = CornerRadius(0.8f, 0.8f)
                    )

                    // Battery charge level (97% green)
                    val fillW = (w - 3.5f) * 0.97f
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF62E048), Color(0xFF34A822))
                        ),
                        topLeft = Offset(1.8f, 1.8f),
                        size = Size(fillW, h - 3.6f),
                        cornerRadius = CornerRadius(1.5f, 1.5f)
                    )
                }
            }
        }
    }
}
