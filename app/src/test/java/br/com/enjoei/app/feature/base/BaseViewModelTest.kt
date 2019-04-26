/*
 * Copyright (C) 2018 Diego Figueredo do Nascimento.
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
package br.com.enjoei.app.feature.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.enjoei.app.BuildConfig
import br.com.enjoei.app.di.androidMockModule
import br.com.enjoei.app.presentation.di.*
import br.com.enjoei.app.util.RxImmediateSchedulerRule
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest


abstract class BaseViewModelTest : KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxRule = RxImmediateSchedulerRule()

    var mockServer: MockWebServer = MockWebServer()
    val moshi = Moshi.Builder().build()

    @Before
    @Throws
    fun setUp() {
        setupKoin()
        mockServer.start()
    }

    @After
    @Throws
    fun tearDown() {
        stopKoin()
        mockServer.shutdown()
    }

    private fun setupKoin() {
        startKoin {
            modules(
                listOf(
                    androidMockModule,
                    interactorModule,
                    repositoryModule,
                    viewModelModule,
                    networkModule
                )
            )
            properties(
                mapOf(
                    PROPERTY_BASE_URL to BuildConfig.API_BASE
                )
            )
        }
    }
}