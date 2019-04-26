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

import br.com.enjoei.app.domain.model.Product
import br.com.enjoei.app.presentation.extensions.asBRL
import br.com.enjoei.app.presentation.feature.home.HomeReducer
import br.com.enjoei.app.presentation.feature.home.HomeViewModel
import br.com.enjoei.app.presentation.model.PhotoView
import br.com.enjoei.app.presentation.model.ProductItemView


class HomeReducerMock : HomeReducer {
    override fun reducer(
        state: HomeViewModel.HomeScreenState?,
        change: HomeViewModel.HomeScreenChange
    ): HomeViewModel.HomeScreenState =
        state?.let {
            when (change) {
                is HomeViewModel.HomeScreenChange.Loading -> state.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    isLoadError = false
                )
                is HomeViewModel.HomeScreenChange.LoadingMore -> state.copy(
                    isLoading = false,
                    isLoadingMore = true,
                    isLoadError = false
                )
                is HomeViewModel.HomeScreenChange.Error -> state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isLoadError = true,
                    productList = mutableListOf(),
                    errorMessage = change.throwable?.message ?: ""
                )
                is HomeViewModel.HomeScreenChange.HomeScreenFetched -> state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isLoadError = false,
                    productList = change.response.products.map { productMapper(it) }
                )
                is HomeViewModel.HomeScreenChange.HomeScreenFetchMore -> state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isLoadError = false,
                    productList = state.productList.plus(change.response.products.map { productMapper(it) })
                )
            }
        } ?: HomeViewModel.HomeScreenState()


    override fun productMapper(product: Product): ProductItemView {
        val user = product.user.avatar
        val discount = if (product.discount == 0.0) "" else "-${product.discount.toInt()}%"

        return ProductItemView(
            productId = product.id,
            title = product.title,
            content = product.content,
            size = product.size ?: "",
            likes = product.likes.toString(),
            commentCount = product.commentsCount.toString(),
            installmentAndDiscount = product.maxInstallment.toString(),
            price = product.price.asBRL(true),
            oldPrice = if (product.originalPrice == product.price) "" else product.originalPrice.asBRL(true),
            discount = discount,
            avatar = PhotoView(user.id, user.crop, user.gravity),
            photos = product.photos.map { photo -> PhotoView(photo.id, photo.crop, photo.gravity) }
        )
    }
}