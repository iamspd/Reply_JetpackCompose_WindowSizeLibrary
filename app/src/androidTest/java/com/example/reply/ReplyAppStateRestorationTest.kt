package com.example.reply

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.reply.data.local.LocalEmailsDataProvider
import com.example.reply.ui.ReplyApp
import org.junit.Rule
import org.junit.Test

class ReplyAppStateRestorationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @TestCompactWidth
    fun compactDevice_selectedEmailRetained_afterConfigurationChange() {
        // Setup compact window
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Compact)
        }

        // Given the Fourth email is displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].body)
        ).assertIsDisplayed()

        // Open details screen
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].subject)
        ).performClick()

        // Verify correct details page is displayed for the given email
        composeTestRule.onNodeWithContentDescriptionForStringId(
            R.string.navigation_back
        ).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].body)
        ).assertExists()

        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Verify that it still shows the details page for the same email
        composeTestRule.onNodeWithContentDescriptionForStringId(
            R.string.navigation_back
        ).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].body)
        ).assertExists()
    }

    @Test
    @TestExpandedWidth
    fun expandedDevice_selectedEmailRetained_afterConfigurationChange() {
        // Setup expanded view
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(
                windowSize = WindowWidthSizeClass.Expanded
            )
        }

        // Given the fourth email is displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].body)
        ).assertExists()

        // Select fourth email
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].subject)
        ).performClick()

        // Verify the email details is displayed on details section
        composeTestRule.onNodeWithTagForStringId(R.string.details_screen).onChildren().assertAny(
            hasAnyDescendant(
                hasText(
                    composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].body)
                )
            )
        )

        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Verify that the same is still displayed on the details screen
        composeTestRule.onNodeWithTagForStringId(R.string.details_screen).onChildren().assertAny(
            hasAnyDescendant(
                hasText(
                    composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[3].body)
                )
            )
        )
    }
}