package com.michaelmccormick.feature.cli.ui.di

import com.michaelmccormick.core.di.Factory
import com.michaelmccormick.domain.BeginTransactionUseCase
import com.michaelmccormick.domain.CommitTransactionUseCase
import com.michaelmccormick.domain.CountValuesUseCase
import com.michaelmccormick.domain.DeleteValueUseCase
import com.michaelmccormick.domain.GetValueUseCase
import com.michaelmccormick.domain.RollbackTransactionUseCase
import com.michaelmccormick.domain.SetValueUseCase
import com.michaelmccormick.feature.cli.ui.CliScreenViewModel

class CliScreenViewModelFactory(
    private val getValueUseCase: GetValueUseCase,
    private val setValueUseCase: SetValueUseCase,
    private val deleteValueUseCase: DeleteValueUseCase,
    private val countValuesUseCase: CountValuesUseCase,
    private val beginTransactionUseCase: BeginTransactionUseCase,
    private val commitTransactionUseCase: CommitTransactionUseCase,
    private val rollbackTransactionUseCase: RollbackTransactionUseCase,
) : Factory<CliScreenViewModel> {
    override fun create(): CliScreenViewModel {
        return CliScreenViewModel(
            getValueUseCase = getValueUseCase,
            setValueUseCase = setValueUseCase,
            deleteValueUseCase = deleteValueUseCase,
            countValuesUseCase = countValuesUseCase,
            beginTransactionUseCase = beginTransactionUseCase,
            commitTransactionUseCase = commitTransactionUseCase,
            rollbackTransactionUseCase = rollbackTransactionUseCase,
        )
    }
}
