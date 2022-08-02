package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.CountValuesUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class CountValuesUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<CountValuesUseCase> {
    override fun create(): CountValuesUseCase {
        return CountValuesUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
