package pl.srw.parentbank.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.srw.parentbank.data.repository.FamilyRepositoryImpl
import pl.srw.parentbank.data.repository.UserRepositoryImpl
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.repository.FamilyRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.domain.usecase.AddFamilyMemberUseCase
import pl.srw.parentbank.domain.usecase.CreateFamilyUseCase
import pl.srw.parentbank.domain.usecase.GetFamilyMembersUseCase
import pl.srw.parentbank.presentation.setup.FamilySetupViewModel

fun appModule(): List<Module> = listOf(
    platformModule(),
    dataModule,
    domainModule,
    presentationModule
)

val dataModule = module {
    single { ParentBankDatabase(get()) }
    singleOf(::FamilyRepositoryImpl) bind FamilyRepository::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}

val domainModule = module {
    factoryOf(::CreateFamilyUseCase)
    factoryOf(::AddFamilyMemberUseCase)
    factoryOf(::GetFamilyMembersUseCase)
}

val presentationModule = module {
    viewModelOf(::FamilySetupViewModel)
}
