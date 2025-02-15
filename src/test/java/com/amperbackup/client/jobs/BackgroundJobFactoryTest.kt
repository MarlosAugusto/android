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
import androidx.work.WorkerParameters
import com.amperbackup.client.device.DeviceInfo
import com.amperbackup.client.device.PowerManagementService
import com.amperbackup.client.preferences.AppPreferences
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Provider

class BackgroundJobFactoryTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var params: WorkerParameters

    @Mock
    private lateinit var contentResolver: ContentResolver

    @Mock
    private lateinit var preferences: com.amperbackup.client.preferences.AppPreferences

    @Mock
    private lateinit var powerManagementService: PowerManagementService

    @Mock
    private lateinit var backgroundJobManager: BackgroundJobManager

    @Mock
    private lateinit var deviceInfo: DeviceInfo

    private lateinit var factory: BackgroundJobFactory

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        factory = BackgroundJobFactory(
            preferences,
            contentResolver,
            powerManagementService,
            Provider { backgroundJobManager },
            deviceInfo
        )
    }

    @Test
    fun `worker is created on api level 24+`() {
        // GIVEN
        //      api level is > 24
        //      content URI trigger is supported
        whenever(deviceInfo.apiLevel).thenReturn(Build.VERSION_CODES.N)

        // WHEN
        //      factory is called to create content observer worker
        val worker = factory.createWorker(context, ContentObserverWork::class.java.name, params)

        // THEN
        //      factory creates a worker compatible with API level
        assertNotNull(worker)
    }

    @Test
    fun `worker is not created below api level 24`() {
        // GIVEN
        //      api level is < 24
        //      content URI trigger is not supported
        whenever(deviceInfo.apiLevel).thenReturn(Build.VERSION_CODES.M)

        // WHEN
        //      factory is called to create content observer worker
        val worker = factory.createWorker(context, ContentObserverWork::class.java.name, params)

        // THEN
        //      factory does not create a worker incompatible with API level
        assertNull(worker)
    }
}
