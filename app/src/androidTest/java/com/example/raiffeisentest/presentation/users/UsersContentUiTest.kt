package com.example.raiffeisentest.presentation.users

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.presentation.theme.RaiffeisenTestTheme
import org.junit.Rule
import org.junit.Test

class UsersContentUiTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingState_isDisplayed() {
        composeRule.setContent {
            RaiffeisenTestTheme {
                UsersContent(UsersUiState.Loading, {}, {})
            }
        }

        composeRule.onNodeWithTag(USERS_LOADING_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun userDetails_areDisplayed() {
        val user =
            User(
                id = "1",
                avatarUrl = "",
                fullName = "Ada Lovelace",
                age = 36,
                nationality = "GB",
                registeredAt = "2020-01-02T00:00:00Z",
            )

        composeRule.setContent {
            RaiffeisenTestTheme {
                UsersContent(UsersUiState.Content(users = listOf(user)), {}, {})
            }
        }

        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()
        composeRule.onNodeWithText("36 years from GB").assertIsDisplayed()
        composeRule.onNodeWithText("Registered Jan 2, 2020").assertIsDisplayed()
    }

    @Test
    fun unavailableToolbarAction_showsSnackbar() {
        composeRule.setContent {
            RaiffeisenTestTheme {
                UsersContent(UsersUiState.Content(users = emptyList()), {}, {})
            }
        }

        composeRule.onNodeWithContentDescription("Search users").performClick()
        composeRule.onNodeWithText("Feature not yet implemented").assertIsDisplayed()
    }

    @Test
    fun errorState_offersRetry() {
        composeRule.setContent {
            RaiffeisenTestTheme {
                UsersContent(UsersUiState.Error, {}, {})
            }
        }

        composeRule.onNodeWithText("Unable to load users").assertIsDisplayed()
        composeRule.onNodeWithText("Try again").assertIsDisplayed()
    }
}
