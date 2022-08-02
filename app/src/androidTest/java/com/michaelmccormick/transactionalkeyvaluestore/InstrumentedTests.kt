package com.michaelmccormick.transactionalkeyvaluestore

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import com.michaelmccormick.feature.cli.ui.CLI_SCREEN_CLI_LINES_LIST_TAG
import com.michaelmccormick.transactionalkeyvaluestore.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class InstrumentedTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun shouldShowCliInputTextFieldWithSubmitButton() {
        composeTestRule.onInputField()
            .assertIsDisplayed()
            .assertIsEnabled()
        composeTestRule.onSubmitButton()
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun shouldShowShowSubmittedCommand() {
        // When
        inputAndSubmit("SET foo bar")

        // Then
        with(composeTestRule.onCliLinesList()) {
            childAtHasText(0, "> SET foo bar")
            onChildren().assertCountEquals(1)
        }
    }

    @Test
    fun shouldShowShowSubmittedCommandWithOutput() {
        // Given
        inputAndSubmit("SET foo bar")

        // When
        inputAndSubmit("GET foo")

        // Then
        with(composeTestRule.onCliLinesList()) {
            childAtHasText(0, "> SET foo bar")
            childAtHasText(1, "> GET foo")
            childAtHasText(2, "bar")
            onChildren().assertCountEquals(3)
        }
    }

    @Test
    fun transactionWithRollbackEndToEndTest() {
        inputAndSubmit("SET foo 123")
        inputAndSubmit("SET bar abc")
        inputAndSubmit("BEGIN")
        inputAndSubmit("SET foo 456")
        inputAndSubmit("GET foo")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("456").assertIsDisplayed()
        inputAndSubmit("SET bar def")
        inputAndSubmit("GET bar")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("def").assertIsDisplayed()
        inputAndSubmit("ROLLBACK")
        inputAndSubmit("GET foo")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("123").assertIsDisplayed()
        inputAndSubmit("GET bar")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("abc").assertIsDisplayed()
        inputAndSubmit("COMMIT")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("There is no active transaction").assertIsDisplayed()
    }

    @Test
    fun nestedTransactionsEndToEndTest() {
        inputAndSubmit("SET foo 123")
        inputAndSubmit("BEGIN")
        inputAndSubmit("SET bar 456")
        inputAndSubmit("SET foo 456")
        inputAndSubmit("BEGIN")
        inputAndSubmit("COUNT 456")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        inputAndSubmit("SET foo 789")
        inputAndSubmit("GET foo")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("789").assertIsDisplayed()
        inputAndSubmit("ROLLBACK")
        inputAndSubmit("GET foo")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("456").assertIsDisplayed()
        inputAndSubmit("ROLLBACK")
        inputAndSubmit("GET foo")
        composeTestRule.onCliLinesList().scrollToBottom()
        composeTestRule.onNodeWithText("123").assertIsDisplayed()
    }

    private fun SemanticsNodeInteractionsProvider.onInputField() = onNodeWithText("Enter a command")
    private fun SemanticsNodeInteractionsProvider.onSubmitButton() = onNodeWithContentDescription("Submit command")
    private fun SemanticsNodeInteractionsProvider.onCliLinesList() = onNodeWithTag(CLI_SCREEN_CLI_LINES_LIST_TAG)
    private fun SemanticsNodeInteraction.childAtHasText(index: Int, text: String) = onChildAt(index).assert(hasText(text, substring = true))
    private fun SemanticsNodeInteraction.scrollToBottom() = performScrollToIndex(onChildren().fetchSemanticsNodes().size - 1)

    private fun inputAndSubmit(command: String) {
        composeTestRule.onInputField().performTextInput(command)
        composeTestRule.onSubmitButton().performClick()
        composeTestRule.waitForIdle()
    }
}
