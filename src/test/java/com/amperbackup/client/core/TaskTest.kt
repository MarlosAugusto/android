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
package com.amperbackup.client.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.same
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TaskTest {

    @Mock
    private lateinit var taskBody: () -> String
    @Mock
    private lateinit var onResult: OnResultCallback<String>
    @Mock
    private lateinit var onError: OnErrorCallback

    private lateinit var task: Task<String>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val postResult = { r: Runnable -> r.run() }
        task = Task(postResult, taskBody, onResult, onError)
    }

    @Test
    fun `task result is posted`() {
        whenever(taskBody.invoke()).thenReturn("result")
        task.run()
        verify(onResult).invoke(eq("result"))
        verify(onError, never()).invoke(any())
    }

    @Test
    fun `task result is not posted when cancelled`() {
        whenever(taskBody.invoke()).thenReturn("result")
        task.cancel()
        task.run()
        verify(onResult, never()).invoke(any())
        verify(onError, never()).invoke(any())
    }

    @Test
    fun `task error is posted`() {
        val exception = RuntimeException("")
        whenever(taskBody.invoke()).thenThrow(exception)
        task.run()
        verify(onResult, never()).invoke(any())
        verify(onError).invoke(same(exception))
    }

    @Test
    fun `task error is not posted when cancelled`() {
        val exception = RuntimeException("")
        whenever(taskBody.invoke()).thenThrow(exception)
        task.cancel()
        task.run()
        verify(onResult, never()).invoke(any())
        verify(onError, never()).invoke(any())
    }
}
