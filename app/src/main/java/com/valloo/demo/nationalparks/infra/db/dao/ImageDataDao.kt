package com.valloo.demo.nationalparks.infra.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ImageDataDao {

    @Query("SELECT * FROM ImageDataEntity WHERE parkId = :parkId")
    fun findAll(parkId: String): Single<List<ImageDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(imageDataEntities: List<ImageDataEntity>): Completable
}
