package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository

class CountValuesUseCase internal constructor(
    private val keyValueStoreRepository: KeyValueStoreRepository,
) {
    operator fun invoke(value: String): Int = keyValueStoreRepository.count(value)
}
