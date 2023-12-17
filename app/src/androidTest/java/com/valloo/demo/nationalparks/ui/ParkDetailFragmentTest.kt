package com.valloo.demo.nationalparks.ui

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.di.RepositoryModule
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.repo.park.ParkRemoteMediator
import com.valloo.demo.nationalparks.repo.park.ParkRepository
import com.valloo.demo.nationalparks.repo.park.ParkRepositoryImpl
import com.valloo.demo.nationalparks.ui.park.detail.ParkDetailFragment
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
class ParkDetailFragmentTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @BindValue lateinit var mockParkRepository: ParkRepository

    @BindValue lateinit var mockParkRemoteMediator: ParkRemoteMediator

    /** Park displayed in fragment. */
    private lateinit var park: ParkEntity

    @Before
    fun setUp() {
        park = Instancio.create(ParkEntity::class.java)

        mockParkRepository = mockk<ParkRepositoryImpl>()
        every { mockParkRepository.getParkEntity(any()) } returns Single.just(park)

        val image = Instancio.create(ImageDataEntity::class.java).copy(url = VALID_IMAGE_URL)
        every { mockParkRepository.getParkImages(any()) } returns Single.just(listOf(image))

        every { mockParkRepository.getParkActivities(any()) } returns
            Single.just(Instancio.createList(ParkActivityEntity::class.java))
    }

    /** Test that the data from the park are displayed as intended. */
    @Test
    fun checkDisplayedData() {
        val args = Bundle().apply { putString("id", "parkId") }
        launchFragmentWithTestNavHostController<ParkDetailFragment>(
            currentDestinationId = R.id.navigation_parkDetail,
            args = args
        )

        onView(withId(R.id.descriptionTextView)).check(matches(withText(park.description)))

        onView(withId(R.id.designationTextView)).check(matches(withText(park.designation)))

        onView(withId(R.id.weatherTextView)).check(matches(withText(park.weatherInfo)))
    }

    companion object {
        const val VALID_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThYwIHKSxn00VWE9T9AfBZz5fcm9Agh1ZpnVJuYUFg4g&s"
    }
}
