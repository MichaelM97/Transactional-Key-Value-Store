package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class GetValueUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke(key: String): Result<String> = keyValueStoreRepository.get(key)
}
