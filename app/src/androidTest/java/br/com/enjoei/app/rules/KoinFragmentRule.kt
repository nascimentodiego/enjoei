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

import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import br.com.enjoei.app.presentation.util.FragmentTestActivity
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class KoinFragmentRule<T : Fragment>(
    val fragmentClass: Class<T>,
    val launchAutomatically: Boolean = true
) : TestRule {

    val koinTestActivity = KoinActivityRule(
        FragmentTestActivity::class.java,
        true,
        true
    )

    val serverRule: RequestMatcherRule
        get() = koinTestActivity.serverRule

    lateinit var fragment: T

    private val delegate = RuleChain
        .outerRule(koinTestActivity)
        .around(AddFragmentRule())

    override fun apply(base: Statement?, description: Description?): Statement {
        return delegate.apply(base, description)
    }

    fun launchFragment(arguments: Bundle? = null) {
        fragment = fragmentClass.newInstance()
        fragment.arguments = arguments
        koinTestActivity.activityRule.activity.setFragment(fragment)
    }

    inner class AddFragmentRule : ExternalResource() {
        override fun before() {
            super.before()
            if (launchAutomatically) {
                fragment = fragmentClass.newInstance()
                koinTestActivity.activityRule.activity.setFragment(fragment)
            }
        }
    }

}