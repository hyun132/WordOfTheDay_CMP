package org.hyun.projectkmp.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.hyun.projectkmp.AndroidVoiceRecognizer
import org.hyun.projectkmp.VoiceRecognizer
import org.hyun.projectkmp.getDatabaseBuilder
import org.hyun.projectkmp.getWordDatabase
import org.hyun.projectkmp.word.data.local.WordRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single<VoiceRecognizer> { AndroidVoiceRecognizer() }
        single<WordRoomDatabase> {
            val builder = getDatabaseBuilder(context = get())
            getWordDatabase(builder)
        }
    }