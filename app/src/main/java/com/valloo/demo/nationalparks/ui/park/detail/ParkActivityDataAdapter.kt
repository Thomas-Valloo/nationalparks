package com.valloo.demo.nationalparks.ui.park.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valloo.demo.nationalparks.databinding.ListItemParkactivityBinding
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity

class ParkActivityDataAdapter(private var dataSet: List<ParkActivityEntity>) :
    RecyclerView.Adapter<ParkActivityDataAdapter.ParkActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkActivityViewHolder {
        val itemBinding =
            ListItemParkactivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkActivityViewHolder(itemBinding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ParkActivityViewHolder, position: Int) {
        dataSet[position].let {
            holder.bindTo(it)
        }
    }

    fun updateDataset(parkActivities: List<ParkActivityEntity>) {
        dataSet = parkActivities.sortedBy { it.name }
        notifyDataSetChanged()
    }


    class ParkActivityViewHolder(private val itemBinding: ListItemParkactivityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(parkActivityEntity: ParkActivityEntity) {
            itemBinding.nameTextView.text = parkActivityEntity.name
        }
    }
}