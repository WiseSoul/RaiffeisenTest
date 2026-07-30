package com.example.raiffeisentest.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.raiffeisentest.presentation.navigation.AppNavHost
import com.example.raiffeisentest.presentation.theme.RaiffeisenTestTheme

/** Provides application-level UI dependencies and hosts root navigation. */
@Composable
internal fun AppRouter() {
    RaiffeisenTestTheme {
        AppNavHost(navController = rememberNavController())
    }
}
