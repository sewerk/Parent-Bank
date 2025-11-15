package pl.srw.parentbank.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Main Koin module that combines all application modules.
 */
fun appModule(): List<Module> = listOf(
    platformModule(),
    DataModule.dataModule(),
    DomainModule.domainModule(),
    PresentationModule.presentationModule()
)

/**
 * Data layer module container.
 */
internal object DataModule {
    /**
     * Data layer module for repositories and data sources.
     */
    fun dataModule() = module {
        // Repository implementations
        single<pl.srw.parentbank.domain.repository.FamilyRepository> {
            pl.srw.parentbank.data.repository.FamilyRepositoryImpl(get())
        }
        single<pl.srw.parentbank.domain.repository.UserRepository> {
            pl.srw.parentbank.data.repository.UserRepositoryImpl(get())
        }
        single<pl.srw.parentbank.domain.repository.AccountRepository> {
            pl.srw.parentbank.data.repository.AccountRepositoryImpl(get())
        }
        single<pl.srw.parentbank.domain.repository.TransactionRepository> {
            pl.srw.parentbank.data.repository.TransactionRepositoryImpl(get())
        }
        single<pl.srw.parentbank.domain.repository.ScheduledTransactionRepository> {
            pl.srw.parentbank.data.repository.ScheduledTransactionRepositoryImpl(get())
        }
    }
}

/**
 * Domain layer module container.
 */
internal object DomainModule {
    /**
     * Domain layer module for use cases.
     */
    fun domainModule() = module {
        // Family use cases
        factory { pl.srw.parentbank.domain.usecase.family.CreateFamilyUseCase(get()) }
        factory { pl.srw.parentbank.domain.usecase.family.JoinFamilyUseCase(get()) }

        // User use cases
        factory { pl.srw.parentbank.domain.usecase.user.CreateUserUseCase(get()) }

        // Account use cases
        factory { pl.srw.parentbank.domain.usecase.account.CreateAccountUseCase(get()) }

        // Transaction use cases
        factory { pl.srw.parentbank.domain.usecase.transaction.CreateTransactionUseCase(get(), get(), get()) }
        factory { pl.srw.parentbank.domain.usecase.transaction.ApproveTransactionUseCase(get(), get(), get()) }
        factory { pl.srw.parentbank.domain.usecase.transaction.DenyTransactionUseCase(get(), get()) }

        // Scheduled transaction use cases
        factory { pl.srw.parentbank.domain.usecase.scheduled.CreateScheduledTransactionUseCase(get(), get()) }
        factory { pl.srw.parentbank.domain.usecase.scheduled.ExecuteScheduledTransactionsUseCase(get(), get(), get()) }
    }
}

/**
 * Presentation layer module container.
 */
internal object PresentationModule {
    /**
     * Presentation layer module for ViewModels.
     */
    fun presentationModule() = module {
        // ViewModels
        factory { pl.srw.parentbank.presentation.family.FamilyViewModel(get(), get()) }
        factory { pl.srw.parentbank.presentation.user.UserViewModel(get(), get()) }
        factory { pl.srw.parentbank.presentation.dashboard.DashboardViewModel(get(), get(), get()) }
        factory { pl.srw.parentbank.presentation.transaction.TransactionViewModel(get(), get(), get()) }
    }
}
