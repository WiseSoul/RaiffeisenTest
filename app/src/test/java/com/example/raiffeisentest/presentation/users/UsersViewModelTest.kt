package com.example.raiffeisentest.presentation.users

import com.example.raiffeisentest.MainDispatcherRule
import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.domain.usecase.GetUsersPageUseCase
import com.example.raiffeisentest.domain.repository.UserRepository
import com.example.raiffeisentest.presentation.users.UsersUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `Ensure first page is loaded when ViewModel is created`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Arrange & Act
            val repository = FakeUserRepository()
            val viewModel = UsersViewModel(GetUsersPageUseCase(repository))
            advanceUntilIdle()

            // Assert
            assertEquals(listOf(0), repository.requestedPages)
            assertEquals(20, (viewModel.uiState.value as UsersUiState.Content).users.size)
        }

    @Test
    fun `Ensure next page is prefetched when three users remain`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Arrange
            val repository = FakeUserRepository()
            val viewModel = UsersViewModel(GetUsersPageUseCase(repository))
            advanceUntilIdle()

            // Act
            viewModel.onLastVisibleItemChanged(index = 15)
            advanceUntilIdle()
            viewModel.onLastVisibleItemChanged(index = 16)
            advanceUntilIdle()

            // Assert
            assertEquals(listOf(0, 1), repository.requestedPages)
        }

    @Test
    fun `Ensure no more than three pages are loaded`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Arrange
            val repository = FakeUserRepository()
            val viewModel = UsersViewModel(GetUsersPageUseCase(repository))
            advanceUntilIdle()

            // Act
            listOf(16, 36, 56).forEach { lastVisibleIndex ->
                viewModel.onLastVisibleItemChanged(lastVisibleIndex)
                advanceUntilIdle()
            }

            // Assert
            assertEquals(listOf(0, 1, 2), repository.requestedPages)
            assertEquals(60, (viewModel.uiState.value as UsersUiState.Content).users.size)
        }

    @Test
    fun `Ensure initial error can be retried`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Arrange
            val repository = FakeUserRepository(failNextRequest = true)
            val viewModel = UsersViewModel(GetUsersPageUseCase(repository))
            advanceUntilIdle()
            assertEquals(UsersUiState.Error, viewModel.uiState.value)

            // Act
            viewModel.retry()
            advanceUntilIdle()

            // Assert
            assertEquals(listOf(0, 0), repository.requestedPages)
            assertTrue(viewModel.uiState.value is UsersUiState.Content)
        }
}

private class FakeUserRepository(
    private var failNextRequest: Boolean = false,
) : UserRepository {
    val requestedPages = mutableListOf<Int>()

    override suspend fun getUsers(
        page: Int,
        pageSize: Int,
    ): List<User> {
        requestedPages += page
        if (failNextRequest) {
            failNextRequest = false
            error("Network unavailable")
        }

        return List(pageSize) { index ->
            User(
                id = "$page-$index",
                avatarUrl = "https://example.com/avatar.png",
                fullName = "User $page-$index",
                age = 30,
                nationality = "RO",
                registeredAt = "2020-01-01T00:00:00Z",
            )
        }
    }
}
