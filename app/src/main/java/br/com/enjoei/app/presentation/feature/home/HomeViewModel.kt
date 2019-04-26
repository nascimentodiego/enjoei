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

import br.com.enjoei.app.domain.interactor.HomeUseCaseContract
import br.com.enjoei.app.domain.model.ProductList
import br.com.enjoei.app.presentation.base.*
import br.com.enjoei.app.presentation.feature.home.HomeViewModel.*
import br.com.enjoei.app.presentation.util.SingleEvent
import io.reactivex.Observable

class HomeViewModel(
    private val useCase: HomeUseCaseContract,
    private val reducer: HomeReducer
) : BaseViewModel<HomeIntention, HomeSideEffect, HomeScreenState>() {

    init {
        _state.postValue(HomeScreenState())

        subscribeSideEffect()
        subscribeState()
    }

    private fun subscribeSideEffect() {
        addDisposable(
            observerChooseProduct()
                .subscribe {
                    _sideEffect.postValue(SingleEvent(it))
                }
        )
    }

    private fun subscribeState() {
        addDisposable(
            Observable.merge(
                observerInitScreen(),
                observerLoadMoreProducts()
            )
                .subscribe {
                    _state.postValue(it)
                }
        )
    }

    private fun observerInitScreen() =
        baseIntentions
            .ofType(HomeIntention.LoadScreen::class.java)
            .switchMap {
                useCase.initLoad()
            }.map {
                loadState(it)
            }
            .doOnSubscribe { _state.postValue(loadingState()) }
            .onErrorReturn { errorState(it) }

    private fun observerLoadMoreProducts() =
        baseIntentions
            .ofType(HomeIntention.LoadMore::class.java)
            .switchMap {
                useCase.getProductListByPage().doOnSubscribe { _state.postValue(loadingMoreState()) }
            }.map {
                loadMoreState(it)
            }.onErrorReturn { errorState(it) }

    private fun loadingState() =
        reducer.reducer(
            _state.value,
            HomeScreenChange.Loading
        )

    private fun loadingMoreState() =
        reducer.reducer(
            _state.value,
            HomeScreenChange.LoadingMore
        )

    private fun loadState(response: ProductList) =
        reducer.reducer(_state.value, HomeScreenChange.HomeScreenFetched(response))

    private fun loadMoreState(response: ProductList) =
        reducer.reducer(_state.value, HomeScreenChange.HomeScreenFetchMore(response))

    private fun errorState(throwable: Throwable?): HomeScreenState {
        subscribeSideEffect()
        subscribeState()

        return reducer.reducer(_state.value, HomeScreenChange.Error(throwable))
    }

    private fun observerChooseProduct() =
        baseIntentions
            .ofType(HomeIntention.LoadProductDetail::class.java)
            .map {
                HomeSideEffect.NavigateToDetail(it.productView)
            }

    sealed class HomeIntention : BaseIntention {
        object LoadScreen : HomeIntention()
        object LoadMore : HomeIntention()
        data class LoadProductDetail(val productView: HomeReducer.ProductItemView) : HomeIntention()
    }

    sealed class HomeSideEffect : BaseSideEffect {
        data class NavigateToDetail(val productView: HomeReducer.ProductItemView) : HomeSideEffect()
    }

    data class HomeScreenState(
        val productList: List<HomeReducer.ProductItemView> = emptyList(),
        val isLoading: Boolean = true,
        val isLoadingMore: Boolean = true,
        val isLoadError: Boolean = false,
        val errorMessage: String = ""

    ) : BaseState

    sealed class HomeScreenChange : BaseChange {
        object Loading : HomeScreenChange()
        object LoadingMore : HomeScreenChange()
        data class HomeScreenFetched(val response: ProductList) : HomeScreenChange()
        data class HomeScreenFetchMore(val response: ProductList) : HomeScreenChange()
        data class Error(val throwable: Throwable?) : HomeScreenChange()
    }
}