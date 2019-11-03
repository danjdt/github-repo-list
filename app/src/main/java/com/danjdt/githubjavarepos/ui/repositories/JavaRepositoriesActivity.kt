package com.danjdt.githubjavarepos.ui.repositories

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.danjdt.domain.entity.Repository
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.viewmodel.JavaRepositoriesViewModel
import kotlinx.android.synthetic.main.activity_repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
class JavaRepositoriesActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val repositoriesViewModel: JavaRepositoriesViewModel by viewModel()

    private val adapter: RepositoryAdapter by lazy {
        RepositoryAdapter()
    }

    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    private val scrollListener = object : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isLastItemVisible()) {
                fetchNextPage()
            }
        }
    }

    private val repositoriesObserver: Observer<List<Repository>> = Observer { repositories ->
        adapter.addItens(repositories)
    }

    private val isLoadingObserver: Observer<Boolean> = Observer { isLoading ->
        progressBar.visibility = if (isLoading) VISIBLE else GONE
        repositoriesRecyclerView.visibility = if (isLoading) GONE else VISIBLE
    }

    private val hasLoadMoreObserver: Observer<Boolean> = Observer { hasLoadMore ->
        adapter.showLoading = hasLoadMore
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
            repositoriesViewModel.fetchFirstPage()
        }
    }

    private fun fetchNextPage() {
        launch {
            repositoriesViewModel.fetchNextPage()
        }
    }

    private fun setupRecyclerView() {
        repositoriesRecyclerView.adapter = adapter
        repositoriesRecyclerView.layoutManager = linearLayoutManager
        repositoriesRecyclerView.addOnScrollListener(scrollListener)
    }

    private fun setupObservers() {
        repositoriesViewModel.repositories.observe(this, repositoriesObserver)
        repositoriesViewModel.isLoading.observe(this, isLoadingObserver)
        repositoriesViewModel.hasLoadMore.observe(this, hasLoadMoreObserver)
    }

    private fun isLastItemVisible(): Boolean {
        val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        val lastItemPosition = linearLayoutManager.itemCount - 1
        if (lastItemPosition > 0 && lastVisiblePosition == lastItemPosition) {
            return true
        }

        return false
    }
}