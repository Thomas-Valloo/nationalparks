package com.valloo.demo.nationalparks.utils

// https://developer.android.com/training/dependency-injection/hilt-testing?hl=fr#launchfragment
// https://github.com/android/architecture-samples/blob/views-hilt/app/src/androidTest/java/com/example/android/architecture/blueprints/todoapp/HiltExt.kt#L37

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.EmptyFragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.fragment.testing.manifest.R
import com.valloo.demo.nationalparks.HiltTestActivity

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int =R.style.FragmentScenarioEmptyFragmentActivityTheme,
    navHostController: NavHostController? = null,
    crossinline action: Fragment.() -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY, themeResId)

    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
            if (viewLifecycleOwner != null) {
                navHostController?.let {
                    Navigation.setViewNavController(fragment.requireView(), it)
                }
            }
        }
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
}
