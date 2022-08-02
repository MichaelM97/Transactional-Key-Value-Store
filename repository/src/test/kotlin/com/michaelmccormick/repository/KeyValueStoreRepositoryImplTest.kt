package com.michaelmccormick.repository

import com.michaelmccormick.data.MemoryDataSourceFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class KeyValueStoreRepositoryImplTest {
    private lateinit var keyValueStoreRepositoryImpl: KeyValueStoreRepositoryImpl

    @BeforeEach
    fun before() {
        keyValueStoreRepositoryImpl = KeyValueStoreRepositoryImpl(
            memoryDataSourceFactory = MemoryDataSourceFactory(),
        )
    }

    @Nested
    inner class Get {
        @Test
        fun `Should get value`() {
            // Given
            val key = "foo"
            val value = "bar"
            keyValueStoreRepositoryImpl.set(key, value)

            // Then
            assertEquals(Result.success(value), keyValueStoreRepositoryImpl.get(key))
        }

        @Test
        fun `Should return failure if value doesn't exist`() {
            assertTrue(keyValueStoreRepositoryImpl.get("foo").isFailure)
        }
    }

    @Nested
    inner class Set {
        @Test
        fun `Should set value`() {
            // Given
            val key = "foo"
            val value = "bar"

            // When
            keyValueStoreRepositoryImpl.set(key, value)

            // Then
            assertEquals(Result.success(value), keyValueStoreRepositoryImpl.get(key))
        }

        @Test
        fun `Should overwrite previously set value`() {
            // Given
            val key = "foo"
            val value1 = "bar"
            val value2 = "123"

            // When
            keyValueStoreRepositoryImpl.set(key, value1)
            keyValueStoreRepositoryImpl.set(key, value2)

            // Then
            assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `Should delete value`() {
            // Given
            val key = "foo"
            val value = "bar"
            keyValueStoreRepositoryImpl.set(key, value)
            assertEquals(Result.success(value), keyValueStoreRepositoryImpl.get(key))

            // When
            keyValueStoreRepositoryImpl.delete(key)

            // Then
            assertTrue(keyValueStoreRepositoryImpl.get(key).isFailure)
        }
    }

    @Nested
    inner class Count {
        @Test
        fun `Should count occurrences of value`() {
            // Given
            keyValueStoreRepositoryImpl.set("1", "foo")
            keyValueStoreRepositoryImpl.set("2", "bar")
            keyValueStoreRepositoryImpl.set("3", "foo")
            keyValueStoreRepositoryImpl.set("4", "foo")
            keyValueStoreRepositoryImpl.set("5", "bar")

            // Then
            assertEquals(3, keyValueStoreRepositoryImpl.count("foo"))
            assertEquals(2, keyValueStoreRepositoryImpl.count("bar"))
        }

        @Test
        fun `Should return 0 when value doesn't exist`() {
            assertEquals(0, keyValueStoreRepositoryImpl.count("foo"))
        }
    }

    @Nested
    inner class CommitTransaction {
        @Test
        fun `Should commit transaction`() {
            // Given
            val key = "foo"
            val value1 = "abc"
            val value2 = "def"
            keyValueStoreRepositoryImpl.set(key, value1)
            assertEquals(Result.success(value1), keyValueStoreRepositoryImpl.get(key))

            // When
            keyValueStoreRepositoryImpl.beginTransaction()
            keyValueStoreRepositoryImpl.set(key, value2)
            assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
            keyValueStoreRepositoryImpl.commitTransaction()

            // Then
            assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
        }

        @Test
        fun `Should return failure if no transaction exists`() {
            assertTrue(keyValueStoreRepositoryImpl.commitTransaction().isFailure)
        }

        @Test
        fun `Should return failure when all transactions completed`() {
            // Given
            keyValueStoreRepositoryImpl.beginTransaction()
            keyValueStoreRepositoryImpl.beginTransaction()
            keyValueStoreRepositoryImpl.beginTransaction()

            // When
            keyValueStoreRepositoryImpl.commitTransaction()
            keyValueStoreRepositoryImpl.rollbackTransaction()
            keyValueStoreRepositoryImpl.rollbackTransaction()

            // Then
            assertTrue(keyValueStoreRepositoryImpl.commitTransaction().isFailure)
        }
    }

    @Nested
    inner class RollbackTransaction {
        @Test
        fun `Should rollback transaction`() {
            // Given
            val key = "foo"
            val value1 = "abc"
            val value2 = "def"
            keyValueStoreRepositoryImpl.set(key, value1)
            assertEquals(Result.success(value1), keyValueStoreRepositoryImpl.get(key))

            // When
            keyValueStoreRepositoryImpl.beginTransaction()
            keyValueStoreRepositoryImpl.set(key, value2)
            assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
            keyValueStoreRepositoryImpl.rollbackTransaction()

            // Then
            assertEquals(Result.success(value1), keyValueStoreRepositoryImpl.get(key))
        }

        @Test
        fun `Should return failure if no transaction exists`() {
            assertTrue(keyValueStoreRepositoryImpl.rollbackTransaction().isFailure)
        }

        @Test
        fun `Should return failure when all transactions completed`() {
            // Given
            keyValueStoreRepositoryImpl.beginTransaction()
            keyValueStoreRepositoryImpl.beginTransaction()
            keyValueStoreRepositoryImpl.beginTransaction()

            // When
            keyValueStoreRepositoryImpl.rollbackTransaction()
            keyValueStoreRepositoryImpl.commitTransaction()
            keyValueStoreRepositoryImpl.commitTransaction()

            // Then
            assertTrue(keyValueStoreRepositoryImpl.rollbackTransaction().isFailure)
        }
    }

    @Test
    fun `Should handle committing and rolling back of nested transactions`() {
        // Given
        val key = "foo"
        val value1 = "abc"
        val value2 = "def"
        val value3 = "ghi"
        val value4 = "jkl"
        keyValueStoreRepositoryImpl.set(key, value1)
        assertEquals(Result.success(value1), keyValueStoreRepositoryImpl.get(key))

        // When
        keyValueStoreRepositoryImpl.beginTransaction()
        keyValueStoreRepositoryImpl.set(key, value2)
        assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
        keyValueStoreRepositoryImpl.beginTransaction()
        keyValueStoreRepositoryImpl.set(key, value3)
        assertEquals(Result.success(value3), keyValueStoreRepositoryImpl.get(key))
        keyValueStoreRepositoryImpl.beginTransaction()
        keyValueStoreRepositoryImpl.set(key, value4)
        assertEquals(Result.success(value4), keyValueStoreRepositoryImpl.get(key))
        keyValueStoreRepositoryImpl.commitTransaction()
        assertEquals(Result.success(value4), keyValueStoreRepositoryImpl.get(key))
        keyValueStoreRepositoryImpl.rollbackTransaction()
        assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
        keyValueStoreRepositoryImpl.commitTransaction()

        // Then
        assertEquals(Result.success(value2), keyValueStoreRepositoryImpl.get(key))
        assertTrue(keyValueStoreRepositoryImpl.commitTransaction().isFailure)
        assertTrue(keyValueStoreRepositoryImpl.rollbackTransaction().isFailure)
    }
}
