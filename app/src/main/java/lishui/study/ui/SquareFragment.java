package lishui.study.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import lishui.study.R;
import lishui.study.common.BaseFragment;
import lishui.study.databinding.SquareFragmentBinding;
import lishui.study.viewmodel.SquareViewModel;

public class SquareFragment extends BaseFragment {

    private SquareFragmentBinding mBinding;

    private SquareViewModel mViewModel;

    public static SquareFragment newInstance() {
        return new SquareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.square_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SquareViewModel.class);
        subscribeToViewModel(mViewModel);

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
