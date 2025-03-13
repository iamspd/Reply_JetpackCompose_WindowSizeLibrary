package com.example.reply.ui

import androidx.lifecycle.ViewModel
import com.example.reply.data.Email
import com.example.reply.data.MailboxType
import com.example.reply.data.local.LocalEmailsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReplyViewModel: ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    init {
        initializeAppUiState()
    }

    fun updateCurrentMailbox(mailboxType: MailboxType) {
        _appUiState.update {
            it.copy(
                currentMailbox = mailboxType
            )
        }
    }

    fun updateDetailsScreenState(email: Email) {
        _appUiState.update {
            it.copy(
                currentSelectedEmail = email
            )
        }
    }

    private fun initializeAppUiState() {
        val mailboxes: Map<MailboxType, List<Email>> =
            LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }

        _appUiState.value = AppUiState(
            mailboxes = mailboxes
        )
    }
}