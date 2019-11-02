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

    private val repositories: ArrayList<Repository> = ArrayList()

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
        }
    }

    fun addItens(itens: List<Repository>) {
        repositories.addAll(itens)
        notifyDataSetChanged()
    }

    fun clear() {
        repositories.clear()
        notifyDataSetChanged()
    }
}
