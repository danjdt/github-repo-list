package com.danjdt.githubjavarepos.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.danjdt.domain.entity.Repository
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsActivity
import com.danjdt.githubjavarepos.utils.KEY_REPOSITORY

/**
 * @autor danieljdt
 * @date 2019-11-04
 */
class RouterImpl(val context: Context) : Router {

    // region Public Methods

    override fun openPullRequests(repository: Repository) {
        val intent = Intent(context, PullRequestsActivity::class.java)
        intent.putExtra(KEY_REPOSITORY, repository)
        context.startActivity(intent)
    }

    override fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    // endregion
}
