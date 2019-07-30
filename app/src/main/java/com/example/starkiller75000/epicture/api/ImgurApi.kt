package com.example.starkiller75000.epicture.api

import android.util.Base64
import com.example.starkiller75000.epicture.entities.Image
import com.example.starkiller75000.epicture.entities.ImgurGalleryAlbum
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject

class ImgurApi {
    companion object {

        private const val thumbnailMode = "m"
        private const val clientId = "df9e9932bc6786b"

        private fun getJsonData(jsonResponse: String): String {
            val responseObject = JSONObject(jsonResponse)
            return if (responseObject.getBoolean("success")) {
                responseObject.get("data").toString()
            } else {
                // Horrible hack, fixme
                "[]"
            }
        }

        fun getImageFile(id: String): ByteArray {
            val request = HttpUtils.createRequest("https://i.imgur.com/$id.jpg", mapOf())
            val response = HttpUtils.sendRequest(request)
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }

        fun getThumbnailFile(id: String): ByteArray {
            val request = HttpUtils.createRequest("https://i.imgur.com/$id$thumbnailMode.jpg", mapOf())
            val response = HttpUtils.sendRequest(request)
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }

        fun uploadImage(file: ByteArray, accessToken: String): String {
            val base64Encoded = Base64.encodeToString(file, Base64.DEFAULT)

            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", base64Encoded)
                .build()

            val request = Request.Builder()
                .url("https://api.imgur.com/3/image")
                .header("Authorization", "Bearer $accessToken")
                .post(body)
                .build()

            val response = HttpUtils.sendRequest(request)
            val responseBody = response.body()!!
            val jsonResponse = responseBody.string()
            val imgurJson = getJsonData(jsonResponse)
            val imgurObject = JSONObject(imgurJson)
            return imgurObject.optString("link", "N/A")
        }

        fun getSearch(search: String, sort: Int): Array<Image> {
            val sortParam = when (sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (sort) {
                2 -> "day"
                3 -> "week"
                4 -> "month"
                5 -> "year"
                6 -> "all"
                else -> ""
            }

            var url = "https://api.imgur.com/3/gallery/search/$sortParam/"
            if (!timeWindow.equals("")) {
                url += timeWindow
            }

            url += "?q=$search"

            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID $clientId"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val gallery = gson.fromJson(imgurJson, Array<ImgurGalleryAlbum>::class.java)
            val galleryImages = gallery
                .filter { if (it.is_album) { it.cover != null } else { true } }
                .map {
                    Image (
                        if (it.is_album) { it.cover } else { it.id },
                        if (it.title != null) { it.title } else { "" },
                        if (it.account_url != null) { it.account_url } else { "" },
                        it.points,
                        it.datetime,
                        if (it.is_album) { it.id } else { "" },
                        it.is_album
                    )
                }

            return galleryImages.toTypedArray()
        }
    }
}