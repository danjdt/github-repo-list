package com.danjdt.githubjavarepos.ui.pullrequests

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.PullRequest
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.extensions.loadImageRounded
import com.danjdt.githubjavarepos.ui.core.ItemClickListener

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
class PullRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val containerView: View = itemView.findViewById(R.id.containerView)
    private val pullRequestTextView: TextView = itemView.findViewById(R.id.pullRequestTextView)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
    private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)

    fun bind(pullRequest: PullRequest, itemClickListener: ItemClickListener<PullRequest>) {
        with(pullRequest) {
            pullRequestTextView.text = title
            descriptionTextView.text = body
            usernameTextView.text = user.login
            userImageView.loadImageRounded(user.avatarUrl)
        }

        containerView.setOnClickListener {
            itemClickListener.onItemClicked(pullRequest)
        }
    }
}
