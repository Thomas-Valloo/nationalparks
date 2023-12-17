package com.valloo.demo.nationalparks.ui.park.list

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.flowable
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo
import com.valloo.demo.nationalparks.repo.park.ParkRemoteMediator
import com.valloo.demo.nationalparks.repo.park.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class ParkListViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var parkRepository: ParkRepository

    @Inject
    lateinit var mediator: ParkRemoteMediator

    fun getParkList(): Flowable<PagingData<ParkListItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = mediator,
            pagingSourceFactory = {
                parkRepository.getPagedList()
            }
        ).flowable
            .map { pagingData ->
                pagingData.map(::toParkListItem)
            }
    }

    private fun toParkListItem(parkListInfo: ParkListInfo): ParkListItem {
        return ParkListItem(
            id = parkListInfo.park.parkId,
            parkCode = parkListInfo.park.parkCode,
            name = parkListInfo.park.name,
            designation = parkListInfo.park.designation,
            description = parkListInfo.park.description,
            imageData = parkListInfo.images.firstOrNull(),
            activities = parkListInfo.parkActivities.map { it.name }
        )

    }
}