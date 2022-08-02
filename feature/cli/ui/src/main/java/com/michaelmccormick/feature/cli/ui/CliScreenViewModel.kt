package com.michaelmccormick.feature.cli.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelmccormick.domain.BeginTransactionUseCase
import com.michaelmccormick.domain.CommitTransactionUseCase
import com.michaelmccormick.domain.CountValuesUseCase
import com.michaelmccormick.domain.DeleteValueUseCase
import com.michaelmccormick.domain.GetValueUseCase
import com.michaelmccormick.domain.RollbackTransactionUseCase
import com.michaelmccormick.domain.SetValueUseCase
import com.michaelmccormick.feature.cli.ui.model.CliLine
import com.michaelmccormick.feature.cli.ui.model.CliLineErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CliScreenViewModel internal constructor(
    private val getValueUseCase: GetValueUseCase,
    private val setValueUseCase: SetValueUseCase,
    private val deleteValueUseCase: DeleteValueUseCase,
    private val countValuesUseCase: CountValuesUseCase,
    private val beginTransactionUseCase: BeginTransactionUseCase,
    private val commitTransactionUseCase: CommitTransactionUseCase,
    private val rollbackTransactionUseCase: RollbackTransactionUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state

    fun onCommandUpdated(newCommand: String) {
        updateState(state.value.copy(commandInput = newCommand))
    }

    fun onCommandSubmitted() {
        val inputs = state.value.commandInput.split(COMMAND_DELIMITER)
        inputs.firstOrNull()?.let { command ->
            reduce(Event.CommandSubmitted(command, inputs.getOrNull(1), inputs.getOrNull(2)))
        } ?: run {
            CliLine.Error(CliLineErrorType.EMPTY_COMMAND).addToState()
        }
        updateState(state.value.copy(commandInput = ""))
    }

    private fun reduce(event: Event) {
        when (event) {
            is Event.CommandSubmitted -> handleCommand(event.command, event.key, event.value)
        }
    }

    private fun handleCommand(command: String, key: String?, value: String?) {
        CliLine.Command(command, key, value).addToState()
        when (command.uppercase()) {
            GET_COMMAND -> {
                key?.let {
                    getValueUseCase(it)
                        .onSuccess { result -> CliLine.Success(result).addToState() }
                        .onFailure { CliLine.Error(CliLineErrorType.ENTRY_DOES_NOT_EXIST).addToState() }
                } ?: CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED).addToState()
            }
            SET_COMMAND -> {
                if (key == null) {
                    CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED).addToState()
                } else if (value == null) {
                    CliLine.Error(CliLineErrorType.VALUE_PARAM_REQUIRED).addToState()
                } else {
                    setValueUseCase(key, value)
                }
            }
            DELETE_COMMAND -> {
                key?.let {
                    deleteValueUseCase(it)
                } ?: CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED).addToState()
            }
            COUNT_COMMAND -> {
                key?.let {
                    CliLine.Success(result = "${countValuesUseCase(it)}").addToState()
                } ?: CliLine.Error(CliLineErrorType.KEY_PARAM_REQUIRED).addToState()
            }
            BEGIN_COMMAND -> {
                beginTransactionUseCase()
            }
            COMMIT_COMMAND -> {
                commitTransactionUseCase()
                    .onFailure { CliLine.Error(CliLineErrorType.NO_TRANSACTION).addToState() }
            }
            ROLLBACK_COMMAND -> {
                rollbackTransactionUseCase()
                    .onFailure { CliLine.Error(CliLineErrorType.NO_TRANSACTION).addToState() }
            }
            else -> {
                CliLine.Error(CliLineErrorType.UNKNOWN_COMMAND).addToState()
            }
        }
    }

    private fun CliLine.addToState() {
        val updatedList = state.value.cliLines.toMutableList()
        updatedList.add(this)
        updateState(state.value.copy(cliLines = updatedList))
    }

    private fun updateState(newState: ViewState) {
        viewModelScope.launch { _state.emit(newState) }
    }

    private sealed class Event {
        data class CommandSubmitted(val command: String, val key: String?, val value: String?) : Event()
    }

    data class ViewState(
        val commandInput: String = "",
        val cliLines: List<CliLine> = emptyList(),
    )

    private companion object {
        const val COMMAND_DELIMITER = " "
        const val GET_COMMAND = "GET"
        const val SET_COMMAND = "SET"
        const val DELETE_COMMAND = "DELETE"
        const val COUNT_COMMAND = "COUNT"
        const val BEGIN_COMMAND = "BEGIN"
        const val COMMIT_COMMAND = "COMMIT"
        const val ROLLBACK_COMMAND = "ROLLBACK"
    }
}
