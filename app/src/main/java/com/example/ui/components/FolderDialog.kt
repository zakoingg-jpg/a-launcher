package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LauncherApp
import com.example.ui.icons.IOS4Icon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IOS4FolderOverlay(
    folderApp: LauncherApp?,
    onDismiss: () -> Unit,
    onAppClick: (LauncherApp, Rect?) -> Unit
) {
    AnimatedVisibility(
        visible = folderApp != null,
        enter = fadeIn() + scaleIn(initialScale = 0.85f),
        exit = fadeOut() + scaleOut(targetScale = 0.85f)
    ) {
        if (folderApp == null) return@AnimatedVisibility

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Folder drawer with dark woven linen texture
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(16.dp, RoundedCornerShape(16.dp))
                    .clickable(enabled = false) {} // Prevent click-through
            ) {
                // Woven linen texture background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF22252A))
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val w = size.width
                        val h = size.height

                        // Criss-cross linen fibers
                        val step = 4f
                        var x = 0f
                        while (x < w) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.04f),
                                start = Offset(x, 0f),
                                end = Offset(x, h),
                                strokeWidth = 1f
                            )
                            x += step
                        }
                        var y = 0f
                        while (y < h) {
                            drawLine(
                                color = Color.Black.copy(alpha = 0.25f),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f
                            )
                            y += step
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Folder Title with embossed 3D text
                        Text(
                            text = folderApp.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.9f),
                                    offset = Offset(0f, 2f),
                                    blurRadius = 3f
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Apps inside the folder
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            folderApp.folderApps.forEach { subApp ->
                                var subBounds by remember { mutableStateOf<Rect?>(null) }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(68.dp)
                                        .combinedClickable(
                                            onClick = {
                                                onAppClick(subApp, subBounds)
                                                onDismiss()
                                            }
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier.onGloballyPositioned { coords ->
                                            subBounds = coords.boundsInRoot()
                                        }
                                    ) {
                                        IOS4Icon(
                                            app = subApp,
                                            size = 56.dp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = subApp.title,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            shadow = Shadow(
                                                color = Color.Black.copy(alpha = 0.9f),
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Close tip
                        Text(
                            text = "Tap outside to close",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
