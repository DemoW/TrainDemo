package lishui.study.util;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import lishui.study.adapter.WrapperAdapter;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by lishui.lin on 18-8-1 16:40
 */
public class SwipeToLoadHelper extends RecyclerView.OnScrollListener {

	private static final String TAG = "PT/SwipeToLoadHelper";
	private RecyclerView.LayoutManager mLayoutManager;
	private WrapperAdapter mAdapterWrapper;
	private LoadMoreListener mListener;
	/** 是否正在加载中 */
	private boolean mLoading = false;
	/** 上拉刷新功能是否开启 */
	private boolean mIsSwipeToLoadEnabled = true;

	public SwipeToLoadHelper(RecyclerView recyclerView, WrapperAdapter adapterWrapper) {
		mLayoutManager = recyclerView.getLayoutManager();
		mAdapterWrapper = adapterWrapper;

		if (mLayoutManager instanceof GridLayoutManager) {
			mAdapterWrapper.setAdapterType(WrapperAdapter.ADAPTER_TYPE_GRID);
			mAdapterWrapper.setSpanCount(((GridLayoutManager) mLayoutManager).getSpanCount());
		} else if (mLayoutManager instanceof LinearLayoutManager) {
			mAdapterWrapper.setAdapterType(WrapperAdapter.ADAPTER_TYPE_LINEAR);
		}

		// 将OnScrollListener设置RecyclerView
		recyclerView.addOnScrollListener(this);

	}

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		if (mIsSwipeToLoadEnabled && SCROLL_STATE_IDLE == newState && !mLoading) {
			if (mLayoutManager instanceof GridLayoutManager) {
				final GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
				gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
					@Override
					public int getSpanSize(int position) {
						if (mIsSwipeToLoadEnabled) {
							// 功能开启, 根据位置判断, 最后一个item时返回整个宽度, 其他位置返回1
							// AdapterWrapper会保证最后一个item会从新的一行开始
							if (position == mLayoutManager.getItemCount() - 1) {
								return gridLayoutManager.getSpanCount();
							} else {
								return 1;
							}
						} else {
							return 1;
						}
					}
				});
			}

			if (mLayoutManager instanceof LinearLayoutManager) {
				LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLayoutManager;
				int lastCompletePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
				// only when the complete visible item is second last
				if (lastCompletePosition == mLayoutManager.getItemCount() - 2) {
					int firstCompletePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
					// 获取最后一项item高度
					View child = linearLayoutManager.findViewByPosition(lastCompletePosition + 1);
					if (child == null)
						return;
//					int deltaY = recyclerView.getBottom() - recyclerView.getPaddingBottom() - child.getBottom();
					int deltaY = child.getHeight();
					if (deltaY > 0 && firstCompletePosition != 0) {
						recyclerView.smoothScrollBy(0, -deltaY);
					}
				} else if (lastCompletePosition == mLayoutManager.getItemCount() - 1) {
					// 最后一项完全显示, 触发操作, 执行加载更多操作 禁用回弹判断
					mLoading = true;
					mAdapterWrapper.setLoadItemState(true);
					if (mListener != null) {
						mListener.onLoad();
					}
				}
			}
		}
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
	}

	/** 设置下拉刷新功能是否开启 */
	public void setSwipeToLoadEnabled(boolean isSwipeToLoadEnabled) {
		if (mIsSwipeToLoadEnabled != isSwipeToLoadEnabled) {
			mIsSwipeToLoadEnabled = isSwipeToLoadEnabled;
			mAdapterWrapper.setLoadItemVisibility(isSwipeToLoadEnabled);
		}
	}

	/** 设置LoadMore Item为加载完成状态, 上拉加载更多完成时调用 */
	public void setLoadMoreFinish() {
		mLoading = false;
		mAdapterWrapper.setLoadItemState(false);
	}

	/** 上拉操作触发时调用的接口 */
	public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
		mListener = loadMoreListener;
	}

	public void setLoadMoreFail(boolean isFinished) {
		mAdapterWrapper.setLoadItemFailState(isFinished);
	}
	public interface LoadMoreListener {
		void onLoad();
	}
}
