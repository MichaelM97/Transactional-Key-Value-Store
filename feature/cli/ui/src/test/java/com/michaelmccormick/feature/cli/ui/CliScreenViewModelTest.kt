package com.michaelmccormick.feature.cli.ui

import com.michaelmccormick.core.test.DispatchersExtension
import com.michaelmccormick.domain.BeginTransactionUseCase
import com.michaelmccormick.domain.CommitTransactionUseCase
import com.michaelmccormick.domain.CountValuesUseCase
import com.michaelmccormick.domain.DeleteValueUseCase
import com.michaelmccormick.domain.GetValueUseCase
import com.michaelmccormick.domain.RollbackTransactionUseCase
import com.michaelmccormick.domain.SetValueUseCase
import com.michaelmccormick.feature.cli.ui.model.CliLine
import com.michaelmccormick.feature.cli.ui.model.CliLineErrorType
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, DispatchersExtension::class)
internal class CliScreenViewModelTest {
    private val mockGetValueUseCase: GetValueUseCase = mockk()
    private val mockSetValueUseCase: SetValueUseCase = mockk()
    private val mockDeleteValueUseCase: DeleteValueUseCase = mockk()
    private val mockCountValuesUseCase: CountValuesUseCase = mockk()
    private val mockBeginTransactionUseCase: BeginTransactionUseCase = mockk()
    private val mockCommitTransactionUseCase: CommitTransactionUseCase = mockk()
    private val mockRollbackTransactionUseCase: RollbackTransactionUseCase = mockk()
    private lateinit var viewModel: CliScreenViewModel

    @BeforeEach
    fun before() {
        viewModel = CliScreenViewModel(
            mockGetValueUseCase,
            mockSetValueUseCase,
            mockDeleteValueUseCase,
            mockCountValuesUseCase,
            mockBeginTransactionUseCase,
            mockCommitTransactionUseCase,
            mockRollbackTransactionUseCase,
        )
    }

    @Nested
    inner class OnCommandUpdated {
        @Test
        fun `Should update command input in state`() {
            // Given
            val command = "SET foo bar"

            // When
            viewModel.onCommandUpdated(command)

            // Then
            assertEquals(command, viewModel.state.value.commandInput)
        }
    }

    @Nested
    inner class OnCommandSubmitted {
        @Test
        fun `Should set command input to empty string in state`() {
            // Given
            val command = "SET foo bar"
            viewModel.onCommandUpdated(command)
            assertEquals(command, viewModel.state.value.commandInput)
            every { mockSetValueUseCase("foo", "bar") } just Runs

            // When
            viewModel.onCommandSubmitted()

            // Then
            assertEquals("", viewModel.state.value.commandInput)
        }

        @Test
        fun `Should add error to cli lines in state if command unknown`() {
            // Given
            val command = "ABC"
            viewModel.onCommandUpdated(command)

            // When
            viewModel.onCommandSubmitted()

            // Then
            assertEquals(
                CliLine.Error(CliLineErrorType.UNKNOWN_COMMAND),
                viewModel.state.value.cliLines[1],
            )
        }

        @Nested
        inner class GetCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("GET foo")
                every { mockGetValueUseCase("foo") } returns Result.success("bar")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "GET",
                        key = "foo",
                        value = null,
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should add error to cli lines in state if no key passed`() {
                // Given
                viewModel.onCommandUpdated("GET")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 0) { mockGetValueUseCase(any()) }
            }

            @Test
            fun `Should add success to cli lines in state if use case successful`() {
                // Given
                viewModel.onCommandUpdated("GET foo")
                every { mockGetValueUseCase("foo") } returns Result.success("bar")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Success("bar"),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 1) { mockGetValueUseCase("foo") }
            }

            @Test
            fun `Should add error to cli lines in state if use case fails`() {
                // Given
                viewModel.onCommandUpdated("GET foo")
                every { mockGetValueUseCase("foo") } returns Result.failure(Exception())

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.ENTRY_DOES_NOT_EXIST),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 1) { mockGetValueUseCase("foo") }
            }
        }

        @Nested
        inner class SetCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("SET foo bar")
                every { mockSetValueUseCase("foo", "bar") } just Runs

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "SET",
                        key = "foo",
                        value = "bar",
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should add error to cli lines in state if no key passed`() {
                // Given
                viewModel.onCommandUpdated("SET")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 0) { mockSetValueUseCase(any(), any()) }
            }

