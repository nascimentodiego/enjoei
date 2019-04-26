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

import android.net.Uri
import android.view.View
import android.widget.ImageView
import br.com.enjoei.app.BuildConfig
import br.com.enjoei.app.presentation.feature.home.HomeReducer
import br.com.enjoei.app.presentation.model.PhotoView
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun ImageView.loadImage(path: String) {
    Glide.with(this)
        .load(path)
        .into(this)
}

fun ImageView.loadImageFromAsset(picture: String) {
    Glide.with(this)
        .load(Uri.parse("file:///android_asset/$picture"))
        .into(this)
}

fun ImageView.loadUserPhoto(photo: PhotoView) {
    val url = BuildConfig.API_BASE_CLOUDINARY + MediaManager
        .get()
        .url()
        .transformation()
        .width(200)
        .height(200)
        .crop(photo.crop)
        .gravity(photo.gravity)
        .radius("max")
        .generate() + "/" + photo.id + ".png"



    Glide.with(this)
        .load(url)
        .into(this)
}

fun ImageView.loadProductPhoto(photo: PhotoView) {
    val url = BuildConfig.API_BASE_CLOUDINARY + MediaManager
        .get()
        .url()
        .transformation()
        .width(400)
        .height(400)
        .crop(photo.crop)
        .gravity(photo.gravity)
        .radius(5)
        .generate() + "/" + photo.id + ".png"



    Glide.with(this)
        .load(url)
        .into(this)
}


