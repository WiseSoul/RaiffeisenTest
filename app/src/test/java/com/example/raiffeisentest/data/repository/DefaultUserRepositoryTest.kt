package com.example.raiffeisentest.data.repository

import com.example.raiffeisentest.data.remote.RandomUserRemoteDataSource
import com.example.raiffeisentest.data.remote.api.RandomUserApi
import com.example.raiffeisentest.data.remote.dto.DateWithAgeDto
import com.example.raiffeisentest.data.remote.dto.LoginDto
import com.example.raiffeisentest.data.remote.dto.NameDto
import com.example.raiffeisentest.data.remote.dto.PictureDto
import com.example.raiffeisentest.data.remote.dto.RandomUserResponseDto
import com.example.raiffeisentest.data.remote.dto.RegisteredDateDto
import com.example.raiffeisentest.data.remote.dto.UserDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultUserRepositoryTest {
    @Test
    fun `Ensure DTO is mapped to domain model`() =
        runTest {
            // Arrange
            val api = FakeRandomUserApi()
            val repository = DefaultUserRepository(RandomUserRemoteDataSource(api))

            // Act
            val users = repository.getUsers(page = 1, pageSize = 20)

            // Assert
            assertEquals(2, api.requestedPage)
            assertEquals(20, api.requestedPageSize)
            assertEquals("abc", api.requestedSeed)
            assertEquals("Ada Lovelace", users.single().fullName)
            assertEquals("avatar-url", users.single().avatarUrl)
        }
}

private class FakeRandomUserApi : RandomUserApi {
    var requestedPage: Int? = null
    var requestedPageSize: Int? = null
    var requestedSeed: String? = null

    override suspend fun getUsers(
        page: Int,
        pageSize: Int,
        seed: String,
    ): RandomUserResponseDto {
        requestedPage = page
        requestedPageSize = pageSize
        requestedSeed = seed

        return RandomUserResponseDto(
            users =
                listOf(
                    UserDto(
                        login = LoginDto(uuid = "user-id"),
                        name = NameDto(first = "Ada", last = "Lovelace"),
                        dob = DateWithAgeDto(age = 36),
                        nat = "GB",
                        registered = RegisteredDateDto(date = "2020-01-02T00:00:00Z"),
                        picture = PictureDto(large = "avatar-url"),
                    ),
                ),
        )
    }
}
