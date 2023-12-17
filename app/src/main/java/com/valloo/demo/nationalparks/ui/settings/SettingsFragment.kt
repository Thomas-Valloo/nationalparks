package com.valloo.demo.nationalparks.ui.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.infra.ConnectivityUtils
import com.valloo.demo.nationalparks.infra.PreferencesStoreImpl.Companion.KEY_REFRESH_STRATEGY
import com.valloo.demo.nationalparks.infra.PreferencesStoreImpl.Companion.KEY_SYNCHRONIZE_SERVER
import com.valloo.demo.nationalparks.work.DownloadParksWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val refreshStrategyPreference = findPreference<ListPreference>(KEY_REFRESH_STRATEGY)
        refreshStrategyPreference?.summaryProvider =
            Preference.SummaryProvider<ListPreference> { preference ->
                preference.entry
            }


        val synchronizeServerPreference = findPreference<Preference>(
            KEY_SYNCHRONIZE_SERVER
        )
        synchronizeServerPreference?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { _ ->
                if (!ConnectivityUtils.isInternetAvailable(requireContext())) {
                    Snackbar.make(
                        requireView(),
                        R.string.pref_synchronizeServer_when_online,
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }

                enqueueDownloadWorker()
                true
            }
    }


    private fun enqueueDownloadWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<DownloadParksWorker>()
            .setConstraints(constraints)
            .addTag(DownloadParksWorker.DOWNLOAD_PARKS_WORKER_TAG)
            .build()
        workManager.enqueueUniqueWork(DOWNLOAD_WORK_NAME, ExistingWorkPolicy.KEEP, request)
    }

    companion object {
        const val DOWNLOAD_WORK_NAME = "downloadWorkName"
    }
}
