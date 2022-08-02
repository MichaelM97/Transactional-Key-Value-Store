package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.BeginTransactionUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class BeginTransactionUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<BeginTransactionUseCase> {
    override fun create(): BeginTransactionUseCase {
        return BeginTransactionUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
