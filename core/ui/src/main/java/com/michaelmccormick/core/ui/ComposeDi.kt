package com.michaelmccormick.core.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelmccormick.core.di.Factory

object ComposeDi {
    @Composable
    inline fun <reified T : ViewModel> viewModelFactory(
        viewModelFactory: Factory<T>,
    ): T = viewModel(
        modelClass = T::class.java,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create() as T
            }
        },
    )
}
