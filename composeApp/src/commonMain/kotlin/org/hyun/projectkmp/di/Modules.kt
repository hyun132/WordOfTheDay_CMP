package org.hyun.projectkmp.di

import com.russhwolf.settings.Settings
import org.hyun.projectkmp.auth.data.network.KtorRemoteAuthDataSource
import org.hyun.projectkmp.auth.data.network.RemoteAuthDataSource
import org.hyun.projectkmp.auth.data.reporitory.DefaultAuthRepository
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.auth.presentation.login.LoginViewModel
import org.hyun.projectkmp.auth.presentation.reset_password.ResetPasswordViewModel
import org.hyun.projectkmp.auth.presentation.signup.SignupViewModel
import org.hyun.projectkmp.core.data.HttpClientFactory
import org.hyun.projectkmp.word.data.local.WordDao
import org.hyun.projectkmp.word.data.local.WordRoomDatabase
import org.hyun.projectkmp.word.data.network.KtorRemoteWordDataSource
import org.hyun.projectkmp.word.data.network.RemoteWordDataSource
import org.hyun.projectkmp.word.data.repository.DefaultWordRepository
import org.hyun.projectkmp.word.data.repository.LocalRepository
import org.hyun.projectkmp.word.domain.repository.WordRepository
import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.hyun.projectkmp.word.presentation.bookmark.BookmarkViewModel
import org.hyun.projectkmp.word.presentation.history.HistoryViewModel
import org.hyun.projectkmp.word.presentation.learning.LearningViewModel
import org.hyun.projectkmp.word.presentation.profile.ProfileViewModel
import org.hyun.projectkmp.word.presentation.profile.create.CreateProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<WordDao> { get<WordRoomDatabase>().wordDao() }

    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteWordDataSource).bind<RemoteWordDataSource>()
    singleOf(::DefaultWordRepository).bind<WordRepository>()
    singleOf(::LocalRepository)

    singleOf(::KtorRemoteAuthDataSource).bind<RemoteAuthDataSource>()
    singleOf(::DefaultAuthRepository).bind<RemoteRepository>()
    single { Settings() }

    viewModelOf(::WordHomeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::BookmarkViewModel)
    viewModelOf(::ProfileViewModel)
    viewModel { LearningViewModel(get(), get(), get()) }
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::CreateProfileViewModel)
    viewModelOf(::ResetPasswordViewModel)
}