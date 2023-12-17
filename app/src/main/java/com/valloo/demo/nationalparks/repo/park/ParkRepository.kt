package com.valloo.demo.nationalparks.repo.park

import androidx.paging.PagingSource
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo
import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkJsonDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ParkRepository {
    fun getPagedList(): PagingSource<Int, ParkListInfo>

    fun getParkEntity(id: String): Single<ParkEntity>

    fun getParkImages(parkId: String): Single<List<ImageDataEntity>>

    fun getParkActivities(parkId: String): Single<List<ParkActivityEntity>>

    fun savePark(parkJsonDto: ParkJsonDto): Completable
}