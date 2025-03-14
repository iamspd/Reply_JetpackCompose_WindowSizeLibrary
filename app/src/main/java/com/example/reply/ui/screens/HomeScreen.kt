package com.example.reply.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.reply.R
import com.example.reply.data.Email
import com.example.reply.data.MailboxType
import com.example.reply.data.local.LocalAccountsDataProvider
import com.example.reply.data.local.LocalEmailsDataProvider
import com.example.reply.ui.AppUiState
import com.example.reply.ui.theme.ReplyTheme
import com.example.reply.ui.utils.AppContentType
import com.example.reply.ui.utils.AppNavigationType

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    ReplyTheme {
        HomeScreen(
            appUiState = AppUiState(
                mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
            ),
            navigationType = AppNavigationType.BOTTOM_NAVIGATION,
            contentType = AppContentType.LIST_ONLY,
            onTabPressed = {},
            onEmailCardClick = {},
            onDetailsScreenBackPressed = {}
        )
    }
}

@Composable
fun HomeScreen(
    appUiState: AppUiState,
    navigationType: AppNavigationType,
    contentType: AppContentType,
    onTabPressed: (MailboxType) -> Unit,
    onEmailCardClick: (Email) -> Unit,
    onDetailsScreenBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            mailboxType = MailboxType.Inbox,
            icon = Icons.Default.AccountBox,
            text = stringResource(R.string.tab_inbox)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Sent,
            icon = Icons.AutoMirrored.Filled.Send,
            text = stringResource(id = R.string.tab_sent)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Drafts,
            icon = Icons.Default.MailOutline,
            text = stringResource(id = R.string.tab_drafts)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Spam,
            icon = Icons.Default.Warning,
            text = stringResource(id = R.string.tab_spam)
        )
    )

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(Modifier.width(dimensionResource(R.dimen.drawer_width))) {
                    PermanentNavDrawerContent(
                        currentTab = appUiState.currentMailbox,
                        onTabPressed = onTabPressed,
                        navigationItemContentList = navigationItemContentList,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                            .padding(dimensionResource(R.dimen.drawer_padding_content))
                            .testTag(stringResource(R.string.navigation_drawer))
                    )
                }
            }
        ) {
            HomeContent(
                appUiState = appUiState,
                navigationType = navigationType,
                contentType = contentType,
                onEmailCardClick = onEmailCardClick,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        }
    } else {
        if (appUiState.isShowingHomePage) {
            HomeContent(
                appUiState = appUiState,
                navigationType = navigationType,
                contentType = contentType,
                onEmailCardClick = onEmailCardClick,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        } else {
            DetailsScreen(
                appUiState = appUiState,
                isFullScreen = true,
                onBackPressed = onDetailsScreenBackPressed,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeContent(
    appUiState: AppUiState,
    navigationType: AppNavigationType,
    contentType: AppContentType,
    onEmailCardClick: (Email) -> Unit,
    onTabPressed: (MailboxType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier) {

        AnimatedVisibility(visible = navigationType == AppNavigationType.NAVIGATION_RAIL) {
            AppNavigationRail(
                currentTab = appUiState.currentMailbox,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = Modifier.testTag(stringResource(R.string.navigation_rail))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (contentType == AppContentType.LIST_AND_DETAIL) {
                EmailListAndDetailContent(
                    appUiState = appUiState,
                    onEmailCardClick = onEmailCardClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                EmailListContent(
                    appUiState = appUiState,
                    onEmailCardClick = onEmailCardClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = dimensionResource(R.dimen.email_list_only_horizontal_padding)
                        )
                )
            }

            AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION) {
                AppBottomNavigationBar(
                    currentTab = appUiState.currentMailbox,
                    onTabPressed = onTabPressed,
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(R.string.navigation_bottom))
                )
            }
        }
    }
}

@Composable
private fun PermanentNavDrawerContent(
    modifier: Modifier = Modifier,
    currentTab: MailboxType,
    onTabPressed: (MailboxType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>
) {
    Column(modifier = modifier) {
        PermanentNavDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.profile_image_padding))
        )
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                selected = currentTab == navItem.mailboxType,
                label = {
                    Text(
                        text = navItem.text,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header))
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { onTabPressed(navItem.mailboxType) }
            )
        }
    }
}

@Composable
private fun PermanentNavDrawerHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppLogo(modifier = Modifier.size(dimensionResource(R.dimen.reply_logo_size)))
        ProfileAvatar(
            avatarRes = LocalAccountsDataProvider.getContactAccountById(3L).avatar,
            contentDescriptionRes = stringResource(R.string.profile),
            modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
        )
    }
}

@Composable
private fun AppNavigationRail(
    currentTab: MailboxType,
    onTabPressed: ((MailboxType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentTab == navItem.mailboxType,
                onClick = { onTabPressed(navItem.mailboxType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Composable
private fun AppBottomNavigationBar(
    currentTab: MailboxType,
    onTabPressed: ((MailboxType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.mailboxType,
                onClick = { onTabPressed(navItem.mailboxType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

private data class NavigationItemContent(
    val mailboxType: MailboxType,
    val icon: ImageVector,
    val text: String
)