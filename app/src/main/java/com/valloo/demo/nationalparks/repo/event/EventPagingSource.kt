package com.valloo.demo.nationalparks.repo.event

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.valloo.demo.nationalparks.infra.http.EventsApi
import com.valloo.demo.nationalparks.infra.http.EventsApi.Companion.STARTING_PAGE_INDEX
import com.valloo.demo.nationalparks.infra.http.jsonDto.EventJsonDto
import com.valloo.demo.nationalparks.repo.USState
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import java.io.IOException

/**
 * PagingSource for events. Will query the NPS server to get results.
 */
class EventPagingSource(
    private val eventsApi: EventsApi,
    private val pageSize: Int,
    /**
     * Events are restricted to a state.
     */
    private val usState: USState
) : RxPagingSource<Int, EventJsonDto>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, EventJsonDto>> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return eventsApi.events(
            stateCode = listOf(usState.code),
            pageSize = pageSize,
            pageNumber = page
        )
            .map<LoadResult<Int, EventJsonDto>> { result ->
                LoadResult.Page(
                    data = result.data,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                    nextKey = if (result.data.size < pageSize) null else page.plus(1)
                )
            }
            .onErrorReturn { e ->
                when (e) {
                    // Retrofit calls that return the body type throw either IOException for
                    // network failures, or HttpException for any non-2xx HTTP status codes.
                    // This code reports all errors to the UI, but you can inspect/wrap the
                    // exceptions to provide more context.
                    is IOException -> LoadResult.Error(e)
                    is HttpException -> LoadResult.Error(e)
                    else -> throw e
                }
            }
    }

    override fun getRefreshKey(state: PagingState<Int, EventJsonDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
