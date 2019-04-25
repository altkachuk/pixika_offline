package moshimoshi.cyplay.com.doublenavigation.ui.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

/**
 * Created by romainlebouc on 20/04/16.
 */
public abstract class BothWaysEndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = BothWaysEndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.

    private int visibleThreshold = 10; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private int bottom_page = 0;
    private int top_page = 0;

    private boolean notifiyDisabledUntilEndofScroll = false;
    protected boolean notifySelectedDate = true;

    private LinearLayoutManager mLinearLayoutManager;

    public BothWaysEndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            notifiyDisabledUntilEndofScroll = false;
            this.setNotifySelectedDate(true);
        }
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mLinearLayoutManager != null && !notifiyDisabledUntilEndofScroll) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();

            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount >= previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                loading = true;
                onLoadMore(top_page);
            }

            if (!loading && firstVisibleItem < visibleThreshold && dy < 0) {
                bottom_page--;
                loading = true;
                onLoadMore(bottom_page);
            }
        }

    }

    public abstract void onLoadMore(int current_page);

    public void setNotifiyDisabledUntilEndofScroll() {
        notifiyDisabledUntilEndofScroll = true;
    }

    public abstract void setNotifySelectedDate(boolean notifySelectedDate);
}