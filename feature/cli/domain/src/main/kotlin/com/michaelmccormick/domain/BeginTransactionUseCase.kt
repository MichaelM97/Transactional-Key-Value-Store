package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class BeginTransactionUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke() = keyValueStoreRepository.beginTransaction()
}