            @Test
            fun `Should add error to cli lines in state if no value passed`() {
                // Given
                viewModel.onCommandUpdated("SET foo")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.VALUE_PARAM_REQUIRED),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 0) { mockSetValueUseCase(any(), any()) }
            }

            @Test
            fun `Should call use case and not add to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("SET foo bar")
                every { mockSetValueUseCase("foo", "bar") } just Runs

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(1, viewModel.state.value.cliLines.size)
                verify(exactly = 1) { mockSetValueUseCase("foo", "bar") }
            }
        }

        @Nested
        inner class DeleteCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("DELETE foo")
                every { mockDeleteValueUseCase("foo") } just Runs

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "DELETE",
                        key = "foo",
                        value = null,
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should add error to cli lines in state if no key passed`() {
                // Given
                viewModel.onCommandUpdated("DELETE")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 0) { mockDeleteValueUseCase(any()) }
            }

            @Test
            fun `Should call use case and not add to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("DELETE foo")
                every { mockDeleteValueUseCase("foo") } just Runs

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(1, viewModel.state.value.cliLines.size)
                verify(exactly = 1) { mockDeleteValueUseCase("foo") }
            }
        }

        @Nested
        inner class CountCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("COUNT bar")
                every { mockCountValuesUseCase("bar") } returns 1

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "COUNT",
                        key = "bar",
                        value = null,
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should add error to cli lines in state if no key passed`() {
                // Given
                viewModel.onCommandUpdated("COUNT")

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 0) { mockCountValuesUseCase(any()) }
            }

            @Test
            fun `Should call use case and add success to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("COUNT bar")
                every { mockCountValuesUseCase("bar") } returns 34

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Success("34"),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 1) { mockCountValuesUseCase("bar") }
            }
        }

        @Nested
        inner class BeginCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("BEGIN")
                every { mockBeginTransactionUseCase() } just Runs

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "BEGIN",
                        key = null,
                        value = null,
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should call use case and not add to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("BEGIN")
                every { mockBeginTransactionUseCase() } just Runs

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(1, viewModel.state.value.cliLines.size)
                verify(exactly = 1) { mockBeginTransactionUseCase() }
            }
        }

        @Nested
        inner class CommitCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("COMMIT")
                every { mockCommitTransactionUseCase() } returns Result.success(Unit)

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "COMMIT",
                        key = null,
                        value = null,
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should call use case and not add to cli lines in state when use case successful`() {
                // Given
                viewModel.onCommandUpdated("COMMIT")
                every { mockCommitTransactionUseCase() } returns Result.success(Unit)

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(1, viewModel.state.value.cliLines.size)
                verify(exactly = 1) { mockCommitTransactionUseCase() }
            }

            @Test
            fun `Should call use case and add error to cli lines in state when use case fails`() {
                // Given
                viewModel.onCommandUpdated("COMMIT")
                every { mockCommitTransactionUseCase() } returns Result.failure(Exception())

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.NO_TRANSACTION),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 1) { mockCommitTransactionUseCase() }
            }
        }

        @Nested
        inner class RollbackCommand {
            @Test
            fun `Should add command input to cli lines in state`() {
                // Given
                viewModel.onCommandUpdated("ROLLBACK")
                every { mockRollbackTransactionUseCase() } returns Result.success(Unit)

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Command(
                        command = "ROLLBACK",
                        key = null,
                        value = null,
                    ),
                    viewModel.state.value.cliLines.first(),
                )
            }

            @Test
            fun `Should call use case and not add to cli lines in state when use case successful`() {
                // Given
                viewModel.onCommandUpdated("ROLLBACK")
                every { mockRollbackTransactionUseCase() } returns Result.success(Unit)

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(1, viewModel.state.value.cliLines.size)
                verify(exactly = 1) { mockRollbackTransactionUseCase() }
            }

            @Test
            fun `Should call use case and add error to cli lines in state when use case fails`() {
                // Given
                viewModel.onCommandUpdated("ROLLBACK")
                every { mockRollbackTransactionUseCase() } returns Result.failure(Exception())

                // When
                viewModel.onCommandSubmitted()

                // Then
                assertEquals(
                    CliLine.Error(CliLineErrorType.NO_TRANSACTION),
                    viewModel.state.value.cliLines[1],
                )
                verify(exactly = 1) { mockRollbackTransactionUseCase() }
            }
        }
    }
}
