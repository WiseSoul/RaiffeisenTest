package com.example.raiffeisentest.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Response returned by the Random User API. */
internal data class RandomUserResponseDto(
    @SerializedName("results") val users: List<UserDto>,
)

/** Network representation of a user. */
internal data class UserDto(
    val login: LoginDto,
    val name: NameDto,
    val dob: DateWithAgeDto,
    val nat: String,
    val registered: RegisteredDateDto,
    val picture: PictureDto,
)

internal data class LoginDto(val uuid: String)

internal data class NameDto(
    val first: String,
    val last: String,
)

internal data class DateWithAgeDto(val age: Int)

internal data class RegisteredDateDto(val date: String)

internal data class PictureDto(val large: String)
