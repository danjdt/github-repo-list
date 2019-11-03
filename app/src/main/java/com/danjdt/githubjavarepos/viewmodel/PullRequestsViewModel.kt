package com.danjdt.githubjavarepos.viewmodel

import androidx.lifecycle.ViewModel
import com.danjdt.domain.interactor.FetchPullRequestsInteractor

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
class PullRequestsViewModel(private val interactor: FetchPullRequestsInteractor) : ViewModel()
