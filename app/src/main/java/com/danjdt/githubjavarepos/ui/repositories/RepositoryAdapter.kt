package com.danjdt.githubjavarepos.ui.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.Repository
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.ui.core.LoadingViewHolder

/**
 * @autor danieljdt
 * @date 2019-11-02
 */
class RepositoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_REPOSITORY = 1
    private val LOADING = 2

    private var repositories: List<Repository> = ArrayList()

    var showLoading: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        return if (position < repositories.size) ITEM_REPOSITORY else LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepositoryViewHolder) {
            holder.bind(repositories[position])
        } else if (holder is LoadingViewHolder) {
            holder.bind(showLoading)
        }
    }

    fun addItens(itens: List<Repository>) {
        repositories = itens
        notifyDataSetChanged()
    }

    fun clear() {
        repositories = ArrayList()
        notifyDataSetChanged()
    }
}
