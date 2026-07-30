package com.example.raiffeisentest.domain.usecase

import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.domain.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class GetUsersPageUseCaseTest {
    @Test
    fun `Ensure valid page is delegated with required page size`() =
        runTest {
            // Arrange
            val repository = RecordingUserRepository()

            // Act
            GetUsersPageUseCase(repository)(page = 2)

            // Assert
            assertEquals(2 to 20, repository.request)
        }

    @Test
    fun `Ensure fourth page is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            kotlinx.coroutines.runBlocking {
                GetUsersPageUseCase(RecordingUserRepository())(page = 3)
            }
        }
    }
}

private class RecordingUserRepository : UserRepository {
    var request: Pair<Int, Int>? = null

    override suspend fun getUsers(
        page: Int,
        pageSize: Int,
    ): List<User> {
        request = page to pageSize
        return emptyList()
    }
}
