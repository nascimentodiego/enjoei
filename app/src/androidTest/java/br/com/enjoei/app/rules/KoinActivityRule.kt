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
package br.com.enjoei.app.rules

import android.app.Activity
import android.app.Application
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import br.com.enjoei.app.presentation.di.*
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestName
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class KoinActivityRule<T : Activity> constructor(
    activityClass: Class<T>,
    initialTouchMode: Boolean = true,
    launchActivity: Boolean = true
) : TestRule, KoinComponent {

    private val testName = TestName()
    val activityRule = ActivityTestRule<T>(activityClass, initialTouchMode, launchActivity)
    val serverRule: RequestMatcherRule = InstrumentedTestRequestMatcherRule()

    private val delegateRule = RuleChain
        .outerRule(testName)
        .around(serverRule)
        .around(getKoinRule())
        .around(activityRule)

    override fun apply(base: Statement?, description: Description?): Statement {
        return delegateRule.apply(base, description)
    }

    private fun getKoinRule(): StartAndEndKoinRule {
        val application =
            InstrumentationRegistry.getTargetContext().applicationContext as Application
        val baseUrl = serverRule.url("/").toString()
        return StartAndEndKoinRule(application, baseUrl)
    }

    inner class StartAndEndKoinRule(
        private val app: Application,
        private val baseUrl: String
    ) : ExternalResource() {

        override fun before() {
            super.before()
            startKoin {
                // inject Android context
                androidContext(app)

                modules(
                    listOf(
                        androidModule,
                        interactorModule,
                        repositoryModule,
                        viewModelModule,
                        networkModule
                    )
                )
                properties(
                    mapOf(
                        PROPERTY_BASE_URL to baseUrl
                    )
                )
            }
        }

        override fun after() {
            stopKoin()
            super.after()
        }
    }
}