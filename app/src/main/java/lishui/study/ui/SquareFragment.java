package lishui.study.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.study.R;
import lishui.study.viewmodel.SquareViewModel;

public class SquareFragment extends Fragment {

    @BindView(R.id.first_thumbnail)
    ImageView mImageView;

    private SquareViewModel mViewModel;

    public static SquareFragment newInstance() {
        return new SquareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.square_fragment, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SquareViewModel.class);
        subscribeToViewModel(mViewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadThumbnailBitmap();
    }

    private void subscribeToViewModel(SquareViewModel viewModel) {
        viewModel.getThumbnailLiveData().observe(this, bitmap -> {
            if (bitmap != null) {
                Glide.with(this).load(bitmap).into(mImageView);
//                mImageView.setImageBitmap(bitmap);
            }
        });
    }

}
