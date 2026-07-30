package com.example.raiffeisentest.presentation.navigation.users

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.raiffeisentest.presentation.navigation.NavigationShim
import com.example.raiffeisentest.presentation.navigation.route.UsersRoute
import com.example.raiffeisentest.presentation.users.UsersScreen
import com.example.raiffeisentest.presentation.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel

private object UsersDestination : NavigationShim<UsersRoute> {
    @Composable
    override fun Content(arguments: UsersRoute) {
        UsersScreen(usersViewModel = koinViewModel<UsersViewModel>())
    }
}

/** Adds the users feature destination to a parent navigation graph. */
internal fun NavGraphBuilder.usersNavigationGraph(
    screenMock: NavigationShim<UsersRoute>? = null,
) {
    composable<UsersRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<UsersRoute>()
        (screenMock ?: UsersDestination).Content(arguments = route)
    }
}
