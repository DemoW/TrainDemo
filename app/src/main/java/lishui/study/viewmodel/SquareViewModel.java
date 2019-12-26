package lishui.study.viewmodel;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Size;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import lishui.study.common.log.LogUtil;
import lishui.study.common.util.BitmapUtils;
import lishui.study.common.util.Utilities;

public class SquareViewModel extends AndroidViewModel {

    private MutableLiveData<Bitmap> thumbnailLiveData = new MutableLiveData<>();

    public SquareViewModel(Application app) {
        super(app);
    }

    public void loadThumbnailBitmap() {
        try (Cursor cursor = MediaStore.Images.Media.query(
                getApplication().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.ORIENTATION,
                        MediaStore.Images.ImageColumns.DATE_TAKEN},
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 1")) {

            if (cursor != null && cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
                int orientationIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

                int id = cursor.getInt(idIndex);
                int orientation = cursor.getInt(orientationIndex);

                LogUtil.d("get first thumbnail orientation: " + orientation);
                Bitmap thumb;
                if (Utilities.ATLEAST_Q) {
                    // get the thumb uri
                    final Uri thumbUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    // Thumbnails.MINI_KIND and signal can not be cancelled
                    thumb = getApplication().getContentResolver().loadThumbnail(thumbUri, new Size(512, 384), null);
                } else {
                    thumb = MediaStore.Images.Thumbnails.getThumbnail(getApplication().getContentResolver(),
                            id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                }

                thumb = BitmapUtils.rotateBitmapIfNeed(thumb, 0, 0, thumb.getWidth(), thumb.getHeight(), orientation);

                thumbnailLiveData.postValue(thumb);
            }
        } catch (Exception e) {
            LogUtil.d("loadThumbnailBitmap error");
        }
    }

    public LiveData<Bitmap> getThumbnailLiveData() {
        return thumbnailLiveData;
    }
}
