package com.michaelmccormick.transactionalkeyvaluestore.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.michaelmccormick.core.ui.ComposeDi
import com.michaelmccormick.feature.cli.ui.CliScreen
import com.michaelmccormick.feature.cli.ui.di.CliUiComponent
import com.michaelmccormick.transactionalkeyvaluestore.AppComponent
import com.michaelmccormick.transactionalkeyvaluestore.KeyValueStoreApplication
import com.michaelmccormick.transactionalkeyvaluestore.ui.theme.TransactionalKeyValueStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent: AppComponent = (application as KeyValueStoreApplication).appComponent
        setContent {
            TransactionalKeyValueStoreTheme {
                NavigationGraph(appComponent)
            }
        }
    }
}

@Composable
private fun NavigationGraph(appComponent: AppComponent) {
    NavHost(
        navController = rememberNavController(),
        startDestination = Routes.Cli.route,
    ) {
        composable(Routes.Cli.route) {
            val cliUiComponent = CliUiComponent(
                cliDomainModule = appComponent.cliDomainModule,
            )
            CliScreen(
                viewModel = ComposeDi.viewModelFactory(cliUiComponent.cliScreenViewModelFactory),
            )
        }
    }
}
