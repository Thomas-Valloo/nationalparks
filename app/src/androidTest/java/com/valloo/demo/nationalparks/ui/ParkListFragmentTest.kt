package com.valloo.demo.nationalparks.ui

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import androidx.paging.testing.asPagingSourceFactory
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.di.RepositoryModule
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo
import com.valloo.demo.nationalparks.matchers.atPosition
import com.valloo.demo.nationalparks.repo.park.ParkRemoteMediator
import com.valloo.demo.nationalparks.repo.park.ParkRemoteMediatorImpl
import com.valloo.demo.nationalparks.repo.park.ParkRepository
import com.valloo.demo.nationalparks.repo.park.ParkRepositoryImpl
import com.valloo.demo.nationalparks.ui.park.list.ParkListFragment
import com.valloo.demo.nationalparks.utils.launchFragmentWithTestNavHostController
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.instancio.Instancio
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ParkListFragmentTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @BindValue lateinit var mockParkRepository: ParkRepository

    @BindValue lateinit var mockParkRemoteMediator: ParkRemoteMediator

    @OptIn(ExperimentalPagingApi::class)
    @Before
    fun setUp() {
        mockParkRepository = mockk<ParkRepositoryImpl>(relaxed = true)

        mockParkRemoteMediator = mockk<ParkRemoteMediatorImpl>(relaxed = true)
        every { mockParkRemoteMediator.loadSingle(any(), any()) } returns
            Single.just(RemoteMediator.MediatorResult.Success(endOfPaginationReached = false))
    }

    /** Test navigation to the events list. */
    @Test
    fun testNavigationToDetailFragment() {
        val parkListInfos = Instancio.createList(ParkListInfo::class.java)
        every { mockParkRepository.getPagedList() } returns
            parkListInfos.asPagingSourceFactory().invoke()

        val navController =
            launchFragmentWithTestNavHostController<ParkListFragment>(
                currentDestinationId = R.id.navigation_parkList
            )

        onView(withId(R.id.parksRecyclerView))
            .check(matches(atPosition(0, hasDescendant(withId(R.id.nameTextView)))))
            .perform(click())

        assert(navController.currentDestination?.id == R.id.navigation_parkDetail)
    }
}
