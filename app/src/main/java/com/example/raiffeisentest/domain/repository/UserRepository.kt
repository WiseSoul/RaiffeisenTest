package com.example.raiffeisentest.domain.repository

import com.example.raiffeisentest.domain.model.User

/** Domain boundary through which user data is retrieved. */
internal interface UserRepository {
    suspend fun getUsers(
        page: Int,
        pageSize: Int,
    ): List<User>
}
