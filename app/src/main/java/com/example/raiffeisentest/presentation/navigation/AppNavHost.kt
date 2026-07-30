package com.example.raiffeisentest.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.raiffeisentest.presentation.navigation.route.UsersRoute
import com.example.raiffeisentest.presentation.navigation.users.usersNavigationGraph

private const val FADE_DURATION_MS = 150

/** Owns the application's root navigation graph and delegates destinations to features. */
@Composable
internal fun AppNavHost(
    navController: NavHostController,
    usersScreenMock: NavigationShim<UsersRoute>? = null,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = UsersRoute,
        modifier = modifier.fillMaxSize(),
        enterTransition = { fadeIn(tween(FADE_DURATION_MS)) },
        exitTransition = { fadeOut(tween(FADE_DURATION_MS)) },
        popEnterTransition = { fadeIn(tween(FADE_DURATION_MS)) },
        popExitTransition = { fadeOut(tween(FADE_DURATION_MS)) },
    ) {
        usersNavigationGraph(screenMock = usersScreenMock)
    }
}
