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
package br.com.enjoei.app.presentation

import android.app.Application
import br.com.enjoei.app.BuildConfig
import br.com.enjoei.app.presentation.di.*
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.core.context.startKoin
import java.io.IOException
import java.net.SocketException

class EnjoeiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupRxJavaDefaultErrorHandler()
    }

    private fun setupKoin() {
        startKoin {
            modules(
                listOf(
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

    private fun setupRxJavaDefaultErrorHandler() {
        RxJavaPlugins.setErrorHandler { error ->
            var e: Throwable? = error
            if (e is UndeliverableException) {
                e = e.cause
            }
            if (e is IOException || e is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that's likely a bug in the application
                val currentThread = Thread.currentThread()
                currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, e)
                return@setErrorHandler
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                val currentThread = Thread.currentThread()
                currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, e)
                return@setErrorHandler
            }
        }
    }
}