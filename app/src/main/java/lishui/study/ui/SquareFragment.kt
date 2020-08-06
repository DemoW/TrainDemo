package lishui.study.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import lishui.study.R
import lishui.study.common.BaseFragment
import lishui.study.databinding.SquareFragmentBinding
import lishui.study.viewmodel.SquareViewModel

class SquareFragment : BaseFragment() {

    private lateinit var mBinding: SquareFragmentBinding
    private lateinit var mViewModel: SquareViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.square_fragment, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //mViewModel = ViewModelProvider.NewInstanceFactory().create(SquareViewModel::class.java)
        mViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
                .create(SquareViewModel::class.java)
        subscribeToViewModel(mViewModel)
        mViewModel.loadThumbnailBitmap()
    }

    private fun subscribeToViewModel(viewModel: SquareViewModel) {
        viewModel.getThumbnailLiveData().observe(viewLifecycleOwner, Observer { bitmap: Bitmap? ->
            bitmap?.let {
                Glide.with(this).load(bitmap).into(mBinding.firstThumbnail)
            }
        })
    }
}