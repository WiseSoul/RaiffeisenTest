package com.example.raiffeisentest.data.repository

import com.example.raiffeisentest.data.mapper.toDomain
import com.example.raiffeisentest.data.remote.RandomUserRemoteDataSource
import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.domain.repository.UserRepository

/** Maps remote user data to domain models. */
internal class DefaultUserRepository(
    private val remoteDataSource: RandomUserRemoteDataSource,
) : UserRepository {
    override suspend fun getUsers(
        page: Int,
        pageSize: Int,
    ): List<User> = remoteDataSource.getUsers(page, pageSize).map { it.toDomain() }
}
