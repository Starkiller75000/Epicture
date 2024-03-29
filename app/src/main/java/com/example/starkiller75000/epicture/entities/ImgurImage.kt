package com.example.starkiller75000.epicture.entities

data class ImgurImage (
    val id: String,
    val title: String,
    val description: String,
    val datetime: Long,
    val type: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Int,
    val views: Int,
    val bandwidth: Long,
    val section: String,
    val link: String,
    val favorite: Boolean,
    val nsfw: Boolean,
    val vote: String,
    val in_gallery: Boolean
)