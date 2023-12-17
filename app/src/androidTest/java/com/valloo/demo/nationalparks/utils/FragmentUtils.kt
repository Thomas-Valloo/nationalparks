package com.valloo.demo.nationalparks.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.valloo.demo.nationalparks.R

/**
 * This should be used when testing navigation or a single fragment : it uses [TestNavHostController], so `navigate()`
 * won't be called. Only the state of the controller will be updated.
 *
 * @param currentDestinationId position the controller to the correct destination for the Fragment.
 * @param args arguments to pass to the current destination for setup.
 */
inline fun <reified T : Fragment> launchFragmentWithTestNavHostController(
    @IdRes currentDestinationId: Int? = null,
    args: Bundle = Bundle()
): TestNavHostController {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    UiThreadStatement.runOnUiThread {
        navController.setGraph(R.navigation.mobile_navigation)
        currentDestinationId?.let { navController.setCurrentDestination(it, args) }
    }
    launchFragmentInHiltContainer<T>(fragmentArgs = args, navHostController = navController)
    return navController
}
