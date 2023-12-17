package com.valloo.demo.nationalparks.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import androidx.work.workDataOf
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.infra.PreferencesStore
import com.valloo.demo.nationalparks.infra.http.ParksApi
import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkJsonDto
import com.valloo.demo.nationalparks.repo.park.ParkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/** Worker used to download parks data and insert them in database. */
@HiltWorker
class DownloadParksWorker
@AssistedInject
constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val parkRepository: ParkRepository,
    private val parksApi: ParksApi,
    private val preferencesStore: PreferencesStore
) : RxWorker(appContext, workerParams) {

    /** Number of parks downloaded and inserted in database. */
    private var parksCurrentCount = 0
    private var parksTotalCount = 0

    override fun createWork(): Single<Result> {
        return setForeground(createForegroundInfo(0, 100)).andThen(downloadParks())
    }

    private fun downloadParks(): Single<Result> {
        return parksApi
            .parks(limit = PARKS_LIMIT_REQUEST)
            .map { apiResponse -> apiResponse.data }
            .flatMapCompletable(::insertParks)
            .andThen(Single.just(Result.success()))
    }

    private fun insertParks(parks: List<ParkJsonDto>): Completable {
        // Add a delay to allow user to see the download mechanics, otherwise would be to fast.
        val addDelay = preferencesStore.getAddDelayToDebug()
        val delayInMs = if (addDelay) 10L else 0L

        return Observable.fromIterable(parks)
            .flatMapCompletable { item ->
                parkRepository
                    .savePark(item)
                    .andThen(updateProgress())
                    .delay(delayInMs, TimeUnit.MILLISECONDS)
            }
            .andThen(
                Completable.fromAction {
                    preferencesStore.setParksLastUpdate(System.currentTimeMillis())
                }
            )
    }

    private fun updateProgress(): Completable {
        parksCurrentCount++

        val workData =
            workDataOf(
                WORK_DATA_KEY_PROGRESS to parksCurrentCount,
                WORK_DATA_KEY_MAX_PROGRESS to parksTotalCount
            )

        return setForeground(createForegroundInfo(parksCurrentCount, parksTotalCount))
            .andThen(setCompletableProgress(workData))
    }

    private fun createForegroundInfo(progress: Int, maxProgress: Int): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val id = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_title)
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, id)
                .setContentTitle(title)
                .setTicker(title)
                .setChannelId(CHANNEL_ID)
                .setProgress(maxProgress, progress, false)
                .setSmallIcon(R.drawable.downloading)
                .setOngoing(true)
                .setOnlyAlertOnce(true)

        val contentText =
            if (progress == maxProgress) {
                applicationContext.getString(R.string.notif_download_complete)
            } else {
                applicationContext.getString(
                    R.string.notif_downloading,
                    progress * 100 / maxProgress
                )
            }
        notificationBuilder.setContentText(contentText)

        return ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = applicationContext.getString(R.string.channel_name)
        val description = applicationContext.getString(R.string.channel_description)
        val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW)
        channel.description = description

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val DOWNLOAD_PARKS_WORKER_TAG = "DownloadParksWorkerTAG"

        const val WORK_DATA_KEY_PROGRESS = "progress"
        const val WORK_DATA_KEY_MAX_PROGRESS = "max_progress"

        /** Limit greater than the number of parks to retrieve them all with one request. */
        const val PARKS_LIMIT_REQUEST = 500

        const val CHANNEL_ID = "CHANNEL_ID_DOWNLOAD"
        const val NOTIFICATION_ID = 1
    }
}
