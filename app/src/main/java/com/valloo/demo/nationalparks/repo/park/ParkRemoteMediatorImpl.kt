package com.valloo.demo.nationalparks.repo.park

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.valloo.demo.nationalparks.infra.PreferencesStore
import com.valloo.demo.nationalparks.infra.db.AppDatabase
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo
import com.valloo.demo.nationalparks.infra.http.ParksApi
import com.valloo.demo.nationalparks.infra.http.ParksApi.Companion.STARTING_INDEX
import com.valloo.demo.nationalparks.repo.RefreshStrategy
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * This mediator will query remote data when needed, depending on the [RefreshStrategy] chosen, and
 * the data already downloaded. Database ise used as cache.
 */
@OptIn(ExperimentalPagingApi::class)
class ParkRemoteMediatorImpl
@Inject
constructor(
    private val repository: ParkRepository,
    private val database: AppDatabase,
    private val parksApi: ParksApi,
    private val preferencesStore: PreferencesStore
) : ParkRemoteMediator() {

    override fun initializeSingle(): Single<InitializeAction> {
        val dataRefreshNeeded =
            when (val refreshStrategy = preferencesStore.getRefreshStrategy()) {
                RefreshStrategy.ALWAYS -> true
                RefreshStrategy.NEVER -> false
                else ->
                    System.currentTimeMillis() - preferencesStore.getParksLastUpdate() >
                        refreshStrategy.cacheTimeoutInMs
            }

        val initializeAction =
            if (dataRefreshNeeded) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        return Single.just(initializeAction)
    }

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, ParkListInfo>
    ): Single<MediatorResult> {
        val startingPosition =
            when (loadType) {
                LoadType.REFRESH -> {
                    // Data needs a refresh : parks are deleted (and alos associated entities with
                    // delete cascade)
                    database.parkDao().deleteAll().subscribeOn(Schedulers.io()).toSingle {
                        STARTING_INDEX
                    }
                }
                LoadType.PREPEND -> return Single.just(MediatorResult.Success(true))
                LoadType.APPEND -> {
                    database.parkDao().count()
                }
            }

        var endOfPaginationReached = true

        return startingPosition
            .flatMap { parksApi.parks(start = it, limit = state.config.pageSize) }
            .map { apiResponse ->
                endOfPaginationReached = apiResponse.start + apiResponse.limit >= apiResponse.total
                apiResponse.data
            }
            .flattenAsObservable { it }
            .flatMapCompletable { park -> repository.savePark(park) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toSingle {
                preferencesStore.setParksLastUpdate(System.currentTimeMillis())
                MediatorResult.Success(endOfPaginationReached)
            }
    }
}
