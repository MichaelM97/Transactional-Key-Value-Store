package com.michaelmccormick.domain.di

import com.michaelmccormick.repository.KeyValueStoreRepository

class CliDomainModule(
    keyValueStoreRepository: KeyValueStoreRepository,
) {
    val getValueUseCaseFactory: GetValueUseCaseFactory = GetValueUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
    val setValueUseCaseFactory: SetValueUseCaseFactory = SetValueUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
    val deleteValueUseCaseFactory: DeleteValueUseCaseFactory = DeleteValueUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
    val countValuesUseCaseFactory: CountValuesUseCaseFactory = CountValuesUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
    val beginTransactionUseCaseFactory: BeginTransactionUseCaseFactory = BeginTransactionUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
    val commitTransactionUseCaseFactory: CommitTransactionUseCaseFactory = CommitTransactionUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
    val rollbackTransactionUseCaseFactory: RollbackTransactionUseCaseFactory = RollbackTransactionUseCaseFactory(
        keyValueStoreRepository = keyValueStoreRepository,
    )
}
