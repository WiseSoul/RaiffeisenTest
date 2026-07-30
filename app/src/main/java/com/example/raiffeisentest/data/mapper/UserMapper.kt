package com.example.raiffeisentest.data.mapper

import com.example.raiffeisentest.data.remote.dto.UserDto
import com.example.raiffeisentest.domain.model.User

/** Converts a network user representation into the app's domain model. */
internal fun UserDto.toDomain(): User =
    User(
        id = login.uuid,
        avatarUrl = picture.large,
        fullName = "${name.first} ${name.last}".trim(),
        age = dob.age,
        nationality = nat,
        registeredAt = registered.date,
    )
