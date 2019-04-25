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
package br.com.enjoei.app.domain.interactor

import br.com.enjoei.app.data.remote.model.ProductListResponse
import br.com.enjoei.app.domain.repository.HomeRepositoryContract
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class HomeUseCase(private val repository: HomeRepositoryContract) : HomeUseCaseContract {

    private var currentPage = 1
    private var totalPage = 1

    override fun initLoad(): Observable<ProductListResponse> =
        repository.fetchProductList()
            .map {
                it.apply {
                    currentPage = this.paginationResponse.current
                    totalPage = this.paginationResponse.total
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    override fun getProductListByPage(): Observable<ProductListResponse> =
        repository.getProductListByPage(currentPage)
            .map {
                it.apply {
                    currentPage = this.paginationResponse.current
                    totalPage = this.paginationResponse.total
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

}