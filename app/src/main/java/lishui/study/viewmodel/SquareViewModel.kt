package lishui.study.viewmodel

import android.app.Application
import android.content.ContentUris
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lishui.study.common.util.BitmapUtils
import lishui.study.common.util.Utilities

class SquareViewModel(val app: Application) : AndroidViewModel(app) {

    private val thumbnailLiveData = MutableLiveData<Bitmap>()

    fun loadThumbnailBitmap() {
        viewModelScope.launch(Dispatchers.Default) {
            getFirstThumbnail()
        }
    }

    private suspend fun getFirstThumbnail() {
        MediaStore.Images.Media.query(
                app.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.DATE_TAKEN),
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 1").use { cursor ->
            if (cursor != null && cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val orientationIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)
                val id = cursor.getInt(idIndex)
                val orientation = cursor.getInt(orientationIndex)
                var thumb: Bitmap
                thumb = if (Utilities.ATLEAST_Q) {
                    // get the thumb uri
                    val thumbUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                    // Thumbnails.MINI_KIND and signal can not be cancelled
                    app.contentResolver.loadThumbnail(thumbUri, Size(512, 384), null)
                } else {
                    MediaStore.Images.Thumbnails.getThumbnail(getApplication<Application>().contentResolver,
                            id.toLong(), MediaStore.Images.Thumbnails.MINI_KIND, null)
                }
                thumb = BitmapUtils.rotateBitmapIfNeed(thumb, 0, 0, thumb.width, thumb.height, orientation)
                thumbnailLiveData.postValue(thumb)
            }
        }
    }

    fun getThumbnailLiveData(): LiveData<Bitmap> {
        return thumbnailLiveData
    }
}