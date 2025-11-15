package pl.srw.parentbank.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Main Koin module that combines all application modules.
 */
fun appModule(): List<Module> = listOf(
    platformModule(),
    DataModule.dataModule(),
    DomainModule.domainModule()
)

/**
 * Data layer module container.
 */
internal object DataModule {
    /**
     * Data layer module for repositories and data sources.
     * Will be populated as we implement repositories.
     */
    fun dataModule() = module {
        // Repository implementations will be added here
        // Example: single<FamilyRepository> { FamilyRepositoryImpl(get()) }
    }
}

/**
 * Domain layer module container.
 */
internal object DomainModule {
    /**
     * Domain layer module for use cases.
     * Will be populated as we implement use cases.
     */
    fun domainModule() = module {
        // Use cases will be added here
        // Example: factory { CreateFamilyUseCase(get()) }
    }
}
