package com.danjdt.githubjavarepos.ui.repositories

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.viewmodel.JavaRepositoriesViewModel
import kotlinx.android.synthetic.main.activity_repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
class JavaRepositoriesActivity : AppCompatActivity(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val repositoriesViewModel: JavaRepositoriesViewModel by viewModel()

    private val adapter: RepositoryAdapter by lazy {
        RepositoryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        setupRecyclerView()
        setupObservers()
        fetchRepositories()
    }

    private fun fetchRepositories() {
        launch {
            repositoriesViewModel.fetchJavaRepositories()
        }
    }

    private fun setupRecyclerView() {
        repositoriesRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        repositoriesViewModel.repositories.observe(this, Observer { repositories ->
            adapter.addItens(repositories)
        })
    }
}