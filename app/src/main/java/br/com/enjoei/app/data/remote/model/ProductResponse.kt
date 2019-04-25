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
package br.com.enjoei.app.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    val id: Int = 0,
    val content: String = "",
    val title: String = "",
    val size: String? = "",
    val price: Double = 0.0,
    @field: Json(name = "original_price") val originalPrice: Double = 0.0,
    @field: Json(name = "discount_percentage") val discount: Double = 0.0,
    @field: Json(name = "likes_count") val likes: Int = 0,
    val photos: List<PhotoResponse> = emptyList(),
    val user: UserResponse = UserResponse()
)