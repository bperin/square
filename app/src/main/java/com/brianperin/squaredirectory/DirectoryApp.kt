package com.brianperin.squaredirectory

import android.app.Application
import com.facebook.cache.common.CacheKey
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker
import com.facebook.imagepipeline.cache.MemoryCache
import com.facebook.imagepipeline.core.ImagePipelineConfig
import timber.log.Timber

class DirectoryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this, ImagePipelineConfig.newBuilder(this).setDiskCacheEnabled(true).build())
    }

}