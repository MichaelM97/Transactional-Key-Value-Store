package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.GetValueUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class GetValueUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<GetValueUseCase> {
    override fun create(): GetValueUseCase {
        return GetValueUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
