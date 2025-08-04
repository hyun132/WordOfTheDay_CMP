package org.hyun.projectkmp.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.hyun.projectkmp.DesktopVoiceRecognizer
import org.hyun.projectkmp.VoiceRecognizer
import org.hyun.projectkmp.getDatabaseBuilder
import org.hyun.projectkmp.getWordDatabase
import org.hyun.projectkmp.word.data.local.WordRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single<VoiceRecognizer> { DesktopVoiceRecognizer() }
        single<WordRoomDatabase> {
            val builder = getDatabaseBuilder()
            getWordDatabase(builder)
        }
    }