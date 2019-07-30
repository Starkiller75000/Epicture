package com.example.starkiller75000.epicture

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import okhttp3.*
import java.io.IOException
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

private val klaxon = Klaxon()

class ImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val token: String = this.arguments!!.getString("token")
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.listRecycleView)
        rv.layoutManager = LinearLayoutManager(context)
        getUserPhoto(token, rv)
        return view
    }

    inner class SimpleRVAdapter(private val dataSource: List<Datum>) : RecyclerView.Adapter<SimpleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return SimpleViewHolder(view)
        }

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            context?.let { Glide.with(it).load(dataSource[position].link).into(holder.imageView) }
            if (dataSource[position].title != null) {
                holder.textView.text = dataSource[position].title.toString()
            }
            holder.textView1.text = dataSource[position].views.toString()
        }

        override fun getItemCount(): Int {
            return dataSource.size
        }
    }

    /**
     * A Simple ViewHolder for the RecyclerView
     */
    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.itemText) as TextView
        var textView1: TextView = itemView.findViewById(R.id.itemTextVue) as TextView
        var imageView: ImageView = itemView.findViewById(R.id.itemImage) as ImageView
    }

    private fun getUserPhoto(token: String, rv: RecyclerView) {
        val okHttpClient = OkHttpClient()
        val myGetRequest = Request.Builder()
            .url("https://api.imgur.com/3/account/me/images")
            .header("Authorization", "Bearer $token")
            .build()

        okHttpClient.newCall(myGetRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val account = ImgurAccountImages.fromJson(body.toString())
                val statusCode = response.code()

                activity!!.runOnUiThread {
                    rv.adapter = SimpleRVAdapter(account!!.data)
                }
            }
        })
    }
}

data class ImgurAccountImages (
    val data: List<Datum>,
    val success: Boolean,
    val status: Long
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<ImgurAccountImages>(json)
    }
}

data class Datum (
    val id: String,
    val title: Any? = null,
    val description: Any? = null,
    val datetime: Long,
    val type: String,
    val animated: Boolean,
    val width: Long,
    val height: Long,
    val size: Long,
    val views: Long,
    val bandwidth: Long,
    val vote: Any? = null,
    val favorite: Boolean,
    val nsfw: Any? = null,
    val section: Any? = null,

    @Json(name = "account_url")
    val accountURL: String,

    @Json(name = "account_id")
    val accountID: Long,

    @Json(name = "is_ad")
    val isAd: Boolean,

    @Json(name = "in_most_viral")
    val inMostViral: Boolean,

    @Json(name = "has_sound")
    val hasSound: Boolean,

    val tags: List<Any?>,

    @Json(name = "ad_type")
    val adType: Long,

    @Json(name = "ad_url")
    val adURL: String,

    @Json(name = "in_gallery")
    val inGallery: Boolean,

    val deletehash: String,
    val name: String? = null,
    val link: String
)