package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class DeleteValueUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke(key: String) {
        keyValueStoreRepository.delete(key)
    }
}
