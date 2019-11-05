package com.danjdt.githubjavarepos.navigation

import com.danjdt.domain.entity.Repository

/**
 *  @autor danieljdt
 *  @date 2019-11-04
 **/
interface Router {

    fun openPullRequests(repository: Repository)

    fun openUrl(url: String)
}