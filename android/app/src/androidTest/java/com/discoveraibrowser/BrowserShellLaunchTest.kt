package com.discoveraibrowser

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrowserShellLaunchTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesBrowserShell() {
        composeRule.onNodeWithText("Discover AI Browser").assertIsDisplayed()
        composeRule.onNodeWithText("Go").assertIsDisplayed()
    }
}
