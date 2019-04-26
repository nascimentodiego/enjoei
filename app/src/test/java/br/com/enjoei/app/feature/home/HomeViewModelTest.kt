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

import br.com.enjoei.app.domain.model.ProductList
import br.com.enjoei.app.feature.base.BaseViewModelTest
import br.com.enjoei.app.presentation.feature.home.HomeReducer
import br.com.enjoei.app.presentation.feature.home.HomeViewModel
import br.com.enjoei.app.rules.TestUtil
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.koin.test.inject

class HomeViewModelTest : BaseViewModelTest() {

    private val viewModel: HomeViewModel by inject()
    private val homeReducerMock: HomeReducer by inject()

    @Test
    fun whenUserInitApp_mustBeShownListOfProducts() {
        // Given
        val responseJson = TestUtil.loadTextFile("home/fixture_product_list.json")
        val products = getMockedProductList(responseJson)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        val actual = mutableListOf<HomeViewModel.HomeScreenState>()
        val expected = mutableListOf(
            HomeViewModel.HomeScreenState(
                isLoading = true,
                isLoadingMore = false,
                isLoadError = false,
                productList = emptyList()
            ),
            HomeViewModel.HomeScreenState(
                isLoading = false,
                isLoadingMore = false,
                isLoadError = false,
                productList = products.products.map { homeReducerMock.productMapper(it) }
            )
        )

        // When
        viewModel.states.observeForever {
            it?.let { state ->
                actual.add(state)
            }
        }

        viewModel.execute(HomeViewModel.HomeIntention.LoadScreen)

        // Then
        Assert.assertEquals(expected, actual)
    }

    private fun getMockedProductList(json: String): ProductList {
        return moshi.adapter(ProductList::class.java).fromJson(json)!!
    }
}