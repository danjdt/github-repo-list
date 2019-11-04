package com.danjdt.githubjavarepos.ui.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.githubjavarepos.R


/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class DividerItemDecoration(context: Context, resId: Int = R.drawable.drawable_divider_inset) :
    RecyclerView.ItemDecoration() {

    private val divider: Drawable
    private val defaultDivider: Drawable =
        ContextCompat.getDrawable(context, R.drawable.drawable_divider_full)!!

    init {
        divider = ContextCompat.getDrawable(context, resId) ?: defaultDivider
    }

    override fun getItemOffsets(rect: Rect, v: View, parent: RecyclerView, s: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(v)
        if (pos < parent.layoutManager?.itemCount!! - 2) {
            rect.bottom = divider.intrinsicHeight
        } else if (pos == parent.layoutManager?.itemCount!! - 2) {
            rect.bottom = defaultDivider.intrinsicHeight
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (pos < parent.layoutManager?.itemCount!! - 2) {
                drawDivider(divider, child, parent, canvas)
            } else if (pos == parent.layoutManager?.itemCount!! - 2) {
                drawDivider(defaultDivider, child, parent, canvas)
            }
        }
    }

    private fun drawDivider(defaultDivider: Drawable, child: View, parent: RecyclerView, canvas: Canvas) {
        val params = child.layoutParams as RecyclerView.LayoutParams
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val dividerTop = child.bottom + params.bottomMargin
        val dividerBottom = dividerTop + defaultDivider.intrinsicHeight
        defaultDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
        defaultDivider.draw(canvas)
    }
}