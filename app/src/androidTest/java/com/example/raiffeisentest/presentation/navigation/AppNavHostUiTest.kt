package com.example.raiffeisentest.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.raiffeisentest.presentation.navigation.route.UsersRoute
import com.example.raiffeisentest.presentation.theme.RaiffeisenTestTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AppNavHostUiTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun usersRoute_isStartDestination() {
        lateinit var navController: TestNavHostController

        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            RaiffeisenTestTheme {
                AppNavHost(
                    navController = navController,
                    usersScreenMock = UsersScreenMock,
                )
            }
        }

        composeRule.onNodeWithText("Users screen mock").assertIsDisplayed()
        composeRule.runOnIdle {
            assertEquals(UsersRoute::class.qualifiedName, navController.currentDestination?.route)
        }
    }
}

private object UsersScreenMock : NavigationShim<UsersRoute> {
    @Composable
    override fun Content(arguments: UsersRoute) {
        Text("Users screen mock")
    }
}
