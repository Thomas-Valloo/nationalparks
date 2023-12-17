package com.valloo.demo.nationalparks.repo.park

import androidx.paging.PagingSource
import com.valloo.demo.nationalparks.infra.db.AppDatabase
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkAndParkActivityJoinEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo
import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkJsonDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ParkRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : ParkRepository {

    override fun getPagedList(): PagingSource<Int, ParkListInfo> {
        return database.parkDao().getPagedList()
    }

    override fun getParkEntity(id: String): Single<ParkEntity> {
        return database.parkDao().getById(id).subscribeOn(Schedulers.io())
    }

    override fun getParkImages(parkId: String): Single<List<ImageDataEntity>> {
        return database.imageDataDao().findAll(parkId).subscribeOn(Schedulers.io())
    }

    override fun getParkActivities(parkId: String): Single<List<ParkActivityEntity>> {
        return database.parkActivityDao().findAll(parkId).subscribeOn(Schedulers.io())
    }

    override fun savePark(parkJsonDto: ParkJsonDto): Completable {
        return database.parkDao().insert(ParkEntity.fromJsonDto(parkJsonDto)).andThen(
            database.imageDataDao()
                .insertAll(parkJsonDto.images.map { ImageDataEntity.fromJsonDto(it, parkJsonDto.id) })
        ).andThen(
            database.parkActivityDao()
                .insertAll(parkJsonDto.activities.map(ParkActivityEntity::fromJsonDto))
        ).andThen(
            database.parkDao().insertParkActivityJoinEntity(parkJsonDto.activities.map {
                ParkAndParkActivityJoinEntity(
                    parkJsonDto.id, it.id
                )
            })
        )
    }
}