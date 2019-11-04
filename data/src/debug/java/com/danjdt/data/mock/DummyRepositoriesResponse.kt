package com.danjdt.data.mock

import com.danjdt.data.network.response.RepositoriesReponse
import com.danjdt.domain.DUMMY_REPOSITORIES

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
val DUMMY_REPOSITORIES_RESPONSE =
    RepositoriesReponse(items = DUMMY_REPOSITORIES, incompleteResults = false, totalCounts = 10)