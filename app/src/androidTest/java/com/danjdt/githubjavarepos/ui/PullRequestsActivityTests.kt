package com.danjdt.githubjavarepos.ui

import android.app.Activity
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
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.di.networkModule
import com.danjdt.githubjavarepos.di.repositoriesModule
import com.danjdt.githubjavarepos.di.repositoryModule
import com.danjdt.githubjavarepos.mock.dummy.DUMMY_REPOSITORY
import com.danjdt.githubjavarepos.mock.interactor.FetchPullRequestsInteractorMock
import com.danjdt.githubjavarepos.ui.core.ErrorView
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestViewHolder
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsActivity
import com.danjdt.githubjavarepos.utils.clickChildViewWithId
import com.danjdt.githubjavarepos.viewmodel.PullRequestsViewModel
import kotlinx.android.synthetic.main.activity_pull_requests.*
import kotlinx.android.synthetic.main.activity_repositories.errorView
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(PullRequestsActivity::class.java, true, false)

    private val interactor: FetchPullRequestsInteractorMock by lazy {
        FetchPullRequestsInteractorMock()
    }

    @Before
    fun setup() {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        setupKoin(instrumentationContext)
    }

    @After
    fun tearDown() {
        stopKoin()
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
            assertEquals(11, pullRequestRecyclerView.adapter?.itemCount ?: 0)

            scrollToRecyclerViewLastItem(pullRequestRecyclerView)

            assertEquals(21, pullRequestRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testViewStateOnFetchNextFail() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(11, pullRequestRecyclerView.adapter?.itemCount ?: 0)

            interactor.exception = Exception()
            scrollToRecyclerViewLastItem(pullRequestRecyclerView)

            assertEquals(11, pullRequestRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testItemClickOpensPullRequestActivity() {
        activityRule.launchActivity(Intent())

        performClickAtItem()

        val activity = getCurrentActivity()

        assertNull(activity)
    }

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
        assertEquals("Ops!", errorView.titleTextView.text)
        assertEquals(
            "Um erro inesperado aconteceu. Por favor tente novamente.",
            errorView.messageTextView.text
        )
    }

    private fun assertEmptyViewContent(errorView: ErrorView) {
        assertEquals("Ops!", errorView.titleTextView.text)
        assertEquals("Nenhum item encontrado.", errorView.messageTextView.text)
    }

    private fun scrollToRecyclerViewLastItem(recyclerView: RecyclerView) {
        onView(withId(R.id.pullRequestRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<PullRequestViewHolder>(
                recyclerView.adapter!!.itemCount - 1
            )
        )
    }

    private fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                .elementAtOrNull(0)
        }
        }
        return currentActivity
    }

    private fun setupKoin(context: Context) {
        startKoin {
            androidContext(context)
        }

        try {
            loadKoinModules(listOf(repositoriesModule, networkModule, repositoryModule, module {
                viewModel {
                    PullRequestsViewModel(interactor, DUMMY_REPOSITORY)
                }
            }))
        } catch (e: Exception) {
        }
    }
}