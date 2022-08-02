package com.michaelmccormick.domain

import com.michaelmccormick.repository.KeyValueStoreRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class SetValueUseCaseTest {
    private val mockKeyValueStoreRepository: KeyValueStoreRepository = mockk()
    private lateinit var setValueUseCase: SetValueUseCase

    @BeforeEach
    fun before() {
        setValueUseCase = SetValueUseCase(
            keyValueStoreRepository = mockKeyValueStoreRepository,
        )
    }

    @Test
    fun `Should call repository when invoked`() {
        // Given
        val key = "foo"
        val value = "bar"
        every { mockKeyValueStoreRepository.set(key, value) } just Runs

        // When
        setValueUseCase(key, value)

        // Then
        verify(exactly = 1) { mockKeyValueStoreRepository.set(key, value) }
    }
}
