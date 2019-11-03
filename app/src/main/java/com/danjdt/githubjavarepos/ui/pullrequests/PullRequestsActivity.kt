package com.danjdt.githubjavarepos.ui.pullrequests

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.PullRequest
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.ui.core.ItemClickListener
import com.danjdt.githubjavarepos.utils.KEY_REPOSITORY
import com.danjdt.githubjavarepos.viewmodel.PullRequestsViewModel
import kotlinx.android.synthetic.main.activity_pull_requests.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
class PullRequestsActivity : AppCompatActivity(), CoroutineScope, ItemClickListener<PullRequest> {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val pullRequestsViewModel: PullRequestsViewModel by viewModel() { parametersOf(repository)}

    private val adapter: PullRequestAdapter by lazy {
        PullRequestAdapter(this)
    }

    private val repository by lazy {
        intent.getSerializableExtra(KEY_REPOSITORY)
    }

    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isLastItemVisible()) {
                fetchNextPage()
            }
        }
    }

    private val pullRequestObserver: Observer<List<PullRequest>> = Observer { pullRequests ->
        pullRequests?.let {
            adapter.addItens(pullRequests)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        fetchRepositories()
    }

    override fun onItemClicked(item: PullRequest) {
        //TODO OPEN URL
    }

    private fun fetchRepositories() {
        launch {
            pullRequestsViewModel.fetchFirstPage()
        }
    }

    private fun fetchNextPage() {
        launch {
            pullRequestsViewModel.fetchNextPage()
        }
    }

    private fun refresh() {
        launch {
            pullRequestsViewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        pullRequestRecyclerView.adapter = adapter
        pullRequestRecyclerView.layoutManager = linearLayoutManager
    }

    private fun setupListeners() {
        pullRequestRecyclerView.addOnScrollListener(scrollListener)

        swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setupObservers() {
        pullRequestsViewModel.pullRequests.observe(this, pullRequestObserver)
        pullRequestsViewModel.isLoading.observe(this, isLoadingObserver)
        pullRequestsViewModel.hasLoadMore.observe(this, hasLoadMoreObserver)
        pullRequestsViewModel.exception.observe(this, Observer { exception ->
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
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
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
        if (progressBar.visibility == View.VISIBLE || errorView.visibility == View.VISIBLE) {
            pullRequestRecyclerView.visibility = View.GONE
        } else {
            pullRequestRecyclerView.visibility = View.VISIBLE
        }
    }
}
