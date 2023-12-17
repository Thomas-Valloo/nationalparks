package com.valloo.demo.nationalparks.ui.park.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import com.valloo.demo.nationalparks.ui.widget.getProgressDrawable
import com.valloo.demo.nationalparks.ui.widget.loadImage

class ImageDataAdapter(private var dataSet: List<ImageDataEntity>) :
    RecyclerView.Adapter<ImageDataAdapter.ImageDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDataViewHolder {
        return ImageDataViewHolder(parent)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ImageDataViewHolder, position: Int) {
        dataSet[position].let {
            holder.bindTo(it)
        }
    }

    fun updateDataset(images: List<ImageDataEntity>) {
        dataSet = images
        notifyDataSetChanged()
    }

    class ImageDataViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
    ) {
        private val imageView = itemView.findViewById<ImageView>(R.id.carouselImageView)

        fun bindTo(imageData: ImageDataEntity?) {
            imageView.loadImage(imageData?.url, getProgressDrawable(imageView.context))
        }
    }

}