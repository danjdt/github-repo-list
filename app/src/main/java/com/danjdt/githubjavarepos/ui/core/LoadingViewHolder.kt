package com.danjdt.githubjavarepos.ui.core

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.githubjavarepos.R

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // region Private Properties

    private val loadMoreView: ProgressBar = itemView.findViewById(R.id.loadMoreView)

    // endregion

    // region Public Methods

    fun bind(show: Boolean) {
        loadMoreView.visibility = if (show) View.VISIBLE else View.GONE
    }

    // endregion
}