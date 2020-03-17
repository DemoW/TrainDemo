package lishui.study.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import lishui.study.databinding.SquareFragmentBinding;
import lishui.study.viewmodel.SquareViewModel;

public class SquareFragment extends Fragment {

    private SquareFragmentBinding mBinding;

    private SquareViewModel mViewModel;

    public static SquareFragment newInstance() {
        return new SquareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = SquareFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
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
        viewModel.getThumbnailLiveData().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                Glide.with(this).load(bitmap).into(mBinding.firstThumbnail);
//                mImageView.setImageBitmap(bitmap);
            }
        });
    }

}
