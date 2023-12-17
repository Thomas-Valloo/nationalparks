package com.valloo.demo.nationalparks.infra.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valloo.demo.nationalparks.infra.http.jsonDto.ImageDataJsonDto

@Entity(
    foreignKeys = [ForeignKey(
        entity = ParkEntity::class,
        parentColumns = arrayOf("parkId"),
        childColumns = arrayOf("parkId"),
        onDelete = CASCADE
    )],
    indices = [Index("parkId")],
)
data class ImageDataEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    val parkId: String,

    val altText: String,
    val caption: String,
    val title: String,
    val url: String
) {

    companion object {
        fun fromJsonDto(imageData: ImageDataJsonDto, parkId: String) =
            ImageDataEntity(
                id = imageData.id,
                parkId = parkId,
                altText = imageData.altText,
                caption = imageData.caption,
                title = imageData.title,
                url = imageData.url
            )
    }
}