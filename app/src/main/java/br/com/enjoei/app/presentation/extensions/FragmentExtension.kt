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
package br.com.enjoei.app.presentation.extensions

import android.os.Build
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun Fragment.setupToolbar(title: String, toolbar: Toolbar, block: (Toolbar) -> Unit = {}) {
    (activity as androidx.appcompat.app.AppCompatActivity).setSupportActionBar(toolbar)
    (activity as androidx.appcompat.app.AppCompatActivity).title = title
    block(toolbar)
}

fun Fragment.setupStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        activity?.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = color
        }
    }
}

inline fun FragmentManager.runTransaction(block: FragmentTransaction.() -> Unit) {
    with(beginTransaction()) {
        block()
        commit()
    }
}

fun Fragment.hideStatusBar() {
    activity?.window?.let { window ->
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}
