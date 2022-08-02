package com.michaelmccormick.repository

import com.michaelmccormick.data.MemoryDataSource
import com.michaelmccormick.data.MemoryDataSourceFactory

internal class KeyValueStoreRepositoryImpl(
    private val memoryDataSourceFactory: MemoryDataSourceFactory,
) : KeyValueStoreRepository {
    private val transactionStack: MutableList<MemoryDataSource> = mutableListOf(memoryDataSourceFactory.create())
    private val currentDataSource: MemoryDataSource
        get() = transactionStack.last()

    override fun get(key: String): Result<String> {
        return currentDataSource.get(key)?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException())
    }

    override fun set(key: String, value: String) = currentDataSource.set(key, value)

    override fun delete(key: String) = currentDataSource.delete(key)

    override fun count(value: String): Int = currentDataSource.count(value)

    override fun beginTransaction() {
        transactionStack.add(memoryDataSourceFactory.create(currentDataSource))
    }

    override fun commitTransaction(): Result<Unit> {
        return if (transactionStack.size == 1) {
            Result.failure(IllegalStateException())
        } else {
            transactionStack.removeAt(transactionStack.size - 2)
            Result.success(Unit)
        }
    }

    override fun rollbackTransaction(): Result<Unit> {
        return if (transactionStack.size == 1) {
            Result.failure(IllegalStateException())
        } else {
            transactionStack.removeLast()
            Result.success(Unit)
        }
    }
}
