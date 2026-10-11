package com.example.ui.apps

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class WallpaperType {
    RAINDROPS,
    DARK_LINEN,
    EARTH,
    PURPLE_NEBULA
}

@Composable
fun RetroSettingsApp(
    currentWallpaper: WallpaperType,
    onWallpaperSelected: (WallpaperType) -> Unit,
    hapticsEnabled: Boolean,
    onHapticsToggle: (Boolean) -> Unit,
    onOpenAllApps: () -> Unit,
    onResetIcons: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC7CCD4)) // Classic iOS 4 striped gray background
    ) {
        // Metallic Navigation Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF8E9BAE), Color(0xFF5A687D))
                    )
                )
                .border(0.5.dp, Color(0xFF424E60))
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
                    text = "Settings",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(Color.Black.copy(0.8f), Offset(0f, 1f), 2f)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(60.dp))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Group: Wallpapers
            item {
                SettingsSectionHeader("WALLPAPER")
                SettingsGroup {
                    WallpaperOptionItem(
                        name = "Water Droplets (Default iOS 4)",
                        isSelected = currentWallpaper == WallpaperType.RAINDROPS,
                        onClick = { onWallpaperSelected(WallpaperType.RAINDROPS) }
                    )
                    SettingsDivider()
                    WallpaperOptionItem(
                        name = "Classic Dark Linen",
                        isSelected = currentWallpaper == WallpaperType.DARK_LINEN,
                        onClick = { onWallpaperSelected(WallpaperType.DARK_LINEN) }
                    )
                    SettingsDivider()
                    WallpaperOptionItem(
                        name = "Blue Earth Horizon",
                        isSelected = currentWallpaper == WallpaperType.EARTH,
                        onClick = { onWallpaperSelected(WallpaperType.EARTH) }
                    )
                    SettingsDivider()
                    WallpaperOptionItem(
                        name = "Cosmic Purple",
                        isSelected = currentWallpaper == WallpaperType.PURPLE_NEBULA,
                        onClick = { onWallpaperSelected(WallpaperType.PURPLE_NEBULA) }
                    )
                }
            }

            // Group: Launcher Behavior
            item {
                SettingsSectionHeader("LAUNCHER OPTIONS")
                SettingsGroup {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Vibration / Haptic Feedback", color = Color.Black, fontSize = 16.sp)
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = onHapticsToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF007AFF)
                            )
                        )
                    }
                    SettingsDivider()
                    SettingsActionItem(
                        title = "Set as Default Home App",
                        subtitle = "Open system default apps to set iOS 4 Launcher",
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val intent = Intent(Settings.ACTION_SETTINGS)
                                context.startActivity(intent)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsActionItem(
                        title = "Browse All Device Apps",
                        subtitle = "View and launch every installed app",
                        onClick = onOpenAllApps
                    )
                    SettingsDivider()
                    SettingsActionItem(
                        title = "Reset Home Screen Layout",
                        subtitle = "Restore default iOS 4 app positions",
                        onClick = onResetIcons
                    )
                }
            }

            // Group: About
            item {
                SettingsSectionHeader("ABOUT")
                SettingsGroup {
                    SettingsInfoRow("Theme", "iOS 4 (Classic Skeuomorphic)")
                    SettingsDivider()
                    SettingsInfoRow("Version", "4.3.3 (8J2)")
                    SettingsDivider()
                    SettingsInfoRow("Model", "iPhone 4,1 (Retro Edition)")
                    SettingsDivider()
                    SettingsInfoRow("Features", "Glass Shelf, Wiggle Mode, Folders")
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        color = Color(0xFF4A5568),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsGroup(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(0.5.dp, Color(0xFFB0B8C2), RoundedCornerShape(10.dp))
    ) {
        content()
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color(0xFFE2E4E8))
    )
}

@Composable
private fun WallpaperOptionItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, color = Color.Black, fontSize = 15.sp)
        if (isSelected) {
            Text("✓", color = Color(0xFF007AFF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SettingsActionItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
        Text("›", color = Color(0xFF8E8E93), fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Black, fontSize = 15.sp)
        Text(value, color = Color(0xFF8E8E93), fontSize = 14.sp)
    }
}
