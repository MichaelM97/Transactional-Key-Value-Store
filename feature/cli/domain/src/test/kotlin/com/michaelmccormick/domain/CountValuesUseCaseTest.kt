package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CountValuesUseCaseTest {
    private val mockKeyValueStoreRepository: KeyValueStoreRepository = mockk()
    private lateinit var countValuesUseCase: CountValuesUseCase

    @BeforeEach
    fun before() {
        countValuesUseCase = CountValuesUseCase(
            keyValueStoreRepository = mockKeyValueStoreRepository,
        )
    }

    @Test
    fun `Should call repository and return count`() {
        // Given
        val value = "bar"
        val count = 15
        every { mockKeyValueStoreRepository.count(value) } returns count

        // Then
        assertEquals(count, countValuesUseCase(value))
    }
}
