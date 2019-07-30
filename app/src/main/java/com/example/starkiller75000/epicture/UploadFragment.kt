package com.example.starkiller75000.epicture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.starkiller75000.epicture.api.ImgurApi
import java.io.File
import java.io.FileInputStream


class UploadFragment : Fragment() {

    private var accessToken = ""
    private val GALLERY_RESULT = 0

    fun getTempFileUri() = Uri.parse("${Environment.getExternalStorageDirectory().absolutePath}/temp.jpg")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        accessToken = this.arguments!!.getString("token")
        Upload(view)
        return view
    }

    fun Upload(view: View) {
        val selectPictureString = getString(R.string.select_picture)
        val pickUpload = view.findViewById<Button>(R.id.pickUpload)
        pickUpload.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, selectPictureString), GALLERY_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            AsyncAction({
                val file = (if (requestCode == GALLERY_RESULT) {
                    val contentResolver = activity!!.contentResolver
                    contentResolver.openInputStream(data?.data).readBytes()
                } else {
                    try {
                        val stream = FileInputStream(File(getTempFileUri().toString()))
                        val buffer = arrayListOf<Byte>()
                        var b: Int = stream.read()
                        while (b != -1) {
                            buffer.add(b.toByte())
                            b = stream.read()
                        }
                        buffer.toByteArray()
                    } catch (e: Exception) {
                        null
                    }
                }) ?: throw Exception("Something something")
                return@AsyncAction ImgurApi.uploadImage(file, accessToken)
            }, { imageUrl ->
                Notification.uploadedNotification(activity!!.applicationContext, imageUrl)
            })
        }
    }
}