package com.example.reply.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reply.ui.screens.HomeScreen
import com.example.reply.ui.utils.AppContentType
import com.example.reply.ui.utils.AppNavigationType

@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: ReplyViewModel = viewModel()
    val appUiState = viewModel.appUiState.collectAsState().value
    val navigationType: AppNavigationType
    val contentType: AppContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
            contentType = AppContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = AppNavigationType.NAVIGATION_RAIL
            contentType = AppContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = AppNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = AppContentType.LIST_AND_DETAIL
        }

        else -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
            contentType = AppContentType.LIST_ONLY
        }
    }

    HomeScreen(
        appUiState = appUiState,
        navigationType = navigationType,
        contentType = contentType,
        onTabPressed = {
            viewModel.updateCurrentMailbox(mailboxType = it)
            viewModel.resetHomeScreenStates()
        },
        onEmailCardClick = { viewModel.updateDetailsScreenState(email = it) },
        onDetailsScreenBackPressed = { viewModel.resetHomeScreenStates() },
        modifier = modifier
    )
}