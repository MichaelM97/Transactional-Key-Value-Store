package com.michaelmccormick.feature.cli.ui.model

enum class CliLineErrorType {
    EMPTY_COMMAND,
    KEY_PARAM_REQUIRED,
    VALUE_PARAM_REQUIRED,
    ENTRY_DOES_NOT_EXIST,
    NO_TRANSACTION,
    UNKNOWN_COMMAND,
}
