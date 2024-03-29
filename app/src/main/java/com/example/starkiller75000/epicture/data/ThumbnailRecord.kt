package com.example.starkiller75000.epicture.data

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.example.starkiller75000.epicture.ImgurContract

class ThumbnailRecord(val id: String, val file: ByteArray) {
    companion object {
        val CONTENT_URI = ImgurContract.BASE_CONTENT_URI.buildUpon()
            .appendPath(ImgurContract.PATH_THUMBNAIL)
            .build()
        val CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/${ImgurContract.CONTENT_AUTHORITY}/${ImgurContract.PATH_THUMBNAIL}"
        val CONTENT_ITEM_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/${ImgurContract.CONTENT_AUTHORITY}/${ImgurContract.PATH_THUMBNAIL}"

        val TABLE_NAME = "thumbnail"
        val COLUMN_ID = "_id"
        val COLUMN_FILE = "file"

        val COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_FILE
        )

        val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (\n" +
                "$COLUMN_ID TEXT PRIMARY KEY,\n" +
                "$COLUMN_FILE BLOB NOT NULL\n" +
                ");"


        fun buildThumbnailUri(id: String): Uri = CONTENT_URI.buildUpon().appendPath(id).build()
        fun getInstanceFromCursor(cursor: Cursor): ThumbnailRecord =
            ThumbnailRecord(
                cursor.getString(0),
                cursor.getBlob(1)
            )
    }
}