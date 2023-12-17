package com.valloo.demo.nationalparks.infra.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ParkActivityDao {

    @Query(
        """
        SELECT parkActivity.*
        FROM ParkActivityEntity parkActivity
        LEFT JOIN ParkAndParkActivityJoinEntity joinEntity
            ON parkActivity.parkActivityId = joinEntity.parkActivityId
        WHERE joinEntity.parkId = :parkId
            """
    )
    fun findAll(parkId: String): Single<List<ParkActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(parkActivities: List<ParkActivityEntity>): Completable
}
