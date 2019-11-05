package com.danjdt.githubjavarepos.mock

import com.danjdt.domain.entity.Repository
import com.danjdt.githubjavarepos.navigation.Router
import com.danjdt.githubjavarepos.utils.Spy

/**
 * @autor danieljdt
 * @date 2019-11-04
 */
class RouterMock(private val spy : Spy<Boolean>) : Router {

    override fun openPullRequests(repository: Repository) {
        spy.report(true)
    }

    override fun openUrl(url: String) {
        spy.report(true)
    }
}