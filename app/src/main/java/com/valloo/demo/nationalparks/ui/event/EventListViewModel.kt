package com.valloo.demo.nationalparks.ui.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.valloo.demo.nationalparks.infra.http.EventsApi
import com.valloo.demo.nationalparks.infra.http.jsonDto.EventJsonDto
import com.valloo.demo.nationalparks.repo.USState
import com.valloo.demo.nationalparks.repo.event.EventPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val eventsApi: EventsApi
) : ViewModel() {

    fun getEventsList(): Flowable<PagingData<EventJsonDto>> {
        val stateCode = state.get<String>(ARG_STATE_CODE)!!
        val usState = USState.valueOf(stateCode)
        val pageSize = 20
        return Pager(
            config = PagingConfig(pageSize = pageSize)
        ) {
            EventPagingSource(eventsApi, pageSize, usState)
        }
            .flowable
    }

    companion object {
        const val ARG_STATE_CODE = "argStateCode"
    }
}