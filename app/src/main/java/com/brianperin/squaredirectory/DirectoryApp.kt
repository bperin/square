package com.brianperin.squaredirectory

import android.app.Application
import timber.log.Timber

class DirectoryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}