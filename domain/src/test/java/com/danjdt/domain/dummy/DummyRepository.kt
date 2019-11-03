package com.danjdt.domain.dummy

import com.danjdt.domain.entity.Repository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
val DUMMY_REPOSITORIES = run {
    val list = mutableListOf<Repository>()
    for (i in 1..10) {
        val repository = Repository(
            id = 10,
            name = "Name",
            owner = DUMMY_USER,
            description = "Description",
            forks = 500,
            stargazersCount = 1000
        )

        list.add(repository)
    }
    list
}

@FlowPreview
fun dummyRepositoriesFlow(): Flow<List<Repository>> = flow {
    emit(DUMMY_REPOSITORIES)
}