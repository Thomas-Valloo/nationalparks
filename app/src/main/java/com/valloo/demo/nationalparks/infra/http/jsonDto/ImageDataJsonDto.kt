package com.valloo.demo.nationalparks.infra.http.jsonDto

data class ImageDataJsonDto(
    val id: Long,
    val altText: String,
    val caption: String,
    val title: String,
    val url: String
)