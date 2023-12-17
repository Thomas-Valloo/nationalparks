package com.valloo.demo.nationalparks.infra.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkActivityJsonDto

@Entity
data class ParkActivityEntity(
    @PrimaryKey
    var parkActivityId: String,
    val name: String,
) {
    companion object {
        fun fromJsonDto(parkActivity: ParkActivityJsonDto) =
            ParkActivityEntity(
                parkActivityId = parkActivity.id,
                name = parkActivity.name
            )
    }
}

