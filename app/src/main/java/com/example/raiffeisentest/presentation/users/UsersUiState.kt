package com.example.raiffeisentest.presentation.users

import androidx.compose.runtime.Immutable
import com.example.raiffeisentest.domain.model.User

/** Complete render state for the users screen. */
@Immutable
internal sealed interface UsersUiState {
    data object Loading : UsersUiState

    data object Error : UsersUiState

    data class Content(
        val users: List<User>,
        val isLoadingMore: Boolean = false,
        val hasLoadMoreError: Boolean = false,
    ) : UsersUiState
}
