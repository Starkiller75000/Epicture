package com.example.starkiller75000.epicture

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.*
import java.io.IOException

private val klaxon = Klaxon()

class ProfileFragment : Fragment() {
    private var username: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val string: String = this.arguments!!.getString("data")
        username = string
        if (container == null) {
            return null
        }
        val ll = inflater.inflate(R.layout.fragment_profile, container, false) as RelativeLayout
        getUserData()
        return ll
    }

    private fun getUserData() {
        val okHttpClient = OkHttpClient()
        val myGetRequest = Request.Builder()
            .url("https://api.imgur.com/3/account/$username")
            .header("Authorization", "Client-ID df9e9932bc6786b")
            .build()

        okHttpClient.newCall(myGetRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val account = ImgurAccount.fromJson(body.toString())
                val statusCode = response.code()

                activity!!.runOnUiThread {
                    User.text = username
                    Picasso.get().load(account!!.data.avatar).into(imageView)
                    point_rep.text = account!!.data.reputation.toString() + " PTS . " + account.data.reputationName
                }
            }
        })
    }
}


data class ImgurAccount (
    val data: Data,
    val success: Boolean,
    val status: Long
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<ImgurAccount>(json)
    }
}

data class Data (
    val id: Long,
    val url: String,
    val bio: Any? = null,
    val avatar: String,

    @Json(name = "avatar_name")
    val avatarName: String,

    val cover: String,

    @Json(name = "cover_name")
    val coverName: String,

    val reputation: Long,

    @Json(name = "reputation_name")
    val reputationName: String,

    val created: Long,

    @Json(name = "pro_expiration")
    val proExpiration: Boolean,

    @Json(name = "user_follow")
    val userFollow: UserFollow,

    @Json(name = "is_blocked")
    val isBlocked: Boolean
)

data class UserFollow (
    val status: Boolean
)