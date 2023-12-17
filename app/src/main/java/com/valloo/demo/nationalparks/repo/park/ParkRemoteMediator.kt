package com.valloo.demo.nationalparks.repo.park

import androidx.paging.ExperimentalPagingApi
import androidx.paging.rxjava3.RxRemoteMediator
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo

@OptIn(ExperimentalPagingApi::class)
abstract class ParkRemoteMediator : RxRemoteMediator<Int, ParkListInfo>()
