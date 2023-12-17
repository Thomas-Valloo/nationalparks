package com.valloo.demo.nationalparks.ui.park.list

import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity

data class ParkListItem(
    val id: String,
    val parkCode: String,
    val name: String,
    val designation: String,
    val description: String,
    val imageData: ImageDataEntity?,
    val activities: List<String>
)