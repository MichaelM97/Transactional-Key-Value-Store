package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.RollbackTransactionUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class RollbackTransactionUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<RollbackTransactionUseCase> {
    override fun create(): RollbackTransactionUseCase {
        return RollbackTransactionUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
