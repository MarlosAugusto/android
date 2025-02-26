/*
 * Nextcloud Android client application
 *
 * @author Chris Narkiewicz
 * Copyright (C) 2019 Chris Narkiewicz <hello@ezaquarii.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.amperbackup.client.jobs

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.amperbackup.client.device.DeviceInfo
import com.amperbackup.client.device.PowerManagementService
import com.amperbackup.client.preferences.AppPreferences
import com.owncloud.android.datamodel.SyncedFolderProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * This factory is responsible for creating all background jobs and for injecting
 * all jobs dependencies.
 */
class BackgroundJobFactory @Inject constructor(
    private val preferences: com.amperbackup.client.preferences.AppPreferences,
    private val contentResolver: ContentResolver,
    private val powerManagerService: PowerManagementService,
    private val backgroundJobManager: Provider<BackgroundJobManager>,
    private val deviceInfo: DeviceInfo
) : WorkerFactory() {

    override fun createWorker(
        context: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        val workerClass = try {
            Class.forName(workerClassName).kotlin
        } catch (ex: ClassNotFoundException) {
            null
        }

        return when (workerClass) {
            ContentObserverWork::class -> createContentObserverJob(context, workerParameters)
            else -> null // falls back to default factory
        }
    }

    private fun createContentObserverJob(context: Context, workerParameters: WorkerParameters): ListenableWorker? {
        val folderResolver = SyncedFolderProvider(contentResolver, preferences)
        @RequiresApi(Build.VERSION_CODES.N)
        if (deviceInfo.apiLevel >= Build.VERSION_CODES.N) {
            return ContentObserverWork(
                context,
                workerParameters,
                folderResolver,
                powerManagerService,
                backgroundJobManager.get()
            )
        } else {
            return null
        }
    }
}
