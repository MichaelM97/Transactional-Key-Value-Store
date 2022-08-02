package com.michaelmccormick.repository

interface KeyValueStoreRepository {
    fun get(key: String): Result<String>
    fun set(key: String, value: String)
    fun delete(key: String)
    fun count(value: String): Int
    fun beginTransaction()
    fun commitTransaction(): Result<Unit>
    fun rollbackTransaction(): Result<Unit>
}
