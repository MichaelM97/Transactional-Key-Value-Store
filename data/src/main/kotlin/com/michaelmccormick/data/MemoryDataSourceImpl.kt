package com.michaelmccormick.data

internal class MemoryDataSourceImpl : MemoryDataSource {
    private val store: MutableMap<String, String> = mutableMapOf()

    override fun get(key: String): String? = store[key]

    override fun set(key: String, value: String) {
        store[key] = value
    }

    override fun delete(key: String) {
        store.remove(key)
    }

    override fun count(value: String): Int = store.values.count { it == value }

    override fun getAll(): Map<String, String> = store

    override fun setAll(map: Map<String, String>) {
        store.clear()
        store.putAll(map)
    }
}
