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
package br.com.enjoei.app.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductItemView(
    val productId: Int = 0,
    val title: String,
    val content: String = "",
    val size: String = "",
    val likes: String = "0",
    val commentCount: String = "0",
    val installmentAndDiscount: String = "",
    val oldPrice: String = "",
    val price: String = "",
    val discount: String = "",
    val photos: List<PhotoView> = emptyList(),
    val avatar: PhotoView = PhotoView()
) : Parcelable