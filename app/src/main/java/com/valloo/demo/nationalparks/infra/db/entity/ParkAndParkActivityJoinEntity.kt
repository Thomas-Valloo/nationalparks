package com.valloo.demo.nationalparks.infra.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["parkId", "parkActivityId"],
    foreignKeys = [
        ForeignKey(
            entity = ParkEntity::class,
            parentColumns = arrayOf("parkId"),
            childColumns = arrayOf("parkId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParkActivityEntity::class,
            parentColumns = arrayOf("parkActivityId"),
            childColumns = arrayOf("parkActivityId"),
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("parkId"), Index("parkActivityId")],
)
data class ParkAndParkActivityJoinEntity(
    val parkId: String,
    val parkActivityId: String
)
