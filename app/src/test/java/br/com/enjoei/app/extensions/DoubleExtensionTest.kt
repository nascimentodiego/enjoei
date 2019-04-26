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
package br.com.enjoei.app.extensions

import br.com.enjoei.app.presentation.extensions.asBRL
import org.junit.Assert
import org.junit.Test


class DoubleExtensionTest {

    @Test
    fun shouldDisplayMinusWhenValueIsNegative() {
        val result = (-1.0).asBRL()

        Assert.assertEquals("-1", result)
    }

    @Test
    fun shouldDisplayTheCorrectFormatOfPtBrCurrencyWhenValueIsDecimal() {
        val result = 22.90.asBRL()

        Assert.assertEquals("23", result)
    }

    @Test
    fun shouldDisplayTheCorrectFormatOfPtBrCurrencyWhenIsThousandsValue() {
        val result = 2450.00.asBRL()

        Assert.assertEquals("2.450", result)
    }

    @Test
    fun shouldDisplayBRLCurrencyWhenRequested() {
        val result = 2450.00.asBRL(true)
        Assert.assertEquals("R$ 2.450", result)
    }
}