package com.valloo.demo.nationalparks.infra.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkJsonDto


/**
 * For fields info, @see [ParkJsonDto]
 */
@Entity
data class ParkEntity(

    @PrimaryKey
    val parkId: String,

    val description: String,
    val designation: String,
    val fullName: String,
    val name: String,
    val parkCode: String,
    val url: String,
    val weatherInfo: String,
) {
    companion object {
        fun fromJsonDto(parkJsonDto: ParkJsonDto) =
            ParkEntity(
                parkId = parkJsonDto.id,
                description = parkJsonDto.description,
                designation = parkJsonDto.designation,
                fullName = parkJsonDto.fullName,
                name = parkJsonDto.name,
                parkCode = parkJsonDto.parkCode,
                url = parkJsonDto.url,
                weatherInfo = parkJsonDto.weatherInfo,
            )
    }
}