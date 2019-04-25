package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 21/04/16.
 */

public class LoadingView extends RelativeLayout {

    public enum LoadingState {
        LOADING, LOADED, NO_RESULT, FAILED
    }

    public View waitingView;
    public View reloadView;
    public View loadedView;
    public View noResultView;

    private LoadingState loadingState = LoadingState.LOADED;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int loadingRef;
    private int loadedRef;
    private int failedRef;
    private int noresultRef;

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
        loadingRef = array.getResourceId(R.styleable.LoadingView_loading_ref, -1);
        loadedRef = array.getResourceId(R.styleable.LoadingView_loaded_ref, -1);
        failedRef = array.getResourceId(R.styleable.LoadingView_failed_ref, -1);
        noresultRef = array.getResourceId(R.styleable.LoadingView_no_result_ref, -1);
        array.recycle();
    }

    public void setLoadingState(LoadingState loadingState) {
        this.loadingState = loadingState;
        this.refresh();
    }

    public LoadingState getLoadingState() {
        return loadingState;
    }

    private void refresh() {
        switch (this.loadingState) {
            case NO_RESULT:
                this.setViewsVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE);
                break;
            case LOADING:
                this.setViewsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
                break;
            case LOADED:
                this.setViewsVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
                break;
            case FAILED:
                this.setViewsVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE);
                break;
        }
    }

    private void setViewsVisibility(int loadedVisibity, int laodingVisibility, int failedVisibility, int noResultVisibility) {
        loadedView = this.findViewById(loadedRef);
        waitingView = this.findViewById(loadingRef);
        reloadView = this.findViewById(failedRef);
        noResultView = this.findViewById(noresultRef);
        if (loadedView != null) {
            if (loadedVisibity == View.VISIBLE && loadedView.getVisibility() != View.VISIBLE) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(500);
                loadedView.startAnimation(fadeIn);
            }
            loadedView.setVisibility(loadedVisibity);
        }
        if (waitingView != null) {
            waitingView.setVisibility(laodingVisibility);
        }
        if (reloadView != null) {
            reloadView.setVisibility(failedVisibility);
        }
        if (noResultView != null) {
            noResultView.setVisibility(noResultVisibility);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        loadedView = this.findViewById(loadedRef);
        waitingView = this.findViewById(loadingRef);
        reloadView = this.findViewById(failedRef);
        noResultView = this.findViewById(noresultRef);
        this.refresh();
    }

}
