package com.valloo.demo.nationalparks.infra.http.jsonDto

import com.google.gson.annotations.SerializedName


data class EventJsonDto(

    /**
     * Unique identifier for this event
     */
    val id: String,

    /**
     * Start date for event
     */
    @SerializedName("datestart")
    val startDate: String,

    /**
     * End date for event
     */
    @SerializedName("dateend")
    val endDate: String,

    /**
     * Event description
     */
    val description: String,

    /**
     * Fee information for event
     */
    @SerializedName("feeinfo")
    val feeInfo: String,

    /**
     * Name and designation of the park associated with event
     */
    @SerializedName("parkfullname")
    val parkFullName: String,

    /**
     * Event title
     */
    val title: String,
)