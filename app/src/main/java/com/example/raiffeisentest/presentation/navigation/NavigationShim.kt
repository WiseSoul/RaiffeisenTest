package com.example.raiffeisentest.presentation.navigation

import androidx.compose.runtime.Composable

/** Testable boundary between a navigation destination and its screen content. */
internal interface NavigationShim<T> {
    @Composable
    fun Content(arguments: T)
}
