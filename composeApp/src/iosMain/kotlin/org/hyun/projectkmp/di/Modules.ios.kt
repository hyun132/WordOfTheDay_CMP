package org.hyun.projectkmp.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.hyun.projectkmp.IOSVoiceRecognizer
import org.hyun.projectkmp.VoiceRecognizer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> {
            Darwin.create()
        }
        single<VoiceRecognizer> { IOSVoiceRecognizer() }
    }