package lishui.study.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import lishui.study.R;
import lishui.study.util.TrainUtils;

/**
 * Created by lishui.lin on 20-4-22
 */
public class BindingAdapter {

    @androidx.databinding.BindingAdapter("imageFromUrl")
    public static void bindImageFromUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.color.base_bg_banner)
                .into(imageView);
    }

    @androidx.databinding.BindingAdapter({"imageFromBitmap"})
    public static void bindLiveBitmap(ImageView imageView, Bitmap bitmap) {
        Glide.with(imageView.getContext())
                .load(bitmap)
                .placeholder(R.color.base_bg_banner)
                .into(imageView);
    }

    @androidx.databinding.BindingAdapter({"goBrowserByUrl"})
    public static void bindGoBrowser(View itemView, String urlStr) {
        itemView.setOnClickListener(view -> {
            if (TextUtils.isEmpty(urlStr)) {
                Toast.makeText(view.getContext(), "等待数据刷新中...", Toast.LENGTH_SHORT).show();
                return;
            }
            TrainUtils.startWebViewBrowser(view.getContext(), urlStr);
        });

    }
}
