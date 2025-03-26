package org.hyun.projectkmp.di

import com.russhwolf.settings.Settings
import org.hyun.projectkmp.core.data.HttpClientFactory
import org.hyun.projectkmp.word.data.network.KtorRemoteWordDataSource
import org.hyun.projectkmp.word.data.network.RemoteWordDataSource
import org.hyun.projectkmp.word.data.repository.DefaultWordRepository
import org.hyun.projectkmp.word.domain.repository.WordRepository
import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.hyun.projectkmp.word.presentation.bookmark.BookmarkViewModel
import org.hyun.projectkmp.word.presentation.learning.LearningViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteWordDataSource).bind<RemoteWordDataSource>()
    singleOf(::DefaultWordRepository).bind<WordRepository>()

    viewModelOf(::WordHomeViewModel)
    viewModelOf(::LearningViewModel)
    viewModelOf(::BookmarkViewModel)
}