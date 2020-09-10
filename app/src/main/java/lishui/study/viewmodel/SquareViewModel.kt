package lishui.study.viewmodel

import android.app.Application
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lishui.study.common.util.BitmapUtils
import lishui.study.common.util.Utilities

class SquareViewModel(val app: Application) : AndroidViewModel(app) {

    var thumbnailLiveData = MutableLiveData<Bitmap>()

    fun loadThumbnailBitmap() {
        viewModelScope.launch(Dispatchers.Default) {
            getFirstThumbnail()
        }
    }

    private fun getFirstThumbnail() {
        MediaStore.Images.Media.query(
                app.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.DATE_TAKEN),
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 1").use { cursor ->
            if (cursor?.moveToFirst() == true) {
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val orientationIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)
                val id = cursor.getInt(idIndex)
                val orientation = cursor.getInt(orientationIndex)
                var thumb: Bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                        app.contentResolver, id.toLong(), MediaStore.Images.Thumbnails.MINI_KIND, BitmapFactory.Options())
                 thumb = BitmapUtils.rotateBitmapIfNeed(thumb, 0, 0, thumb.width, thumb.height, orientation)
                thumbnailLiveData.postValue(null)
            }
        }
    }
}