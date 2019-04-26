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
package br.com.enjoei.app.feature.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import br.com.enjoei.app.R
import br.com.enjoei.app.matchers.RecyclerPositionViewMatcher.withRecyclerView
import okhttp3.mockwebserver.MockResponse

fun HomeFragmentTest.homeFragmentTest(block: HomeRobot.() -> Unit) =
    HomeRobot().apply {
        fragmentRule.launchFragment()
        block()
    }

class HomeRobot {
    private fun productRecycleView() = R.id.recyclerView
    private fun error() = R.id.imageViewError

    fun checkProductIsDisplayed(atPosition: Int) {
        withRecyclerView(productRecycleView(), atPosition).matches(isDisplayed())
    }

    fun checkScreenError() {
        onView(withId(error())).check(matches(isDisplayed()))
    }

    fun mockProducts(serverRule: RequestMatcherRule) {
        serverRule.addFixture("home/fixture_get_products.json")
            .ifRequestMatches()
            .pathIs("/products/home")
    }

    fun mockLoadFailed(serverRule: RequestMatcherRule) {
        serverRule.addResponse(MockResponse().setResponseCode(500))
            .ifRequestMatches()
            .pathIs("/products/home")
    }
}
