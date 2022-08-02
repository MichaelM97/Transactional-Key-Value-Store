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
internal class CommitTransactionUseCaseTest {
    private val mockKeyValueStoreRepository: KeyValueStoreRepository = mockk()
    private lateinit var commitTransactionUseCase: CommitTransactionUseCase

    @BeforeEach
    fun before() {
        commitTransactionUseCase = CommitTransactionUseCase(
            keyValueStoreRepository = mockKeyValueStoreRepository,
        )
    }

    @Test
    fun `Should call repository and return result`() {
        // Given
        val result = Result.success(Unit)
        every { mockKeyValueStoreRepository.commitTransaction() } returns result

        // Then
        assertEquals(result, commitTransactionUseCase())
    }
}
