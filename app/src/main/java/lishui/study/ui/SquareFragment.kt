package lishui.study.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import lishui.study.R
import lishui.study.common.BaseFragment
import lishui.study.databinding.SquareFragmentBinding
import lishui.study.viewmodel.SquareViewModel

class SquareFragment : BaseFragment() {

    private lateinit var mBinding: SquareFragmentBinding
    private val mViewModel by viewModels<SquareViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.square_fragment, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //mViewModel = ViewModelProvider.NewInstanceFactory().create(SquareViewModel::class.java)
//        mViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
//                .create(SquareViewModel::class.java)
        mBinding.bitmapLiveData = mViewModel.thumbnailLiveData
        mBinding.lifecycleOwner = this
        mViewModel.loadThumbnailBitmap()
    }
}