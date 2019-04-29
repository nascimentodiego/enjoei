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
package br.com.enjoei.app.presentation.di

import br.com.enjoei.app.BuildConfig
import br.com.enjoei.app.data.remote.ProductApi
import br.com.enjoei.app.presentation.util.BuildConfigName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val PROPERTY_BASE_URL = "baseUrl"
private const val TIMEOUT = 5L

val networkModule = module {

    single {
        HttpLoggingInterceptor().apply {
            val debugVariants = arrayOf(BuildConfigName.DEBUG)

            level = if (debugVariants.contains(BuildConfig.BUILD_TYPE)) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        } as Interceptor
    }

    single {
        OkHttpClient
            .Builder()
            .addInterceptor(get())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    single {
        val baseUrl = getProperty<String>(PROPERTY_BASE_URL)

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single {
        val retrofit: Retrofit = get()

        retrofit.create(ProductApi::class.java)
    }
}