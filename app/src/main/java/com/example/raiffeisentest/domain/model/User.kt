package com.example.raiffeisentest.domain.model

/** User information consumed by the presentation layer. */
internal data class User(
    val id: String,
    val avatarUrl: String,
    val fullName: String,
    val age: Int,
    val nationality: String,
    val registeredAt: String,
)
