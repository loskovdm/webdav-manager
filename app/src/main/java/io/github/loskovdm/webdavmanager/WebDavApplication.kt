package io.github.loskovdm.webdavmanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp
import io.github.loskovdm.webdavmanager.util.CacheManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WebDavApplication : Application() {
    @Inject
    lateinit var cacheManager: CacheManager

    private var activeActivities = 0

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) { activeActivities++ }
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(p0: Activity) {
                activeActivities--
                if (activeActivities == 0) {
                    CoroutineScope(Dispatchers.IO).launch {
                        cacheManager.clearCache()
                    }
                }
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}