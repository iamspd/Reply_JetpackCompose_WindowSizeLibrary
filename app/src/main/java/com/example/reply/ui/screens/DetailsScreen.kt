package com.example.reply.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.reply.R
import com.example.reply.data.Email
import com.example.reply.data.MailboxType
import com.example.reply.data.local.LocalEmailsDataProvider
import com.example.reply.ui.AppUiState
import com.example.reply.ui.theme.ReplyTheme

@Preview
@Composable
fun PreviewDetailsScreen() {
    ReplyTheme {
        DetailsScreen(
            modifier = Modifier.fillMaxSize(),
            appUiState = AppUiState(
                currentSelectedEmail = LocalEmailsDataProvider.getEmailById(3L)!!
            )
        ) { }
    }
}

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    appUiState: AppUiState,
    isFullScreen: Boolean = false,
    onBackPressed: () -> Unit,
) {

    BackHandler {
        onBackPressed()
    }

    Box(modifier = modifier) {
        LazyColumn(
            contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item {
                if (isFullScreen) {
                    DetailsScreenTopBar(
                        onBackButtonClicked = onBackPressed,
                        appUiState = appUiState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.topbar_padding_vertical))
                    )
                }

                DetailsScreenEmailDetailCard(
                    email = appUiState.currentSelectedEmail,
                    mailboxType = appUiState.currentMailbox,
                    isFullScreen = isFullScreen,
                    modifier = if (isFullScreen) {
                        Modifier
                            .navigationBarsPadding()
                            .padding(horizontal = dimensionResource(R.dimen.detail_card_outer_padding_horizontal))
                    } else {
                        Modifier.padding(horizontal = dimensionResource(R.dimen.detail_card_outer_padding_horizontal))
                    }
                )
            }
        }
    }
}

@Composable
fun DetailsScreenTopBar(
    onBackButtonClicked: () -> Unit,
    appUiState: AppUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.detail_topbar_back_button_padding_horizontal)
                )
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
            onClick = onBackButtonClicked
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.navigation_back)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = dimensionResource(R.dimen.detail_subject_padding_end)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(appUiState.currentSelectedEmail.subject),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DetailsScreenEmailDetailCard(
    modifier: Modifier = Modifier,
    email: Email,
    isFullScreen: Boolean,
    mailboxType: MailboxType
) {
    val context = LocalContext.current
    val displayToast = { text: String ->
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.detail_card_inner_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.detail_card_inner_padding)),
        ) {
            DetailsScreenEmailHeader(
                email = email,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isFullScreen) {
                Text(
                    text = stringResource(email.subject),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.detail_content_padding_top),
                        bottom = dimensionResource(R.dimen.detail_expanded_subject_body_spacing)
                    ),
                )
            }
            Text(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(email.body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DetailsScreenButtonBar(mailboxType, displayToast)
        }
    }
}

@Composable
fun DetailsScreenEmailHeader(
    email: Email,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        ProfileAvatar(
            modifier = Modifier.size(dimensionResource(R.dimen.email_header_profile_size)),
            avatarRes = email.sender.avatar,
            contentDescriptionRes = stringResource(email.sender.firstName) + " "
                    + stringResource(email.sender.lastName)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(R.dimen.email_header_content_padding_vertical),
                    horizontal = dimensionResource(R.dimen.email_header_content_padding_horizontal)
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(email.sender.firstName),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(email.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun DetailsScreenButtonBar(
    mailboxType: MailboxType,
    displayToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (mailboxType) {
            MailboxType.Drafts ->
                ActionButton(
                    onButtonClick = displayToast,
                    buttonText = stringResource(R.string.continue_composing)
                )

            MailboxType.Spam ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.detail_button_bar_item_spacing)
                    )
                ) {
                    ActionButton(
                        onButtonClick = displayToast,
                        buttonText = stringResource(R.string.move_to_inbox),
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        onButtonClick = displayToast,
                        buttonText = stringResource(R.string.delete),
                        modifier = Modifier.weight(1f),
                        containsIrreversibleAction = true
                    )
                }

            MailboxType.Inbox,
            MailboxType.Sent ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.detail_button_bar_item_spacing)
                    )
                ) {
                    ActionButton(
                        onButtonClick = displayToast,
                        buttonText = stringResource(R.string.reply),
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        onButtonClick = displayToast,
                        buttonText = stringResource(R.string.reply_all),
                        modifier = Modifier.weight(1f)
                    )
                }
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onButtonClick: (String) -> Unit,
    buttonText: String,
    containsIrreversibleAction: Boolean = false
) {
    Box(modifier = modifier) {
        Button(
            onClick = { onButtonClick(buttonText) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                if (containsIrreversibleAction) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            )
        ) {
            Text(
                text = buttonText,
                color =
                    if (containsIrreversibleAction) {
                        MaterialTheme.colorScheme.onError
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
            )
        }
    }
}