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
import com.danjdt.githubjavarepos.di.pullRequestsModule
import com.danjdt.githubjavarepos.di.repositoryModule
import com.danjdt.githubjavarepos.mock.interactor.FetchJavaRepositoriesInteractorMock
import com.danjdt.githubjavarepos.ui.core.ErrorView
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsActivity
import com.danjdt.githubjavarepos.ui.repositories.JavaRepositoriesActivity
import com.danjdt.githubjavarepos.ui.repositories.RepositoryViewHolder
import com.danjdt.githubjavarepos.utils.clickChildViewWithId
import com.danjdt.githubjavarepos.viewmodel.JavaRepositoriesViewModel
import kotlinx.android.synthetic.main.activity_repositories.*
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
class JavaRepositoriesActivityTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(JavaRepositoriesActivity::class.java, true, false)

    private val interactor: FetchJavaRepositoriesInteractorMock by lazy {
        FetchJavaRepositoriesInteractorMock()
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
            assertEquals(VISIBLE, repositoriesRecyclerView.visibility)
            assertEquals(GONE, errorView.visibility)
            assertItemViewContent(repositoriesRecyclerView.layoutManager!!.getChildAt(0)!!)
        }
    }

    @Test
    fun testViewStateOnFetchFail() {
        interactor.exception = Exception()
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(GONE, repositoriesRecyclerView.visibility)
            assertEquals(VISIBLE, errorView.visibility)
            assertErrorViewContent(errorView)
        }
    }

    @Test
    fun testViewStateOnFetchEmptyList() {
        interactor.list = ArrayList()
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(GONE, repositoriesRecyclerView.visibility)
            assertEquals(VISIBLE, errorView.visibility)
            assertEmptyViewContent(errorView)
        }
    }

    @Test
    fun testViewStateOnFetchNextSuccess() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(11, repositoriesRecyclerView.adapter?.itemCount ?: 0)

            scrollToRecyclerViewLastItem(repositoriesRecyclerView)

            assertEquals(21, repositoriesRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testViewStateOnFetchNextFail() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(11, repositoriesRecyclerView.adapter?.itemCount ?: 0)

            interactor.exception = Exception()
            scrollToRecyclerViewLastItem(repositoriesRecyclerView)

            assertEquals(11, repositoriesRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    private fun performClickAtItem() {
        onView(withId(R.id.repositoriesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RepositoryViewHolder>(
                0,
                clickChildViewWithId(R.id.containerView)
            )
        )
    }

    private fun assertItemViewContent(itemView: View) {
        val repositoryTextView: TextView = itemView.findViewById(R.id.repositoryTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val forksTextView: TextView = itemView.findViewById(R.id.forksTextView)
        val starsTextView: TextView = itemView.findViewById(R.id.starsTextView)

        assertEquals("Name", repositoryTextView.text)
        assertEquals("Description", descriptionTextView.text)
        assertEquals("Username", usernameTextView.text)
        assertEquals("500", forksTextView.text)
        assertEquals("1.000", starsTextView.text)
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
        onView(withId(R.id.repositoriesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RepositoryViewHolder>(
                recyclerView.adapter!!.itemCount - 1
            )
        )
    }

    private fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync {
            run {
                currentActivity =
                    ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED)
                        .elementAtOrNull(0)
            }
        }
        return currentActivity
    }

    private fun setupKoin(context: Context) {
        try {
            startKoin {
                androidContext(context)
            }

            loadKoinModules(listOf(pullRequestsModule, networkModule, repositoryModule, module {
                viewModel {
                    JavaRepositoriesViewModel(interactor)
                }
            }))
        } catch (e: Exception) {
        }
    }
}