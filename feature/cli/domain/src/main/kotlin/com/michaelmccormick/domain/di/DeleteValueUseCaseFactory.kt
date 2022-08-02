package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.DeleteValueUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class DeleteValueUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<DeleteValueUseCase> {
    override fun create(): DeleteValueUseCase {
        return DeleteValueUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
