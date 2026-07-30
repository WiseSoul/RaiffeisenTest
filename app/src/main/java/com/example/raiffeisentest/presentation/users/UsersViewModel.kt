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
        val users = currentUsers()
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
        if (!canLoadNextPage()) return

        val currentUsers = currentUsers()
        isRequestInProgress = true
        showLoading(currentUsers)

        viewModelScope.launch {
            try {
                val newUsers = getUsersPageUseCase(nextPage)
                nextPage++
                showUsers(currentUsers + newUsers)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (_: Throwable) {
                showError(currentUsers)
            } finally {
                isRequestInProgress = false
            }
        }
    }

    private fun canLoadNextPage(): Boolean =
        !isRequestInProgress && nextPage < GetUsersPageUseCase.MAX_PAGE_COUNT

    private fun currentUsers(): List<User> =
        (uiState.value as? UsersUiState.Content)?.users.orEmpty()

    private fun showLoading(users: List<User>) {
        mutableUiState.value =
            if (users.isEmpty()) {
                UsersUiState.Loading
            } else {
                UsersUiState.Content(
                    users = users,
                    isLoadingMore = true,
                )
            }
    }

    private fun showUsers(users: List<User>) {
        mutableUiState.value =
            UsersUiState.Content(
                users = users.distinctBy(User::id),
            )
    }

    private fun showError(users: List<User>) {
        mutableUiState.value =
            if (users.isEmpty()) {
                UsersUiState.Error
            } else {
                UsersUiState.Content(
                    users = users,
                    hasLoadMoreError = true,
                )
            }
    }
}
