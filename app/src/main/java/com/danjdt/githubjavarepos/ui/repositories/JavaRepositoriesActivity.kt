package com.danjdt.githubjavarepos.ui.repositories

import android.content.Intent
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
import com.danjdt.githubjavarepos.ui.core.ItemClickListener
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsActivity
import com.danjdt.githubjavarepos.utils.KEY_REPOSITORY
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
class JavaRepositoriesActivity : AppCompatActivity(), CoroutineScope,
    ItemClickListener<Repository> {

    // region Public Properties

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    // endregion

    // region Private Properties

    private val repositoriesViewModel: JavaRepositoriesViewModel by viewModel()

    private val adapter: RepositoryAdapter by lazy {
        RepositoryAdapter(this)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val scrollListener = object : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isLastItemVisible()) {
                fetchNextPage()
            }
        }
    }

    private val repositoriesObserver: Observer<List<Repository>> = Observer { repositories ->
        repositories?.let {
            adapter.addItens(repositories)
            displayContent()
        }
    }

    private val isLoadingObserver: Observer<Boolean> = Observer { isLoading ->
        if (swipeRefreshLayout.isRefreshing) {
            displaySwipeRefresh(isLoading)
        } else {
            displayProgressBar(isLoading)
        }
    }

    private val hasLoadMoreObserver: Observer<Boolean> = Observer { hasLoadMore ->
        adapter.showLoading = hasLoadMore
    }

    // endregion

    // region Life Cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        fetchRepositories()
    }

    // endregion

    // region Public Methods

    override fun onItemClicked(item: Repository) {
        openPullRequests(item)
    }

    // endregion

    // region Private Methods

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

    private fun refresh() {
        launch {
            repositoriesViewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        repositoriesRecyclerView.adapter = adapter
        repositoriesRecyclerView.layoutManager = linearLayoutManager
    }

    private fun setupListeners() {
        repositoriesRecyclerView.addOnScrollListener(scrollListener)

        swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setupObservers() {
        repositoriesViewModel.repositories.observe(this, repositoriesObserver)
        repositoriesViewModel.isLoading.observe(this, isLoadingObserver)
        repositoriesViewModel.hasLoadMore.observe(this, hasLoadMoreObserver)
        repositoriesViewModel.exception.observe(this, Observer { exception ->
            displayError(exception)
        })
    }

    private fun isLastItemVisible(): Boolean {
        val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        val lastItemPosition = linearLayoutManager.itemCount - 1
        if (lastItemPosition > 0 && lastVisiblePosition == lastItemPosition) {
            return true
        }

        return false
    }

    private fun displaySwipeRefresh(isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = VISIBLE
        } else {
            progressBar.visibility = GONE
        }

        updateContentVisibility()
    }

    private fun displayError(e: Exception?) {
        errorView.displayError(e)
        updateContentVisibility()
    }

    private fun displayContent() {
        displayError(null)
        updateContentVisibility()
    }

    private fun updateContentVisibility() {
        if (progressBar.visibility == VISIBLE || errorView.visibility == VISIBLE) {
            repositoriesRecyclerView.visibility = GONE
        } else {
            repositoriesRecyclerView.visibility = VISIBLE
        }
    }

    private fun openPullRequests(repository: Repository) {
        val intent = Intent(this, PullRequestsActivity::class.java)
        intent.putExtra(KEY_REPOSITORY, repository)
        startActivity(intent)
    }

    // endregion
}