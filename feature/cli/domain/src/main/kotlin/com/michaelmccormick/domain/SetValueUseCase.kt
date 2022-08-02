package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class SetValueUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke(key: String, value: String) {
        keyValueStoreRepository.set(key, value)
    }
}
