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
package br.com.enjoei.app.presentation.feature.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import br.com.enjoei.app.R
import br.com.enjoei.app.presentation.extensions.loadProductPhoto
import br.com.enjoei.app.presentation.model.PhotoView


class PhotoAdapter(private val photos: List<PhotoView>, private val context: Context) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int = photos.size

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val photo = photos[position]

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.item_photo_detail_product, collection, false) as ViewGroup
        val imageView = layout.findViewById(R.id.imageViewPhoto) as ImageView

        imageView.loadProductPhoto(photo)

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }
}