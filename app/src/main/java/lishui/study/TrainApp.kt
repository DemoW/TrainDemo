/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lishui.study

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.lifecycle.ProcessLifecycleOwner
import lishui.study.common.crash.CrashHandler
import lishui.study.common.util.Utilities
import lishui.study.config.FeatureFlags

/**
 * Android Application class. Used for accessing singletons.
 */
class TrainApp : Application() {

    override fun onCreate() {

        if (FeatureFlags.DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }
        super.onCreate()

        init(this)
    }

    private fun init(trainApp: TrainApp) {
        val lifeCycleObserver = AppLifeCycleObserver(trainApp)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifeCycleObserver)

        if (Utilities.isDevelopersOptionsEnabled(trainApp)) {
            CrashHandler.getInstance().init(trainApp)
        }
    }
}