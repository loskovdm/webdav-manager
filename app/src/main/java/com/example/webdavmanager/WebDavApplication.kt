package com.example.webdavmanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.webdavmanager.file_manager.data.repository.AndroidFileRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WebDavApplication : Application() {
    @Inject
    lateinit var androidFileRepository: AndroidFileRepository

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
                        androidFileRepository.clearCache()
                    }
                }
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}