package com.example.ui.apps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RetroNotesApp(
    onClose: () -> Unit
) {
    var noteText by remember {
        mutableStateOf(
            "iOS 4 Launcher\n\n" +
            "• Skeuomorphic design\n" +
            "• 3D glass dock with reflection\n" +
            "• Classic water drops wallpaper\n" +
            "• Jiggle mode for icon reorder\n" +
            "• Spotlight search\n\n" +
            "Type your own nostalgic notes here..."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9B8)) // Yellow legal pad
    ) {
        // Brown stitched leather header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF5C3615), Color(0xFF381F0A))
                    )
                )
                .shadow(4.dp)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Stitches
            Canvas(modifier = Modifier.fillMaxWidth().height(4.dp).align(Alignment.BottomCenter)) {
                var x = 4f
                while (x < size.width) {
                    drawLine(
                        color = Color(0xCCDDD2B8),
                        start = Offset(x, 0f),
                        end = Offset(x + 6f, 0f),
                        strokeWidth = 2f
                    )
                    x += 10f
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back/Done button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF88572A), Color(0xFF4A2B0F))
                            )
                        )
                        .border(1.dp, Color(0xFFC09060), RoundedCornerShape(6.dp))
                        .clickable { onClose() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "◀ Done",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Notes",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(0.8f),
                            offset = Offset(0f, 2f),
                            blurRadius = 2f
                        )
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(60.dp))
            }
        }

        // Notepad area with ruled lines
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Ruled blue lines
                val lineGap = 32f
                var y = lineGap * 2
                while (y < h) {
                    drawLine(
                        color = Color(0x444287F5),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.2f
                    )
                    y += lineGap
                }

                // Red margin line
                drawLine(
                    color = Color(0x66E53935),
                    start = Offset(60f, 0f),
                    end = Offset(60f, h),
                    strokeWidth = 1.5f
                )
            }

            BasicTextField(
                value = noteText,
                onValueChange = { noteText = it },
                textStyle = TextStyle(
                    color = Color(0xFF2B2212),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Cursive,
                    lineHeight = 28.sp
                ),
                cursorBrush = SolidColor(Color(0xFF381F0A)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 72.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
            )
        }
    }
}
