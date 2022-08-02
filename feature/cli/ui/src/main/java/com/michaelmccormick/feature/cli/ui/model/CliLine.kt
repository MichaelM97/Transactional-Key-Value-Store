package com.michaelmccormick.feature.cli.ui.model

sealed class CliLine {
    data class Command(val command: String, val key: String?, val value: String?) : CliLine()
    data class Success(val result: String) : CliLine()
    data class Error(val errorType: CliLineErrorType) : CliLine()
}
