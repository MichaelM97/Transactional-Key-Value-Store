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
internal class BeginTransactionUseCaseTest {
    private val mockKeyValueStoreRepository: KeyValueStoreRepository = mockk()
    private lateinit var beginTransactionUseCase: BeginTransactionUseCase

    @BeforeEach
    fun before() {
        beginTransactionUseCase = BeginTransactionUseCase(
            keyValueStoreRepository = mockKeyValueStoreRepository,
        )
    }

    @Test
    fun `Should call repository when invoked`() {
        // Given
        every { mockKeyValueStoreRepository.beginTransaction() } just Runs

        // When
        beginTransactionUseCase()

        // Then
        verify(exactly = 1) { mockKeyValueStoreRepository.beginTransaction() }
    }
}
