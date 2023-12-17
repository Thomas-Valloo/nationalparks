package com.valloo.demo.nationalparks.ui.park.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.ui.widget.getProgressDrawable
import com.valloo.demo.nationalparks.ui.widget.loadImage

// Note: Binding is not used here, to propose another approach to update UI with data.
class ParkListItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_park, parent, false)
) {
    private val cardView = itemView.findViewById<MaterialCardView>(R.id.cardView)
    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
    private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
    private val designationTextView = itemView.findViewById<TextView>(R.id.designationTextView)
    private val descriptionTextView = itemView.findViewById<TextView>(R.id.descriptionTextView)
    private val activitiesTextView = itemView.findViewById<TextView>(R.id.activitiesTextView)

    fun bindTo(park: ParkListItem) {
        cardView.setOnClickListener {
            val action = ParkListFragmentDirections.actionNavigationParkListToParkDetail(
                id = park.id,
            )
            cardView.findNavController().navigate(action)
        }

        nameTextView.text = park.name
        designationTextView.text = park.designation
        descriptionTextView.text = park.description
        activitiesTextView.text = if (park.activities.isEmpty()) {
            activitiesTextView.context.getString(R.string.parkList_item_noactivities)
        } else {
            activitiesTextView.context.getString(
                R.string.parkList_item_activities,
                park.activities.sorted().joinToString(separator = ", ")
            )
        }

        imageView.loadImage(park.imageData?.url, getProgressDrawable(imageView.context))
    }
}