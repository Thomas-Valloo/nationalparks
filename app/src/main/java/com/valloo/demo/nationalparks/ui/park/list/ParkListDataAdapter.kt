package com.valloo.demo.nationalparks.ui.park.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class ParkListDataAdapter : PagingDataAdapter<ParkListItem, ParkListItemViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkListItemViewHolder {
        return ParkListItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ParkListItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindTo(it)
        }
    }

    companion object {
        private val Comparator = object : DiffUtil.ItemCallback<ParkListItem>() {
            override fun areItemsTheSame(oldItem: ParkListItem, newItem: ParkListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ParkListItem, newItem: ParkListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}