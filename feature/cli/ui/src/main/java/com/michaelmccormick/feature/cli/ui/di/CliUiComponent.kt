package com.michaelmccormick.feature.cli.ui.di

import com.michaelmccormick.domain.di.CliDomainModule

class CliUiComponent(
    cliDomainModule: CliDomainModule,
) {
    val cliScreenViewModelFactory: CliScreenViewModelFactory = CliScreenViewModelFactory(
        getValueUseCase = cliDomainModule.getValueUseCaseFactory.create(),
        setValueUseCase = cliDomainModule.setValueUseCaseFactory.create(),
        deleteValueUseCase = cliDomainModule.deleteValueUseCaseFactory.create(),
        countValuesUseCase = cliDomainModule.countValuesUseCaseFactory.create(),
        beginTransactionUseCase = cliDomainModule.beginTransactionUseCaseFactory.create(),
        commitTransactionUseCase = cliDomainModule.commitTransactionUseCaseFactory.create(),
        rollbackTransactionUseCase = cliDomainModule.rollbackTransactionUseCaseFactory.create(),
    )
}
