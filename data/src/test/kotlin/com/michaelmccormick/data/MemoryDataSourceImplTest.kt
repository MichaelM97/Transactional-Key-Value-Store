package com.michaelmccormick.data

import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MemoryDataSourceImplTest {
    private lateinit var memoryDataSourceImpl: MemoryDataSourceImpl

    @BeforeEach
    fun before() {
        memoryDataSourceImpl = MemoryDataSourceImpl()
    }

    @Nested
    inner class Get {
        @Test
        fun `Should get value`() {
            // Given
            val key = "foo"
            val value = "bar"
            memoryDataSourceImpl.set(key, value)

            // Then
            assertEquals(value, memoryDataSourceImpl.get(key))
        }

        @Test
        fun `Should return null when value not set`() {
            assertNull(memoryDataSourceImpl.get("foo"))
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
            memoryDataSourceImpl.set(key, value)

            // Then
            assertEquals(value, memoryDataSourceImpl.get(key))
        }

        @Test
        fun `Should overwrite previously set value`() {
            // Given
            val key = "foo"
            val value1 = "bar"
            val value2 = "123"

            // When
            memoryDataSourceImpl.set(key, value1)
            memoryDataSourceImpl.set(key, value2)

            // Then
            assertEquals(value2, memoryDataSourceImpl.get(key))
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `Should delete value`() {
            // Given
            val key = "foo"
            val value = "bar"
            memoryDataSourceImpl.set(key, value)
            assertEquals(value, memoryDataSourceImpl.get(key))

            // When
            memoryDataSourceImpl.delete(key)

            // Then
            assertNull(memoryDataSourceImpl.get(key))
        }
    }

    @Nested
    inner class Count {
        @Test
        fun `Should count occurrences of value`() {
            // Given
            memoryDataSourceImpl.set("1", "foo")
            memoryDataSourceImpl.set("2", "bar")
            memoryDataSourceImpl.set("3", "foo")
            memoryDataSourceImpl.set("4", "foo")
            memoryDataSourceImpl.set("5", "bar")

            // Then
            assertEquals(3, memoryDataSourceImpl.count("foo"))
            assertEquals(2, memoryDataSourceImpl.count("bar"))
        }

        @Test
        fun `Should return 0 when value doesn't exist`() {
            assertEquals(0, memoryDataSourceImpl.count("foo"))
        }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `Should get all values`() {
            // Given
            memoryDataSourceImpl.set("1", "foo")
            memoryDataSourceImpl.set("2", "bar")
            memoryDataSourceImpl.set("3", "foo")
            memoryDataSourceImpl.set("4", "foo")
            memoryDataSourceImpl.set("5", "bar")

            // Then
            assertEquals(
                mapOf(
                    "1" to "foo",
                    "2" to "bar",
                    "3" to "foo",
                    "4" to "foo",
                    "5" to "bar",
                ),
                memoryDataSourceImpl.getAll(),
            )
        }
    }

    @Nested
    inner class SetAll {
        @Test
        fun `Should set all values`() {
            // Given
            val map = mapOf(
                "1" to "foo",
                "2" to "bar",
                "3" to "foo",
                "4" to "foo",
                "5" to "bar",
            )

            // When
            memoryDataSourceImpl.setAll(map)

            // Then
            assertEquals(map, memoryDataSourceImpl.getAll())
        }

        @Test
        fun `Should set all values, replacing any existing ones`() {
            // Given
            val map = mapOf(
                "1" to "foo",
                "2" to "bar",
                "3" to "foo",
                "4" to "foo",
                "5" to "bar",
            )
            memoryDataSourceImpl.setAll(map)
            assertEquals(map, memoryDataSourceImpl.getAll())
            val newMap = mapOf(
                "1" to "123",
                "2" to "456",
                "7" to "789",
                "8" to "abc",
                "9" to "def",
            )

            // When
            memoryDataSourceImpl.setAll(newMap)

            // Then
            assertEquals(newMap, memoryDataSourceImpl.getAll())
        }
    }
}
