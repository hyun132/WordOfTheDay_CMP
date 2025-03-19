package org.hyun.projectkmp

import android.app.Application
import org.hyun.projectkmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class WordApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@WordApplication)
        }
    }
}