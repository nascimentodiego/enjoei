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
package br.com.enjoei.app.presentation.feature.home

import br.com.enjoei.app.presentation.base.Reducer
import br.com.enjoei.app.presentation.feature.home.HomeViewModel.HomeScreenChange
import br.com.enjoei.app.presentation.feature.home.HomeViewModel.HomeScreenState

class HomeReducer : Reducer<HomeScreenState?, HomeScreenChange> {
    override fun reducer(
        state: HomeScreenState?,
        change: HomeScreenChange
    ): HomeScreenState =
        state?.let {
            when (change) {
                is HomeScreenChange.Loading -> state.copy(
                    isLoading = true,
                    isLoadError = false,
                    hasMorePage = false,
                    productList = mutableListOf()
                )
                is HomeScreenChange.Error -> state.copy(
                    isLoading = false,
                    isLoadError = true,
                    productList = mutableListOf(),
                    errorMessage = change.throwable?.message ?: ""
                )
                is HomeScreenChange.HomeScreenFetched -> state.copy(
                    isLoading = false,
                    isLoadError = false,
                    hasMorePage = false,
                    productList = state.productList
                )
            }
        } ?: HomeScreenState()
}