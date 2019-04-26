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

import android.content.Context
import br.com.enjoei.app.R
import br.com.enjoei.app.domain.model.Product
import br.com.enjoei.app.presentation.base.Reducer
import br.com.enjoei.app.presentation.extensions.asBRL
import br.com.enjoei.app.presentation.feature.home.HomeViewModel.HomeScreenChange
import br.com.enjoei.app.presentation.feature.home.HomeViewModel.HomeScreenState
import br.com.enjoei.app.presentation.model.PhotoView
import br.com.enjoei.app.presentation.model.ProductItemView
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeReducer : Reducer<HomeScreenState?, HomeScreenChange>, KoinComponent {

    private val context: Context  by inject()

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

    private fun productMapper(product: Product): ProductItemView {

        val user = product.user.avatar
        val discount = if (product.discount == 0.0) "" else "-${product.discount.toInt()}%"

        return ProductItemView(
            productId = product.id,
            title = product.title,
            content = product.content,
            size = buildSizeText(product.size),
            likes = product.likes.toString(),
            commentCount = product.commentsCount.toString(),
            installmentAndDiscount = buildInstallmentAndDiscountText(
                product.maxInstallment.toString(),
                product.discount
            ),
            price = product.price.asBRL(true),
            oldPrice = if (product.originalPrice == product.price) "" else product.originalPrice.asBRL(true),
            discount = discount,
            avatar = PhotoView(user.id, user.crop, user.gravity),
            photos = product.photos.map { photo -> PhotoView(photo.id, photo.crop, photo.gravity) }
        )
    }

    private fun buildSizeText(size: String?): String {
        return if (size == null) {
            ""
        } else {
            if (size.isEmpty())
                ""
            else {
                " - ${context.getString(R.string.screen_product_size_label) + " $size"}"
            }
        }
    }

    private fun buildInstallmentAndDiscountText(maxInstallment: String, discount: Double): String {
        val discount =
            if (discount == 0.0) "" else "${discount.toInt()} ${context.getString(R.string.screen_product_discount_label)} "

        val installment = if (maxInstallment == "0") "" else context.getString(
            R.string.screen_product_installment_label,
            maxInstallment
        )

        return "$discount $installment"
    }


}