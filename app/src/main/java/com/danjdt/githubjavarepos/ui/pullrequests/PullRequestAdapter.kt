package com.danjdt.githubjavarepos.ui.pullrequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.PullRequest
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.ui.core.ItemClickListener
import com.danjdt.githubjavarepos.ui.core.LoadingViewHolder
import com.danjdt.githubjavarepos.utils.LIST_ITEM
import com.danjdt.githubjavarepos.utils.LOADING

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
class PullRequestAdapter(private val itemClickListener: ItemClickListener<PullRequest>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // region Private Properties

    private var pullRequests: List<PullRequest> = ArrayList()

    var showLoading: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // endregion

    // region Public Methods

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            LOADING -> {
                val view = layoutInflater.inflate(R.layout.view_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.view_pull_request_item, parent, false)
                PullRequestViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return pullRequests.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < pullRequests.size) LIST_ITEM else LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PullRequestViewHolder) {
            holder.bind(pullRequests[position], itemClickListener)
        } else if (holder is LoadingViewHolder) {
            holder.bind(showLoading)
        }
    }

    fun addItens(itens: List<PullRequest>) {
        pullRequests = itens
        notifyDataSetChanged()
    }

    // endregion
}
