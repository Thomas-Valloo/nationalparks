package com.valloo.demo.nationalparks.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.valloo.demo.nationalparks.work.DownloadParksWorker.Companion.DOWNLOAD_PARKS_WORKER_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    workManager: WorkManager,
) : ViewModel() {

    val progress: LiveData<WorkInfo?> =
        workManager.getWorkInfosByTagLiveData(DOWNLOAD_PARKS_WORKER_TAG).map {
            it.lastOrNull()
        }
}