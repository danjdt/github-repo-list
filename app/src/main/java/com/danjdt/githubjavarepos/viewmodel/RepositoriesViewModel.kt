package com.danjdt.githubjavarepos.viewmodel

import androidx.lifecycle.ViewModel
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class RepositoriesViewModel(private val interactor: FetchJavaRepositoriesInteractor) : ViewModel() {
}