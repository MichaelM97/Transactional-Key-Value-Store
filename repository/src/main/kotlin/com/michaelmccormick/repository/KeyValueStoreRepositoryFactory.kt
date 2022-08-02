package com.michaelmccormick.repository

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.data.MemoryDataSourceFactory

class KeyValueStoreRepositoryFactory(
    private val memoryDataSourceFactory: MemoryDataSourceFactory,
) : Factory<KeyValueStoreRepository> {
    override fun create(): KeyValueStoreRepository {
        return KeyValueStoreRepositoryImpl(
            memoryDataSourceFactory = memoryDataSourceFactory,
        )
    }
}
