package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class WeatherDay(
    val day: String,
    val icon: String,
    val high: String,
    val low: String
)

@Composable
fun RetroWeatherApp(
    onClose: () -> Unit
) {
    val forecast = listOf(
        WeatherDay("Wednesday", "☀️", "26°", "15°"),
        WeatherDay("Thursday", "⛅", "24°", "14°"),
        WeatherDay("Friday", "🌧️", "20°", "12°"),
        WeatherDay("Saturday", "☀️", "25°", "16°"),
        WeatherDay("Sunday", "☀️", "27°", "17°"),
        WeatherDay("Monday", "🌤️", "23°", "13°")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF205B9E),
                        Color(0xFF3885CE),
                        Color(0xFF5AA6EA),
                        Color(0xFF83C3FA)
                    )
                )
            )
    ) {
        // Navigation bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0x88FFFFFF), Color(0x33FFFFFF))
                        )
                    )
                    .border(1.dp, Color.White.copy(0.4f), RoundedCornerShape(6.dp))
                    .clickable { onClose() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("◀ Home", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // City & Temperature Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cupertino",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                style = TextStyle(
                    shadow = Shadow(Color.Black.copy(0.4f), Offset(0f, 2f), 4f)
                )
            )
            Text(
                text = "Mostly Sunny",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp
            )

            Text(
                text = "23°",
                color = Color.White,
                fontSize = 80.sp,
                fontWeight = FontWeight.Thin,
                style = TextStyle(
                    shadow = Shadow(Color.Black.copy(0.3f), Offset(0f, 3f), 6f)
                )
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("H: 26°", color = Color.White, fontSize = 14.sp)
                Text("L: 15°", color = Color.White.copy(0.7f), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forecast Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.25f))
                .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "6-DAY FORECAST",
                    color = Color.White.copy(0.6f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                forecast.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.day,
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1.5f)
                        )
                        Text(
                            text = item.icon,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(item.high, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(item.low, color = Color.White.copy(0.6f), fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
