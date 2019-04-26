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
package br.com.enjoei.app.presentation.feature.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.enjoei.app.R
import br.com.enjoei.app.presentation.extensions.*
import br.com.enjoei.app.presentation.feature.home.HomeReducer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates


class ProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    var items: List<HomeReducer.ProductItemView> by Delegates.observable(emptyList()) { _, old, new ->
        if (old != new) notifyDataSetChanged()
    }

    private val clickItem = PublishSubject.create<HomeReducer.ProductItemView>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_header_product, parent, false)
            ProductHeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_product, parent, false)
            ProductViewHolder(view)
        }

    }

    override fun getItemCount(): Int = items.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder && items.isNotEmpty()) {
            val item = items[position - 1]
            holder.itemView.setOnClickListener {
                clickItem.onNext(item)
            }
            holder.bind(item)
        } else if (holder is ProductHeaderViewHolder) {
            holder.bind()
        }
    }

    fun clickItem(): Observable<HomeReducer.ProductItemView> = clickItem.hide()

    override fun getItemViewType(position: Int): Int =
        if (isFirstPosition(position))
            TYPE_HEADER
        else TYPE_ITEM


    fun isFirstPosition(position: Int) = position == 0
}

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageViewProductPhoto: ImageView = itemView.findViewById(R.id.imageViewProductPhoto)
    private val imageViewUserPhoto: ImageView = itemView.findViewById(R.id.imageViewUserPhoto)
    private val textViewDiscount: TextView = itemView.findViewById(R.id.textViewDiscount)
    private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
    private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    private val texViewLikes: TextView = itemView.findViewById(R.id.texViewLikes)


    fun bind(item: HomeReducer.ProductItemView) {
        textViewTitle.text = item.title
        textViewPrice.text = item.price
        texViewLikes.text = item.likes
        if (item.discount.isNotEmpty())
            textViewDiscount.apply {
                text = item.discount
                makeVisible()
            } else {
            textViewDiscount.makeGone()
        }
        imageViewProductPhoto.loadProductPhoto(item.photos[0])
        imageViewUserPhoto.loadUserPhoto(item.avatar)
    }
}

class ProductHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageViewBanner: ImageView = itemView.findViewById(R.id.imageViewBanner)

    fun bind() {
        imageViewBanner.loadImageFromAsset("banner.jpg")
    }
}