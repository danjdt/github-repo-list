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
import com.danjdt.data.interactor.FetchJavaRepositoriesInteractorMock
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.di.networkModule
import com.danjdt.githubjavarepos.di.pullRequestsModule
import com.danjdt.githubjavarepos.di.repositoryModule
import com.danjdt.githubjavarepos.mock.RouterMock
import com.danjdt.githubjavarepos.navigation.Router
import com.danjdt.githubjavarepos.ui.core.ErrorView
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestViewHolder
import com.danjdt.githubjavarepos.ui.repositories.JavaRepositoriesActivity
import com.danjdt.githubjavarepos.ui.repositories.JavaRepositoriesViewModel
import com.danjdt.githubjavarepos.ui.repositories.RepositoryViewHolder
import com.danjdt.githubjavarepos.utils.Spy
import com.danjdt.githubjavarepos.utils.clickChildViewWithId
import kotlinx.android.synthetic.main.activity_repositories.*
import kotlinx.coroutines.FlowPreview
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    // region Public Properties

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(JavaRepositoriesActivity::class.java, true, false)

    // endregion

    // region Private Properties

    private var isOpenPullRequestCalled: Boolean = false

    private val interactor: FetchJavaRepositoriesInteractorMock by lazy {
        FetchJavaRepositoriesInteractorMock()
    }

    private val spy = object : Spy<Boolean> {
        override fun report(t: Boolean) {
            isOpenPullRequestCalled = true
        }
    }

    // endregion

    // region Public Methods

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        setupKoin(context)
    }

    @After
    fun tearDown() {
        stopKoin()
        isOpenPullRequestCalled = false
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
            assertEquals(31, repositoriesRecyclerView.adapter?.itemCount ?: 0)

            scrollToRecyclerViewLastItem(repositoriesRecyclerView)

            assertEquals(61, repositoriesRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testViewStateOnFetchNextFail() {
        activityRule.launchActivity(Intent())
        with(activityRule.activity) {
            assertEquals(31, repositoriesRecyclerView.adapter?.itemCount ?: 0)

            interactor.exception = Exception()
            scrollToRecyclerViewLastItem(repositoriesRecyclerView)

            assertEquals(31, repositoriesRecyclerView.adapter?.itemCount ?: 0)
        }
    }

    @Test
    fun testItemClickOpensPullRequests() {
        activityRule.launchActivity(Intent())
        performClickAtItem()
        assertTrue(isOpenPullRequestCalled)
    }

    // endregion

    // region Private Methods

    private fun performClickAtItem() {
        onView(withId(R.id.repositoriesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PullRequestViewHolder>(
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
        onView(withId(R.id.repositoriesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RepositoryViewHolder>(
                recyclerView.adapter!!.itemCount - 1
            )
        )
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

                single<Router> { RouterMock(spy) }
            }))
        } catch (e: Exception) {
            throw e
        }
    }

    // endregion
}