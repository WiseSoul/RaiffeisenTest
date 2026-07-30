package com.example.raiffeisentest.di

import com.example.raiffeisentest.data.remote.RandomUserRemoteDataSource
import com.example.raiffeisentest.data.remote.api.RandomUserApi
import com.example.raiffeisentest.data.repository.DefaultUserRepository
import com.example.raiffeisentest.domain.repository.UserRepository
import com.example.raiffeisentest.domain.usecase.GetUsersPageUseCase
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Declares application dependencies and their intended lifetimes. */
internal val appModule =
    module {
        single<RandomUserApi> {
            Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RandomUserApi::class.java)
        }

        factory { RandomUserRemoteDataSource(randomUserApi = get()) }
        factory<UserRepository> { DefaultUserRepository(remoteDataSource = get()) }
        factory { GetUsersPageUseCase(userRepository = get()) }
    }
