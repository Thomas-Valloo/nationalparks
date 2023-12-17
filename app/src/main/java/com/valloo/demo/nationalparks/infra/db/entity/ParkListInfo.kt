package com.valloo.demo.nationalparks.infra.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ParkListInfo(
    @Embedded
    val park: ParkEntity,

    @Relation(parentColumn = "parkId", entityColumn = "parkId")
    val images: List<ImageDataEntity>,

    @Relation(
        parentColumn = "parkId",
        entityColumn = "parkActivityId",
        associateBy = Junction(ParkAndParkActivityJoinEntity::class)
    )
    val parkActivities: List<ParkActivityEntity>
)