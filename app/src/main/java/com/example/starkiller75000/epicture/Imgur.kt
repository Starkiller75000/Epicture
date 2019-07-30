package com.example.starkiller75000.epicture


import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import com.example.starkiller75000.epicture.api.ImgurApi
import com.example.starkiller75000.epicture.data.ImageRecord
import com.example.starkiller75000.epicture.data.ThumbnailRecord
import com.example.starkiller75000.epicture.entities.Image
import java.util.concurrent.CountDownLatch

class Imgur {
    companion object {

        fun getImageFile(contentResolver: ContentResolver, cursor: Cursor?, imageId: String): ByteArray {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val imageFile = cursor.getBlob(1)
                cursor.close()
                return imageFile
            }

            val imageFile = ImgurApi.getImageFile(imageId)

            if (imageFile.size < 1024 * 1024 * 5) {
                val dbValues = ContentValues()
                dbValues.put(ImageRecord.COLUMN_ID, imageId)
                dbValues.put(ImageRecord.COLUMN_FILE, imageFile)

                contentResolver.insert(ImageRecord.CONTENT_URI, dbValues)
            }

            return imageFile
        }

        fun getThumbnailFile(contentResolver: ContentResolver, thumbId: String): ByteArray {
            val cursor = contentResolver.query(ThumbnailRecord.buildThumbnailUri(thumbId), ThumbnailRecord.COLUMNS, null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val thumbFile = cursor.getBlob(1)
                cursor.close()
                return thumbFile
            }

            val thumbFile = ImgurApi.getThumbnailFile(thumbId)
            val dbValues = ContentValues()
            dbValues.put(ThumbnailRecord.COLUMN_ID, thumbId)
            dbValues.put(ThumbnailRecord.COLUMN_FILE, thumbFile)

            contentResolver.insert(ThumbnailRecord.CONTENT_URI, dbValues)
            return thumbFile
        }

        fun getThumbnailFiles(contentResolver: ContentResolver, images: Array<Image>): Array<ByteArray> =
            getThumbnailFilesFromMetadata(contentResolver, images.map { it.id })

        private fun getThumbnailFilesFromMetadata(contentResolver: ContentResolver, ids: List<String>): Array<ByteArray> {
            val size = ids.size
            val latch = CountDownLatch(size)
            val resultArray = Array(size, { byteArrayOf() })
            val pool = AsyncAction.pool
            for (i in ids.indices) {
                pool.submit {
                    resultArray[i] = Imgur.getThumbnailFile(contentResolver, ids[i])
                    latch.countDown()
                }
            }
            latch.await()
            return resultArray
        }
    }
}