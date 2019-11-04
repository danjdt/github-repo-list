package com.danjdt.githubjavarepos.ui.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.Repository
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.ui.core.ItemClickListener
import com.danjdt.githubjavarepos.ui.core.LoadingViewHolder
import com.danjdt.githubjavarepos.utils.LIST_ITEM
import com.danjdt.githubjavarepos.utils.LOADING

/**
 * @autor danieljdt
 * @date 2019-11-02
 */
class RepositoryAdapter(val itemClickListener: ItemClickListener<Repository>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // region Private Properties

    private var repositories: List<Repository> = ArrayList()

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
                val view = layoutInflater.inflate(R.layout.view_repository_item, parent, false)
                RepositoryViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return repositories.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < repositories.size) LIST_ITEM else LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepositoryViewHolder) {
            holder.bind(repositories[position], itemClickListener)
        } else if (holder is LoadingViewHolder) {
            holder.bind(showLoading)
        }
    }

    fun addItens(itens: List<Repository>) {
        repositories = itens
        notifyDataSetChanged()
    }

    // endregion
}
