package com.example.starkiller75000.epicture

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.starkiller75000.epicture.api.ImgurApi
import com.example.starkiller75000.epicture.entities.Image
import java.util.concurrent.CountDownLatch

class GalleryAdapter(items: Array<Image>, images: Array<ByteArray>, val activity: Activity, token: String?) : RecyclerView.Adapter<GalleryViewHolder>() {
    val accessToken = token

    var images: Array<ByteArray> = images
        @Synchronized set(value) {
            field = value
            notifyDataSetChanged()
        }

    var items: Array<Image> = items
        @Synchronized set(value) {
            field = value
            AsyncAction({ Imgur.getThumbnailFiles(activity.contentResolver, value) },
                { imageFiles -> images = imageFiles })
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_list_item, parent, false)
        return GalleryViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(items[position] to images[position])
    }

    override fun getItemCount(): Int = items.size

    fun onItemClick(position: Int) {
        val intent = Intent(activity, ImageDetailActivity::class.java)
        intent.putExtra("image", items[position])
        intent.putExtra("token", accessToken)
        activity.startActivity(intent)
    }
}