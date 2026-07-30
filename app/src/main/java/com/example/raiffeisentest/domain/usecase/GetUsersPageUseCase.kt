package com.example.raiffeisentest.domain.usecase

import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.domain.repository.UserRepository

/**
 * Loads one page of users and enforces the paging limits required by the feature.
 *
 * Paging policy belongs here so neither the presentation layer nor data source owns it.
 */
internal class GetUsersPageUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(page: Int): List<User> {
        require(page in 0 until MAX_PAGE_COUNT) { "Page must be between 0 and ${MAX_PAGE_COUNT - 1}" }
        return userRepository.getUsers(
            page = page,
            pageSize = PAGE_SIZE,
        )
    }

    companion object {
        const val PAGE_SIZE = 20
        const val MAX_PAGE_COUNT = 3
        const val PREFETCH_DISTANCE = 3
    }
}
