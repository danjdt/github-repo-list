package com.danjdt.githubjavarepos.ui.pullrequests

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.domain.entity.PullRequest
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.navigation.Router
import com.danjdt.githubjavarepos.ui.core.DividerItemDecoration
import com.danjdt.githubjavarepos.ui.core.ItemClickListener
import com.danjdt.githubjavarepos.utils.KEY_REPOSITORY
import kotlinx.android.synthetic.main.activity_pull_requests.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
class PullRequestsActivity : AppCompatActivity(), CoroutineScope, ItemClickListener<PullRequest> {

    // region Public Properties

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    // endregion

    // region Private Properties

    private val pullRequestsViewModel: PullRequestsViewModel by viewModel { parametersOf(repository) }

    private val router: Router by inject { parametersOf(this) }

    private val adapter: PullRequestAdapter by lazy {
        PullRequestAdapter(this)
    }

    private val repository by lazy {
        intent.getSerializableExtra(KEY_REPOSITORY)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

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

    // endregion

    // region Life Cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)

        setupRecyclerView()
        setupToolbar()
        setupListeners()
        setupObservers()
        fetchRepositories()
    }

    // endregion

    // region Public Methods

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemClicked(item: PullRequest) {
        router.openUrl(item.htmlUrl)
    }

    // endregion

    // region Private Methods

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

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.pull_requests)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        pullRequestRecyclerView.adapter = adapter
        pullRequestRecyclerView.layoutManager = linearLayoutManager
        pullRequestRecyclerView.addItemDecoration(DividerItemDecoration(this))
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

    // endregion
}
