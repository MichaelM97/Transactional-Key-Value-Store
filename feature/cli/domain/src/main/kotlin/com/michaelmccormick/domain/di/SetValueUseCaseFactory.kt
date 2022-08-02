package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.SetValueUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class SetValueUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<SetValueUseCase> {
    override fun create(): SetValueUseCase {
        return SetValueUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
