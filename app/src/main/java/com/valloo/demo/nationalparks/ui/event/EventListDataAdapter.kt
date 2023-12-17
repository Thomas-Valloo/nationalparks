package com.valloo.demo.nationalparks.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.databinding.ListItemEventBinding
import com.valloo.demo.nationalparks.infra.http.jsonDto.EventJsonDto

class EventListDataAdapter :
    PagingDataAdapter<EventJsonDto, EventListDataAdapter.EventListViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val itemBinding =
            ListItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindTo(it)
        }
    }

    companion object {
        private val Comparator = object : DiffUtil.ItemCallback<EventJsonDto>() {
            override fun areItemsTheSame(oldItem: EventJsonDto, newItem: EventJsonDto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventJsonDto, newItem: EventJsonDto): Boolean {
                return oldItem == newItem
            }
        }
    }

    class EventListViewHolder(private val itemBinding: ListItemEventBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(event: EventJsonDto) {
            itemBinding.dateTextView.text = event.startDate
            itemBinding.descriptionTextView.text =
                HtmlCompat.fromHtml(event.description, FROM_HTML_MODE_COMPACT)
            itemBinding.feeTextView.text =
                event.feeInfo.ifBlank { itemBinding.feeTextView.context.getString(R.string.eventList_noFee) }
            itemBinding.parkFullNameTextView.text = event.parkFullName
            itemBinding.titleTextView.text = event.title
        }
    }
}