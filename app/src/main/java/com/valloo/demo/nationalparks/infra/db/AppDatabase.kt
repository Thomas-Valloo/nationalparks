package com.valloo.demo.nationalparks.infra.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valloo.demo.nationalparks.infra.db.dao.ImageDataDao
import com.valloo.demo.nationalparks.infra.db.dao.ParkActivityDao
import com.valloo.demo.nationalparks.infra.db.dao.ParkDao
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkAndParkActivityJoinEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity

@Database(
    entities = [
        ImageDataEntity::class,
        ParkActivityEntity::class,
        ParkAndParkActivityJoinEntity::class,
        ParkEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDataDao(): ImageDataDao
    abstract fun parkActivityDao(): ParkActivityDao
    abstract fun parkDao(): ParkDao
}
