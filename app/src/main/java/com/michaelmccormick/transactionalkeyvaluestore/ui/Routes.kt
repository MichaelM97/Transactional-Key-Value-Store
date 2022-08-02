package com.michaelmccormick.transactionalkeyvaluestore.ui

sealed class Routes(val route: String) {
    object Cli : Routes("cli")
}
