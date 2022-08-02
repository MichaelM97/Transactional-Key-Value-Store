package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class CommitTransactionUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke(): Result<Unit> = keyValueStoreRepository.commitTransaction()
}
