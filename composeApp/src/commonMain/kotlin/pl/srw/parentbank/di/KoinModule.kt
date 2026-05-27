package pl.srw.parentbank.di

import org.koin.core.module.Module
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
    single<FamilyRepository> { FamilyRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val domainModule = module {
    factory { CreateFamilyUseCase(get(), get()) }
    factory { AddFamilyMemberUseCase(get()) }
    factory { GetFamilyMembersUseCase(get()) }
}

val presentationModule = module {
    factory { FamilySetupViewModel(get(), get(), get()) }
}
