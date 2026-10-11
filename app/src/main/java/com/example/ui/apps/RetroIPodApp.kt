package com.example.ui.apps

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun RetroIPodApp(
    onClose: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentTrack by remember { mutableStateOf("Viva La Vida") }
    var currentArtist by remember { mutableStateOf("Coldplay") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE2E4E8), Color(0xFFBCC2CC), Color(0xFF9EA6B4))
                )
            )
    ) {
        // Metallic Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF8E9BAE), Color(0xFF5A687D))
                    )
                )
                .border(1.dp, Color(0xFF424E60))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF6E7E94), Color(0xFF4A586E))
                            )
                        )
                        .border(1.dp, Color.White.copy(0.3f), RoundedCornerShape(6.dp))
                        .clickable { onClose() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("◀ Done", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Now Playing",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(Color.Black.copy(0.8f), Offset(0f, 1f), 2f)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(50.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Album Art in Square with subtle reflection
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .shadow(12.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF3F51B5), Color(0xFFE91E63), Color(0xFFFF9800))
                        )
                    )
                    .border(1.dp, Color.White.copy(0.4f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎵", fontSize = 54.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("iOS 4 Classics", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = currentTrack,
                color = Color(0xFF1E242E),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentArtist,
                color = Color(0xFF4E5868),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Scrubbing timeline
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF8B95A4))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(6.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF007AFF), Color(0xFF5AC8FA))
                            )
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1:42", color = Color(0xFF4A5565), fontSize = 11.sp)
                Text("-2:18", color = Color(0xFF4A5565), fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Classic Media Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⏮", fontSize = 28.sp, modifier = Modifier.clickable { currentTrack = "Clocks" })

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFFFFF), Color(0xFFCFD4DC))
                        )
                    )
                    .border(1.dp, Color(0xFF8E9BAE), CircleShape)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isPlaying) "⏸" else "▶",
                    fontSize = 24.sp,
                    color = Color(0xFF2C3440)
                )
            }

            Text("⏭", fontSize = 28.sp, modifier = Modifier.clickable { currentTrack = "Paradise" })
        }
    }
}
