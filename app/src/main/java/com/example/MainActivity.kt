package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.LauncherApp
import com.example.viewmodel.ModalAppType
import com.example.ui.animation.IOS4AppLaunchTransition
import com.example.ui.apps.RetroCalculatorApp
import com.example.ui.apps.RetroIPodApp
import com.example.ui.apps.RetroNotesApp
import com.example.ui.apps.RetroSettingsApp
import com.example.ui.apps.RetroWeatherApp
import com.example.ui.components.AllAppsDrawer
import com.example.ui.components.IOS4FolderOverlay
import com.example.ui.components.SpotlightSearchView
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LauncherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    IOS4LauncherRoot()
                }
            }
        }
    }
}

@Composable
fun IOS4LauncherRoot(
    viewModel: LauncherViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Back button behavior for a home launcher
    val hasActiveOverlay = uiState.isSpotlightActive ||
            uiState.activeModal != ModalAppType.NONE ||
            uiState.activeFolder != null ||
            uiState.isJiggleMode

    BackHandler(enabled = hasActiveOverlay) {
        when {
            uiState.isSpotlightActive -> viewModel.setSpotlightActive(false)
            uiState.activeFolder != null -> viewModel.closeFolder()
            uiState.activeModal != ModalAppType.NONE -> viewModel.closeModal()
            uiState.isJiggleMode -> viewModel.setJiggleMode(false)
        }
    }

    // Filtered search results for Spotlight
    val searchResults = remember(uiState.searchQuery, uiState.homeApps, uiState.dockApps, uiState.installedApps) {
        val q = uiState.searchQuery.trim().lowercase()
        val allApps = (uiState.homeApps + uiState.dockApps + uiState.installedApps).distinctBy { it.title }
        if (q.isEmpty()) {
            allApps.take(16)
        } else {
            allApps.filter { it.title.lowercase().contains(q) }
        }
    }

    var rootContainerSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .onSizeChanged { rootContainerSize = it }
    ) {
        // Main Home Screen
        HomeScreen(
            homeApps = uiState.homeApps,
            dockApps = uiState.dockApps,
            wallpaper = uiState.wallpaper,
            isJiggleMode = uiState.isJiggleMode,
            onAppClick = { app, bounds ->
                viewModel.launchApp(context, app, bounds)
            },
            onAppLongClick = { _ ->
                viewModel.setJiggleMode(true)
            },
            onRemoveApp = { appId ->
                viewModel.removeHomeApp(appId)
            },
            onExitJiggleMode = {
                viewModel.setJiggleMode(false)
            },
            onOpenSpotlight = {
                viewModel.setSpotlightActive(true)
            }
        )

        // iOS 4 Folder Overlay (e.g. Utilities)
        IOS4FolderOverlay(
            folderApp = uiState.activeFolder,
            onDismiss = { viewModel.closeFolder() },
            onAppClick = { app, bounds ->
                viewModel.launchApp(context, app, bounds)
            }
        )

        // Spotlight Search Overlay
        AnimatedVisibility(
            visible = uiState.isSpotlightActive,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it / 2 }),
            exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it / 2 })
        ) {
            SpotlightSearchView(
                searchQuery = uiState.searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                searchResults = searchResults,
                onAppClick = { app, bounds ->
                    viewModel.setSpotlightActive(false)
                    viewModel.launchApp(context, app, bounds)
                },
                onClose = { viewModel.setSpotlightActive(false) }
            )
        }

        // Modal Apps Overlays
        AnimatedVisibility(
            visible = uiState.activeModal != ModalAppType.NONE,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 3 })
        ) {
            when (uiState.activeModal) {
                ModalAppType.NOTES -> {
                    RetroNotesApp(onClose = { viewModel.closeModal() })
                }
                ModalAppType.WEATHER -> {
                    RetroWeatherApp(onClose = { viewModel.closeModal() })
                }
                ModalAppType.CALCULATOR -> {
                    RetroCalculatorApp(onClose = { viewModel.closeModal() })
                }
                ModalAppType.IPOD -> {
                    RetroIPodApp(onClose = { viewModel.closeModal() })
                }
                ModalAppType.SETTINGS -> {
                    RetroSettingsApp(
                        currentWallpaper = uiState.wallpaper,
                        onWallpaperSelected = { viewModel.setWallpaper(it) },
                        hapticsEnabled = uiState.hapticsEnabled,
                        onHapticsToggle = { viewModel.setHapticsEnabled(it) },
                        onOpenAllApps = { viewModel.openModal(ModalAppType.ALL_APPS) },
                        onResetIcons = { viewModel.resetLayout() },
                        onClose = { viewModel.closeModal() }
                    )
                }
                ModalAppType.ALL_APPS -> {
                    AllAppsDrawer(
                        installedApps = uiState.installedApps,
                        onAppClick = { app, bounds ->
                            viewModel.closeModal()
                            viewModel.launchApp(context, app, bounds)
                        },
                        onClose = { viewModel.openModal(ModalAppType.SETTINGS) }
                    )
                }
                ModalAppType.NONE -> {}
            }
        }

        // Classic iOS 4 App Launch Zooming Animation from icon bounds
        val launchOrigin = uiState.activeLaunchOrigin
        if (launchOrigin != null && rootContainerSize != IntSize.Zero) {
            IOS4AppLaunchTransition(
                origin = launchOrigin,
                containerSize = rootContainerSize,
                onAnimationEnd = {
                    viewModel.clearLaunchOrigin()
                }
            )
        }
    }
}
