package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class RollbackTransactionUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke(): Result<Unit> = keyValueStoreRepository.rollbackTransaction()
}
