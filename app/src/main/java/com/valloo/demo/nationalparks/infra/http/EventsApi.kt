package com.valloo.demo.nationalparks.infra.http

import com.valloo.demo.nationalparks.infra.http.jsonDto.EventJsonDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/** https://www.nps.gov/subjects/developer/api-documentation.htm#/events */
interface EventsApi {

    /**
     * @param pageSize The number of results per page. Default is 10.
     * @param pageNumber The current page number for the results. Default
     *   is 1. [STARTING_PAGE_INDEX]
     * @param parkCode A comma delimited list of park codes (each 4 characters in length).
     * @param stateCode A comma delimited list of 2 character state codes.
     * @param dateStart A stating date in the yyyy-mm-dd format to filter events by.
     * @param dateEnd An ending date in the yyyy-mm-dd format to filter events by.
     */
    @GET("events")
    fun events(
        @Query("pageSize") pageSize: Int? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("parkCode") parkCode: List<String> = emptyList(),
        @Query("stateCode") stateCode: List<String> = emptyList(),
        @Query("dateStart") dateStart: String? = null,
        @Query("dateEnd") dateEnd: String? = null
    ): Single<ApiResponse<EventJsonDto>>

    companion object {
        /**
         * Starting page index for [events] request.
         */
        const val STARTING_PAGE_INDEX = 1
    }
}
