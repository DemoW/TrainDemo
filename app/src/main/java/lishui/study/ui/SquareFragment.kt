package lishui.study.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Bundle
import android.util.Log
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
        mBinding.bitmapLiveData = mViewModel.thumbnailLiveData
        mBinding.lifecycleOwner = this
        mViewModel.loadThumbnailBitmap()

        mBinding.firstThumbnail.setOnClickListener {
            val shortcutManager = context?.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
            if (shortcutManager.isRequestPinShortcutSupported) {
                val pinShortcutInfo = ShortcutInfo.Builder(context, "test-shortcut").apply {
                    val intent = Intent(context, SearchActivity::class.java)
                    intent.action = "lishui.study.action.SEARCH"
                    setIntent(intent)
                    setActivity(ComponentName("lishui.study", "lishui.study.ui.MainActivity"))
                    setShortLabel("test short label")
                }.build()

                Log.d("lishuii", "pinShortcutInfo=$pinShortcutInfo")
                shortcutManager.requestPinShortcut(pinShortcutInfo, null)
            }
        }
    }
}