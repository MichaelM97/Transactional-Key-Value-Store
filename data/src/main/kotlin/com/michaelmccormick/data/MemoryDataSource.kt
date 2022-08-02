package com.michaelmccormick.data

interface MemoryDataSource {
    fun get(key: String): String?
    fun set(key: String, value: String)
    fun delete(key: String)
    fun count(value: String): Int
    fun getAll(): Map<String, String>
    fun setAll(map: Map<String, String>)
}
