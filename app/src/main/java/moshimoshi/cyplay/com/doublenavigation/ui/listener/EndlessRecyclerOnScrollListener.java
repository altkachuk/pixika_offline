package moshimoshi.cyplay.com.doublenavigation.ui.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


/**
 * Created by romainlebouc on 17/08/16.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    //private int visibleThreshold = 21; // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 21;

    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager mGridLayoutManager;

    public EndlessRecyclerOnScrollListener(GridLayoutManager linearLayoutManager, int visibleThreshold) {
        this.mGridLayoutManager = linearLayoutManager;
        this.visibleThreshold=visibleThreshold;
    }

    public EndlessRecyclerOnScrollListener(GridLayoutManager linearLayoutManager) {
        this.mGridLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mGridLayoutManager.getItemCount();
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public void clear(){
        previousTotal = 0; // The total number of items in the dataset after the last load
        loading = true; // True if we are still waiting for the last set of data to load.
        firstVisibleItem= 0;
        visibleItemCount= 0;
        totalItemCount = 0;
        current_page = 1;
    }
    public abstract void onLoadMore(int current_page);
}