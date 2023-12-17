package com.valloo.demo.nationalparks.infra.http

import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkJsonDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/** https://www.nps.gov/subjects/developer/api-documentation.htm#/parks */
interface ParksApi {

    /**
     * @param parkCode A comma delimited list of park codes (each 4-10 characters in length).
     * @param limit Number of results to return per request. Default is 50.
     * @param start Get the next [limit] results starting with this number. Default
     *   is 0. [STARTING_INDEX]
     */
    @GET("parks")
    fun parks(
        @Query("parkCode") parkCode: List<String> = emptyList(),
        @Query("start") start: Int? = null,
        @Query("limit") limit: Int? = null
    ): Single<ApiResponse<ParkJsonDto>>

    companion object {
        /** Starting index for [parks] request. */
        const val STARTING_INDEX = 0
    }
}
