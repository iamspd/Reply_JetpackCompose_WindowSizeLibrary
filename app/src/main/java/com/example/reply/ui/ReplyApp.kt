package com.example.reply.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reply.ui.screens.HomeScreen

@Composable
fun ReplyApp(
    modifier: Modifier = Modifier
) {
    val viewModel: ReplyViewModel = viewModel()
    val appUiState = viewModel.appUiState.collectAsState().value

    HomeScreen(
        appUiState = appUiState,
        onTabPressed = { viewModel.updateCurrentMailbox(mailboxType = it) },
        onEmailCardClick = { viewModel.updateDetailsScreenState(email = it) },
        modifier = modifier
    )
}