package org.hyun.projectkmp.di

import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    viewModelOf(::WordHomeViewModel)
}