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
package br.com.enjoei.app.presentation.widget

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SpacingItemDecoration @JvmOverloads constructor(
    @Px
    private val spacing: Int,
    private var displayMode: Int = INVALID
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        val manager = parent.layoutManager!!
        setSpacingForDirection(outRect, manager, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        layoutManager: RecyclerView.LayoutManager,
        position: Int,
        itemCount: Int
    ) {

        // Resolve display mode automatically
        if (displayMode == INVALID) {
            displayMode = resolveDisplayMode(layoutManager)
        }

        when (displayMode) {
            HORIZONTAL -> {
                with(outRect) {
                    left = if (position == 0) 0 else spacing
                    right = if (position == itemCount - 1) spacing else 0
                    top = 0
                    bottom = 0
                }
            }
            VERTICAL -> {
                with(outRect) {
                    left = 0
                    right = 0
                    top = if (position == 0) 0 else spacing
                    bottom = if (position == itemCount - 1) spacing else 0
                }
            }
            GRID -> if (layoutManager is GridLayoutManager) {
                val cols = layoutManager.spanCount
                val rows = itemCount / cols

                val spacingTop = if (position / cols == rows) 0 else spacing
                val spacingBottom = if (position / cols >= rows - 1) spacing else 0

                with(outRect) {
                    left = spacing
                    right = spacing //if (position % cols == cols - 1) spacing else 0
                    top = spacing //spacingTop
                    bottom = spacing //spacingBottom
                }
            }
        }
    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is GridLayoutManager) return GRID
        return if (layoutManager.canScrollHorizontally()) HORIZONTAL else VERTICAL
    }

    companion object {
        const val INVALID = -1
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val GRID = 2
    }
}