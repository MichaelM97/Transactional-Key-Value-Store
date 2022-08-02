package com.michaelmccormick.data

import com.michaelmccormick.core.di.Factory

class MemoryDataSourceFactory : Factory<MemoryDataSource> {
    override fun create(): MemoryDataSource {
        return MemoryDataSourceImpl()
    }

    fun create(dataSource: MemoryDataSource): MemoryDataSource {
        val dataSourceImpl = MemoryDataSourceImpl()
        dataSourceImpl.setAll(dataSource.getAll())
        return dataSourceImpl
    }
}
