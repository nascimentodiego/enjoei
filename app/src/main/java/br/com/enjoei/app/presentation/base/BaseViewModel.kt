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
package br.com.enjoei.app.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.enjoei.app.presentation.util.SingleEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<I : BaseIntention, E : BaseSideEffect, S : BaseState> : ViewModel() {

    private val baseDisposables = CompositeDisposable()
    protected val baseIntentions = PublishSubject.create<I>()

    protected val _state = MutableLiveData<S>()
    val states: LiveData<S>
        get() = _state

    protected val _sideEffect = MutableLiveData<SingleEvent<E>>()
    val sideEffect: LiveData<SingleEvent<E>>
        get() = _sideEffect

    fun addDisposable(disposables: Disposable) {
        baseDisposables.add(disposables)
    }

    fun execute(intentionToExecute: I) {
        baseIntentions.onNext(intentionToExecute)
    }

    fun bindIntent(intentionToExecute: Observable<I>) {
        intentionToExecute.subscribe(baseIntentions)
    }

    override fun onCleared() {
        baseDisposables.clear()
        baseIntentions.onComplete()
        super.onCleared()
    }
}