package com.danjdt.githubjavarepos.ui.repositories

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.Repository
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.extensions.decimalFormat
import com.danjdt.githubjavarepos.extensions.loadImageRounded

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val repositoryTextView: TextView = itemView.findViewById(R.id.repositoryTextView)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
    private val forksTextView: TextView = itemView.findViewById(R.id.forksTextView)
    private val starsTextView: TextView = itemView.findViewById(R.id.starsTextView)
    private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)

    fun bind(repository: Repository) {
        with(repository) {
            repositoryTextView.text = name
            descriptionTextView.text = description
            usernameTextView.text = owner.login
            forksTextView.text = forks.decimalFormat()
            starsTextView.text = stargazersCount.decimalFormat()
            userImageView.loadImageRounded(owner.avatarUrl)
        }
    }
}