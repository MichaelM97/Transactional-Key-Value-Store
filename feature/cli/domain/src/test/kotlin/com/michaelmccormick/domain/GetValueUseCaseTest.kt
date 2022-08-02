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
internal class GetValueUseCaseTest {
    private val mockKeyValueStoreRepository: KeyValueStoreRepository = mockk()
    private lateinit var getValueUseCase: GetValueUseCase

    @BeforeEach
    fun before() {
        getValueUseCase = GetValueUseCase(
            keyValueStoreRepository = mockKeyValueStoreRepository,
        )
    }

    @Test
    fun `Should call repository and return result`() {
        // Given
        val key = "foo"
        val result = Result.success("bar")
        every { mockKeyValueStoreRepository.get(key) } returns result

        // Then
        assertEquals(result, getValueUseCase(key))
    }
}
