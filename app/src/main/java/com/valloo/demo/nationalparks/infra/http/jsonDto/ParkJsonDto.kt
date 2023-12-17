package com.valloo.demo.nationalparks.infra.http.jsonDto

data class ParkJsonDto(
    val id: String,

    /**
     * Introductory paragraph from the park homepage
     */
    val description: String,

    /**
     * Type of designation (eg, national park, national monument, national recreation area, etc)
     */
    val designation: String,

    /**
     * Full park name (with designation)
     */
    val fullName: String,

    /**
     * Short park name (no designation)
     */
    val name: String,

    /**
     * A variable width character code used to identify a specific park
     */
    val parkCode: String,

    /**
     * Park Website
     */
    val url: String,

    /**
     * General description of the weather in the park over the course of a year
     */
    val weatherInfo: String,

    val activities: List<ParkActivityJsonDto>,

    val images: List<ImageDataJsonDto>
)