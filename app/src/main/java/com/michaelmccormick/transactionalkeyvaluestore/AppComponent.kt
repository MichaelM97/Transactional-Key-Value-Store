package com.michaelmccormick.transactionalkeyvaluestore

import com.michaelmccormick.data.MemoryDataSourceFactory
import com.michaelmccormick.domain.di.CliDomainModule
import com.michaelmccormick.repository.KeyValueStoreRepository
import com.michaelmccormick.repository.KeyValueStoreRepositoryFactory

class AppComponent {
    private val memoryDataSourceFactory: MemoryDataSourceFactory = MemoryDataSourceFactory()
    private val keyValueStoreRepository: KeyValueStoreRepository = KeyValueStoreRepositoryFactory(
        memoryDataSourceFactory = memoryDataSourceFactory,
    ).create()
    val cliDomainModule: CliDomainModule = CliDomainModule(
        keyValueStoreRepository = keyValueStoreRepository,
    )
}
