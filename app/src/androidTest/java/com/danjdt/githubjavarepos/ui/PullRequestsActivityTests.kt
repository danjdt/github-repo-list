package com.danjdt.githubjavarepos.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.danjdt.data.interactor.FetchPullRequestsInteractorMock
import com.danjdt.domain.DUMMY_REPOSITORY
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.di.networkModule
import com.danjdt.githubjavarepos.di.repositoriesModule
import com.danjdt.githubjavarepos.di.repositoryModule
import com.danjdt.githubjavarepos.mock.RouterMock
import com.danjdt.githubjavarepos.navigation.Router
import com.danjdt.githubjavarepos.ui.core.ErrorView
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestViewHolder
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsActivity
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsViewModel
import com.danjdt.githubjavarepos.utils.Spy
import com.danjdt.githubjavarepos.utils.clickChildViewWithId
import kotlinx.android.synthetic.main.activity_pull_requests.*
import kotlinx.android.synthetic.main.activity_repositories.errorView
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module


/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
@RunWith(AndroidJUnit4::class)
@FlowPreview
class PullRequestsActivityTests {

    // region Public Properties

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(PullRequestsActivity::class.java, true, false)

    // endregion

    // region Private Properties

    private var isOpenUrlCalled: Boolean = false

    private val interactor: FetchPullRequestsInteractorMock by lazy {
        FetchPullRequestsInteractorMock()
    }

    private val spy = object : Spy<Boolean> {
        override fun report(t: Boolean) {
            isOpenUrlCalled = true
        }
    }

    // enregion

    // region Public Methods

    @Before
    fun setup() {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        setupKoin(instrumentationContext)
    }

    @After
    fun tearDown() {
        stopKoin()
        isOpenUrlCalled = false
        activityRule.activity?.let {
            activityRule.finishActivity()
        }
    }

    @Test
    fun testViewStateOnFetchSuccess() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(VISIBLE, pullRequestRecyclerView.visibility)
            assertEquals(GONE, errorView.visibility)
            assertItemViewContent(pullRequestRecyclerView.layoutManager!!.getChildAt(0)!!)
        }
    }

    @Test
    fun testViewStateOnFetchFail() {
        interactor.exception = Exception()
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(GONE, pullRequestRecyclerView.visibility)
            assertEquals(VISIBLE, errorView.visibility)
            assertErrorViewContent(errorView)
        }
    }

    @Test
    fun testViewStateOnFetchEmptyList() {
        interactor.list = ArrayList()
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(GONE, pullRequestRecyclerView.visibility)
            assertEquals(VISIBLE, errorView.visibility)
            assertEmptyViewContent(errorView)
        }
    }

    @Test
    fun testViewStateOnFetchNextSuccess() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(31, pullRequestRecyclerView.adapter?.itemCount ?: 0)

            scrollToRecyclerViewLastItem(pullRequestRecyclerView)

            assertEquals(61, pullRequestRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testViewStateOnFetchNextFail() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(31, pullRequestRecyclerView.adapter?.itemCount ?: 0)

            interactor.exception = Exception()
            scrollToRecyclerViewLastItem(pullRequestRecyclerView)

            assertEquals(31, pullRequestRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testItemClickOpensUrl() {
        activityRule.launchActivity(Intent())
        performClickAtItem()
        assertTrue(isOpenUrlCalled)
    }

    // enregion

    // region Private Methods

    private fun performClickAtItem() {
        onView(withId(R.id.pullRequestRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PullRequestViewHolder>(
                0,
                clickChildViewWithId(R.id.containerView)
            )
        )
    }

    private fun assertItemViewContent(itemView: View) {
        val pullRequestTextView: TextView = itemView.findViewById(R.id.pullRequestTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)

        assertEquals("Title", pullRequestTextView.text)
        assertEquals("Lorem ipsum dolor", descriptionTextView.text)
        assertEquals("Username", usernameTextView.text)
    }

    private fun assertErrorViewContent(errorView: ErrorView) {
        val titleTextView: TextView = errorView.findViewById(R.id.titleTextView)
        val messageTextView: TextView = errorView.findViewById(R.id.messageTextView)
        assertEquals("Ops!", titleTextView.text)
        assertEquals(
            "Um erro inesperado aconteceu. Por favor tente novamente.",
            messageTextView.text
        )
    }

    private fun assertEmptyViewContent(errorView: ErrorView) {
        val titleTextView: TextView = errorView.findViewById(R.id.titleTextView)
        val messageTextView: TextView = errorView.findViewById(R.id.messageTextView)
        assertEquals("Ops!", titleTextView.text)
        assertEquals("Nenhum item encontrado.", messageTextView.text)
    }

    private fun scrollToRecyclerViewLastItem(recyclerView: RecyclerView) {
        onView(withId(R.id.pullRequestRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<PullRequestViewHolder>(
                recyclerView.adapter!!.itemCount - 1
            )
        )
    }

    private fun setupKoin(context: Context) {
        startKoin {
            androidContext(context)
        }

        try {
            loadKoinModules(listOf(repositoriesModule, networkModule, repositoryModule, module {
                viewModel {
                    PullRequestsViewModel(
                        interactor,
                        DUMMY_REPOSITORY
                    )
                }

                single<Router> { RouterMock(spy) }
            }))
        } catch (e: Exception) {
            throw e
        }
    }

    // endregion
}