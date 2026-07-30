package com.example.raiffeisentest.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.domain.usecase.GetUsersPageUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Owns user-list state and coordinates threshold-based page loading. */
internal class UsersViewModel(
    private val getUsersPageUseCase: GetUsersPageUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val uiState: StateFlow<UsersUiState> = mutableUiState.asStateFlow()

    private var nextPage = 0
    private var isRequestInProgress = false

    init {
        loadNextPage()
    }

    /** Requests another page when the last visible row enters the prefetch window. */
    fun onLastVisibleItemChanged(index: Int) {
        val users = (uiState.value as? UsersUiState.Content)?.users.orEmpty()
        val prefetchIndex = users.lastIndex - GetUsersPageUseCase.PREFETCH_DISTANCE

        if (users.isNotEmpty() && index >= prefetchIndex) {
            loadNextPage()
        }
    }

    /** Retries the page that most recently failed. */
    fun retry() {
        loadNextPage()
    }

    private fun loadNextPage() {
        if (isRequestInProgress || nextPage >= GetUsersPageUseCase.MAX_PAGE_COUNT) {
            return
        }

        val currentUsers = (uiState.value as? UsersUiState.Content)?.users.orEmpty()
        isRequestInProgress = true
        mutableUiState.value =
            if (currentUsers.isEmpty()) {
                UsersUiState.Loading
            } else {
                UsersUiState.Content(
                    users = currentUsers,
                    isLoadingMore = true,
                )
            }

        viewModelScope.launch {
            try {
                val newUsers = getUsersPageUseCase(nextPage)
                nextPage++
                mutableUiState.value =
                    UsersUiState.Content(
                        users = (currentUsers + newUsers).distinctBy(User::id),
                    )
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (_: Throwable) {
                mutableUiState.value =
                    if (currentUsers.isEmpty()) {
                        UsersUiState.Error
                    } else {
                        UsersUiState.Content(
                            users = currentUsers,
                            hasLoadMoreError = true,
                        )
                    }
            } finally {
                isRequestInProgress = false
            }
        }
    }
}
