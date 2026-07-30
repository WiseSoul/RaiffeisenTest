package com.example.raiffeisentest.data.remote.api

import com.example.raiffeisentest.data.remote.dto.RandomUserResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/** Defines the endpoint used to retrieve deterministic pages of users. */
internal interface RandomUserApi {
    @GET("api/")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") pageSize: Int,
        @Query("seed") seed: String,
    ): RandomUserResponseDto
}
