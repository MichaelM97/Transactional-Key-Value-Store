package com.michaelmccormick.domain.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.CommitTransactionUseCase
import com.michaelmccormick.repository.KeyValueStoreRepository

class CommitTransactionUseCaseFactory(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) : Factory<CommitTransactionUseCase> {
    override fun create(): CommitTransactionUseCase {
        return CommitTransactionUseCase(
            keyValueStoreRepository = keyValueStoreRepository,
        )
    }
}
