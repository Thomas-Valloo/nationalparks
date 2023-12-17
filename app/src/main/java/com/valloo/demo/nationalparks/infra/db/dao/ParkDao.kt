package com.valloo.demo.nationalparks.infra.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.valloo.demo.nationalparks.infra.db.entity.ParkAndParkActivityJoinEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkListInfo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ParkDao {

    @Transaction
    @Query("SELECT * FROM ParkEntity ORDER BY parkCode ASC")
    fun getPagedList(): PagingSource<Int, ParkListInfo>

    @Query("SELECT * FROM ParkEntity WHERE parkId = :id")
    fun getById(id: String): Single<ParkEntity>

    @Query("SELECT COUNT(parkId) FROM ParkEntity")
    fun count(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(park: ParkEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParkActivityJoinEntity(crossRefs: List<ParkAndParkActivityJoinEntity>): Completable

    @Transaction
    @Query("DELETE FROM ParkEntity")
    fun deleteAll(): Completable
}
