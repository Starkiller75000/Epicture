package com.example.starkiller75000.epicture

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.starkiller75000.epicture.data.ImageRecord
import com.example.starkiller75000.epicture.entities.Image
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    val IMAGE_LOADER = 33
    var _image: Image? = null
    var imageId: String? = null
    var menu: Menu? = null
    var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val startIntent = intent
        val image = startIntent.getParcelableExtra<Image>("image")
        val token = startIntent.getStringExtra("token")
        accessToken = token

        detailImageTitle.text = image.title
        detailImageAuthor.text = image.username
        _image = image
        imageId = if (image.isAlbum) { image.albumId } else { image.id }
        loaderManager.initLoader(IMAGE_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            IMAGE_LOADER -> CursorLoader(this, ImageRecord.buildImageUri(_image!!.id), ImageRecord.COLUMNS, null, null, null)
            else -> throw Exception("Invalid Loader id!")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        if (loader == null) {
            return
        }

        if (loader.id == IMAGE_LOADER) {
            AsyncAction({
                val imageFile = Imgur.getImageFile(contentResolver, data, _image!!.id)
                ImageUtils.getScaledDownBitmap(imageFile)
            }, { detailImageView.setImageBitmap(it)
            })
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {}
}
