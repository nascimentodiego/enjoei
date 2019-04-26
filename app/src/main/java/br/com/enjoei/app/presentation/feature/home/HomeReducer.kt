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

import android.text.SpannableString
import br.com.enjoei.app.domain.model.Product
import br.com.enjoei.app.presentation.base.Reducer
import br.com.enjoei.app.presentation.extensions.asBRL
import br.com.enjoei.app.presentation.extensions.strikethroughSpan
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
                    isLoadingMore = false,
                    isLoadError = false
                )
                is HomeScreenChange.LoadingMore -> state.copy(
                    isLoading = false,
                    isLoadingMore = true,
                    isLoadError = false
                )
                is HomeScreenChange.Error -> state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isLoadError = true,
                    productList = mutableListOf(),
                    errorMessage = change.throwable?.message ?: ""
                )
                is HomeScreenChange.HomeScreenFetched -> state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isLoadError = false,
                    productList = change.response.products.map { productMapper(it) }
                )
                is HomeScreenChange.HomeScreenFetchMore -> state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isLoadError = false,
                    productList = state.productList.plus(change.response.products.map { productMapper(it) })
                )
            }
        } ?: HomeScreenState()

    private fun productMapper(productRsponse: Product): ProductItemView {

        val spannablePrice = SpannableString(productRsponse.originalPrice.asBRL(true))
        spannablePrice.strikethroughSpan()

        val user = productRsponse.user.avatar
        val discount = if (productRsponse.discount == 0.0) "" else "-${productRsponse.discount.toInt()}%"

        return ProductItemView(
            productId = productRsponse.id,
            title = productRsponse.title,
            size = productRsponse.size ?: "",
            likes = productRsponse.likes.toString(),
            price = productRsponse.price.asBRL(true),
            oldPrice = spannablePrice,
            discount = discount,
            avatar = PhotoView(user.id, user.crop, user.gravity),
            photos = productRsponse.photos.map { photo -> PhotoView(photo.id, photo.crop, photo.gravity) }
        )
    }

    data class ProductItemView(
        val productId: Int = 0,
        val title: String,
        val content: String = "",
        val size: String = "",
        val likes: String = "0",
        val oldPrice: SpannableString = SpannableString(""),
        val price: String = "",
        val discount: String = "",
        val photos: List<PhotoView> = emptyList(),
        val avatar: PhotoView = PhotoView()
    )

    data class PhotoView(
        val id: String = "",
        val crop: String = "",
        val gravity: String = ""
    )

}