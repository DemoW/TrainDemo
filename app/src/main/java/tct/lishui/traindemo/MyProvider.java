package tct.lishui.traindemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishui.lin on 18-7-27 16:31
 */
public class MyProvider extends ContentProvider {
	private static final String TAG = "PT/MyProvider";

	@Override
	public boolean onCreate() {
		Log.d(TAG, "MyProvider onCreate thread: " + Thread.currentThread().getName());

		// we can know the ContentProvider$onCreate is beyond Application$onCreate
//		oneExcessiveWork();
		return false;
	}

	private void oneExcessiveWork(){
		List<String> stringList = new ArrayList<>();
		for (int i = 0; i < 1000000; i++){
			stringList.add("str" + i);
		}

		// should clear while it not use
		stringList.clear();
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
		Log.d(TAG, "MyProvider thread 2: " + Thread.currentThread().getName());
		return null;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
		return 0;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
		return 0;
	}
}
