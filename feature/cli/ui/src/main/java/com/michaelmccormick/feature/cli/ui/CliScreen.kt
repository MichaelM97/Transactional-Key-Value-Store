package com.michaelmccormick.feature.cli.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.michaelmccormick.feature.cli.ui.model.CliLine
import com.michaelmccormick.feature.cli.ui.model.CliLineErrorType

@Composable
fun CliScreen(viewModel: CliScreenViewModel) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxHeight()) {
        CliLinesList(
            cliLines = state.cliLines,
            modifier = Modifier.weight(1F),
        )
        CommandEntryField(
            commandInput = state.commandInput,
            onCommandUpdated = viewModel::onCommandUpdated,
            onCommandSubmitted = viewModel::onCommandSubmitted,
        )
    }
}

@Composable
private fun CliLinesList(
    cliLines: List<CliLine>,
    modifier: Modifier,
) {
    val state: LazyListState = rememberLazyListState()
    LaunchedEffect(key1 = cliLines.size) {
        if (cliLines.isNotEmpty()) state.animateScrollToItem(cliLines.size - 1)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .background(color = MaterialTheme.colors.background)
            .testTag(CLI_SCREEN_CLI_LINES_LIST_TAG),
        state = state,
        verticalArrangement = Arrangement.Bottom,
    ) {
        items(items = cliLines) {
            val text = when (it) {
                is CliLine.Command -> "> ${it.command} ${it.key.orEmpty()} ${it.value.orEmpty()}"
                is CliLine.Success -> it.result
                is CliLine.Error -> {
                    stringResource(
                        id = when (it.errorType) {
                            CliLineErrorType.EMPTY_COMMAND -> R.string.empty_command_error
                            CliLineErrorType.KEY_PARAM_REQUIRED -> R.string.key_param_required_error
                            CliLineErrorType.VALUE_PARAM_REQUIRED -> R.string.value_param_required_error
                            CliLineErrorType.ENTRY_DOES_NOT_EXIST -> R.string.entry_does_not_exist_error
                            CliLineErrorType.NO_TRANSACTION -> R.string.no_transaction_error
                            CliLineErrorType.UNKNOWN_COMMAND -> R.string.unknown_command_error
                        },
                    )
                }
            }
            Text(text = text)
        }
    }
}

@Composable
private fun CommandEntryField(
    commandInput: String,
    onCommandUpdated: (String) -> Unit,
    onCommandSubmitted: () -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = commandInput,
        onValueChange = { onCommandUpdated(it) },
        label = { Text(text = stringResource(id = R.string.command_entry_field_label)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(onGo = { onCommandSubmitted() }),
        trailingIcon = {
            IconButton(onClick = onCommandSubmitted) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(id = R.string.submit_button_content_description),
                )
            }
        },
    )
}

const val CLI_SCREEN_CLI_LINES_LIST_TAG = "CliScreen_CliLinesList_LazyColumn"
