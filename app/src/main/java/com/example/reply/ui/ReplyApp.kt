package com.example.reply.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reply.ui.screens.HomeScreen
import com.example.reply.ui.utils.AppNavigationType

@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: ReplyViewModel = viewModel()
    val appUiState = viewModel.appUiState.collectAsState().value
    val navigationType: AppNavigationType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = AppNavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = AppNavigationType.PERMANENT_NAVIGATION_DRAWER
        }

        else -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
        }
    }

    HomeScreen(
        appUiState = appUiState,
        navigationType = navigationType,
        onTabPressed = { viewModel.updateCurrentMailbox(mailboxType = it) },
        onEmailCardClick = { viewModel.updateDetailsScreenState(email = it) },
        onDetailsScreenBackPressed = { viewModel.resetHomeScreenStates() },
        modifier = modifier
    )
}