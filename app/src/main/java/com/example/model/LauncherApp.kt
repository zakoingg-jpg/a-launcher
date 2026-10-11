package com.example.model

import android.graphics.drawable.Drawable

enum class AppIconType {
    MESSAGES,
    CALENDAR,
    PHOTOS,
    CAMERA,
    YOUTUBE,
    STOCKS,
    MAPS,
    WEATHER,
    NOTES,
    UTILITIES_FOLDER,
    ITUNES,
    APP_STORE,
    GAME_CENTER,
    SETTINGS,
    PHONE,
    MAIL,
    SAFARI,
    IPOD,
    CLOCK,
    CALCULATOR,
    COMPASS,
    VOICE_MEMOS,
    INSTALLED
}

data class LauncherApp(
    val id: String,
    val title: String,
    val iconType: AppIconType,
    val packageName: String? = null,
    val activityName: String? = null,
    val badgeCount: Int = 0,
    val isDock: Boolean = false,
    val folderApps: List<LauncherApp> = emptyList(),
    val appDrawable: Drawable? = null
)
