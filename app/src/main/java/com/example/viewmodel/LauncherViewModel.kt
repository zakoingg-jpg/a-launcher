package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.MediaStore
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppIconType
import com.example.model.AppLaunchOrigin
import com.example.model.LauncherApp
import com.example.ui.apps.WallpaperType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class ModalAppType {
    NONE,
    NOTES,
    WEATHER,
    CALCULATOR,
    IPOD,
    SETTINGS,
    ALL_APPS
}

data class LauncherUiState(
    val homeApps: List<LauncherApp> = emptyList(),
    val dockApps: List<LauncherApp> = emptyList(),
    val installedApps: List<LauncherApp> = emptyList(),
    val isJiggleMode: Boolean = false,
    val wallpaper: WallpaperType = WallpaperType.RAINDROPS,
    val activeModal: ModalAppType = ModalAppType.NONE,
    val activeFolder: LauncherApp? = null,
    val searchQuery: String = "",
    val isSpotlightActive: Boolean = false,
    val hapticsEnabled: Boolean = true,
    val activeLaunchOrigin: AppLaunchOrigin? = null
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        setupDefaultApps()
        loadInstalledApps()
    }

    private fun setupDefaultApps() {
        val folderApps = listOf(
            LauncherApp(id = "clock", title = "Clock", iconType = AppIconType.CLOCK),
            LauncherApp(id = "calc", title = "Calculator", iconType = AppIconType.CALCULATOR),
            LauncherApp(id = "compass", title = "Compass", iconType = AppIconType.COMPASS),
            LauncherApp(id = "voice", title = "Voice Memos", iconType = AppIconType.VOICE_MEMOS)
        )

        // The 14 exact icons from the user's iOS 4 screenshot
        val defaultHome = listOf(
            LauncherApp(id = "messages", title = "Messages", iconType = AppIconType.MESSAGES, badgeCount = 1),
            LauncherApp(id = "calendar", title = "Calendar", iconType = AppIconType.CALENDAR),
            LauncherApp(id = "photos", title = "Photos", iconType = AppIconType.PHOTOS),
            LauncherApp(id = "camera", title = "Camera", iconType = AppIconType.CAMERA),
            LauncherApp(id = "youtube", title = "YouTube", iconType = AppIconType.YOUTUBE),
            LauncherApp(id = "stocks", title = "Stocks", iconType = AppIconType.STOCKS),
            LauncherApp(id = "maps", title = "Maps", iconType = AppIconType.MAPS),
            LauncherApp(id = "weather", title = "Weather", iconType = AppIconType.WEATHER),
            LauncherApp(id = "notes", title = "Notes", iconType = AppIconType.NOTES),
            LauncherApp(id = "utilities", title = "Utilities", iconType = AppIconType.UTILITIES_FOLDER, folderApps = folderApps),
            LauncherApp(id = "itunes", title = "iTunes", iconType = AppIconType.ITUNES),
            LauncherApp(id = "appstore", title = "App Store", iconType = AppIconType.APP_STORE),
            LauncherApp(id = "gamecenter", title = "Game Center", iconType = AppIconType.GAME_CENTER),
            LauncherApp(id = "settings", title = "Settings", iconType = AppIconType.SETTINGS)
        )

        // The 4 dock apps from the user's iOS 4 screenshot
        val defaultDock = listOf(
            LauncherApp(id = "phone", title = "Phone", iconType = AppIconType.PHONE, isDock = true),
            LauncherApp(id = "mail", title = "Mail", iconType = AppIconType.MAIL, isDock = true, badgeCount = 2),
            LauncherApp(id = "safari", title = "Safari", iconType = AppIconType.SAFARI, isDock = true),
            LauncherApp(id = "ipod", title = "iPod", iconType = AppIconType.IPOD, isDock = true)
        )

        _uiState.value = _uiState.value.copy(
            homeApps = defaultHome,
            dockApps = defaultDock
        )
    }

    private fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = getApplication<Application>().packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = pm.queryIntentActivities(intent, 0)
            val currentPkg = getApplication<Application>().packageName

            val list = resolveInfos
                .filter { it.activityInfo.packageName != currentPkg }
                .map { info ->
                    LauncherApp(
                        id = info.activityInfo.packageName,
                        title = info.loadLabel(pm).toString(),
                        iconType = AppIconType.INSTALLED,
                        packageName = info.activityInfo.packageName,
                        activityName = info.activityInfo.name,
                        appDrawable = info.loadIcon(pm)
                    )
                }
                .sortedBy { it.title.lowercase() }

            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(installedApps = list)
            }
        }
    }

    fun setJiggleMode(enabled: Boolean) {
        if (enabled && _uiState.value.hapticsEnabled) {
            vibrate(50)
        }
        _uiState.value = _uiState.value.copy(isJiggleMode = enabled)
    }

    fun setWallpaper(wallpaper: WallpaperType) {
        _uiState.value = _uiState.value.copy(wallpaper = wallpaper)
    }

    fun setHapticsEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(hapticsEnabled = enabled)
    }

    fun openModal(modal: ModalAppType, origin: AppLaunchOrigin? = null) {
        if (_uiState.value.hapticsEnabled) vibrate(20)
        _uiState.value = _uiState.value.copy(
            activeModal = modal,
            activeLaunchOrigin = origin
        )
    }

    fun closeModal() {
        _uiState.value = _uiState.value.copy(
            activeModal = ModalAppType.NONE,
            activeLaunchOrigin = null
        )
    }

    fun clearLaunchOrigin() {
        _uiState.value = _uiState.value.copy(activeLaunchOrigin = null)
    }

    fun openFolder(folder: LauncherApp) {
        if (_uiState.value.hapticsEnabled) vibrate(25)
        _uiState.value = _uiState.value.copy(activeFolder = folder)
    }

    fun closeFolder() {
        _uiState.value = _uiState.value.copy(activeFolder = null)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setSpotlightActive(active: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSpotlightActive = active,
            searchQuery = if (!active) "" else _uiState.value.searchQuery
        )
    }

    fun resetLayout() {
        setupDefaultApps()
    }

    fun removeHomeApp(appId: String) {
        val updated = _uiState.value.homeApps.filter { it.id != appId }
        _uiState.value = _uiState.value.copy(homeApps = updated)
    }

    fun launchApp(context: Context, app: LauncherApp, originBounds: Rect? = null) {
        if (_uiState.value.isJiggleMode) {
            setJiggleMode(false)
            return
        }

        if (_uiState.value.hapticsEnabled) vibrate(30)

        val launchOrigin = if (originBounds != null) {
            AppLaunchOrigin(app = app, bounds = originBounds)
        } else {
            null
        }

        when (app.iconType) {
            AppIconType.NOTES -> openModal(ModalAppType.NOTES, launchOrigin)
            AppIconType.WEATHER -> openModal(ModalAppType.WEATHER, launchOrigin)
            AppIconType.CALCULATOR -> openModal(ModalAppType.CALCULATOR, launchOrigin)
            AppIconType.IPOD -> openModal(ModalAppType.IPOD, launchOrigin)
            AppIconType.SETTINGS -> openModal(ModalAppType.SETTINGS, launchOrigin)
            AppIconType.UTILITIES_FOLDER -> openFolder(app)
            AppIconType.CLOCK -> openModal(ModalAppType.NONE).also {
                tryLaunchSystemIntent(context, Intent(android.provider.AlarmClock.ACTION_SHOW_ALARMS))
            }
            AppIconType.PHONE -> {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                tryLaunchSystemIntent(context, dialIntent)
            }
            AppIconType.CAMERA -> {
                val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                tryLaunchSystemIntent(context, camIntent)
            }
            AppIconType.SAFARI -> {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                tryLaunchSystemIntent(context, webIntent)
            }
            AppIconType.MAIL -> {
                val mailIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_EMAIL)
                }
                tryLaunchSystemIntent(context, mailIntent)
            }
            AppIconType.MAPS -> {
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=San+Francisco"))
                tryLaunchSystemIntent(context, mapIntent)
            }
            AppIconType.PHOTOS -> {
                val photoIntent = Intent(Intent.ACTION_VIEW).apply {
                    type = "image/*"
                }
                tryLaunchSystemIntent(context, photoIntent)
            }
            AppIconType.CALENDAR -> {
                val calIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_CALENDAR)
                }
                tryLaunchSystemIntent(context, calIntent)
            }
            AppIconType.MESSAGES -> {
                val smsIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_MESSAGING)
                }
                tryLaunchSystemIntent(context, smsIntent)
            }
            AppIconType.YOUTUBE -> {
                val ytIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                tryLaunchSystemIntent(context, ytIntent)
            }
            AppIconType.APP_STORE -> {
                val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store"))
                tryLaunchSystemIntent(context, playIntent)
            }
            AppIconType.ITUNES -> openModal(ModalAppType.IPOD, launchOrigin)
            AppIconType.GAME_CENTER -> openModal(ModalAppType.SETTINGS, launchOrigin)
            AppIconType.COMPASS, AppIconType.VOICE_MEMOS -> openModal(ModalAppType.SETTINGS, launchOrigin)
            AppIconType.STOCKS -> {
                val stockIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://finance.yahoo.com"))
                tryLaunchSystemIntent(context, stockIntent)
            }
            AppIconType.INSTALLED -> {
                if (app.packageName != null) {
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                    if (launchIntent != null) {
                        context.startActivity(launchIntent)
                    }
                }
            }
            else -> openModal(ModalAppType.SETTINGS, launchOrigin)
        }
    }

    private fun tryLaunchSystemIntent(context: Context, intent: Intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // If system app isn't found, open settings as fallback
            openModal(ModalAppType.SETTINGS)
        }
    }

    private fun vibrate(ms: Long) {
        try {
            val ctx = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(ms)
                }
            }
        } catch (_: Exception) {}
    }
}
