package com.example.raiffeisentest.data.remote

import com.example.raiffeisentest.data.remote.api.RandomUserApi
import com.example.raiffeisentest.data.remote.dto.UserDto

/** Provides user pages from the remote API. */
internal class RandomUserRemoteDataSource(
    private val randomUserApi: RandomUserApi,
) {
    suspend fun getUsers(
        page: Int,
        pageSize: Int,
    ): List<UserDto> =
        randomUserApi
            .getUsers(
                page = page,
                pageSize = pageSize,
                seed = API_SEED,
            ).users

    private companion object {
        private const val API_SEED = "abc"
    }
}
