package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.presenter.HomeTweetPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SellerDashboardTweetAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.view.HomeTweetView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.Tweet;
import ninja.cyplay.com.apilibrary.models.singleton.TweetCacheManager;

/**
 * Created by damien on 13/04/16.
 */
public class SellerDashboardTweetsFragment extends BaseFragment implements HomeTweetView {

    @Inject
    HomeTweetPresenter homeTweetPresenter;

    @Inject
    TweetCacheManager tweetCacheManager;

    @BindView(R.id.tweet_loading_view)
    LoadingView stateLoadingView;

    @BindView(R.id.tweet_reconnecting_view)
    View reconnectingView;

    @BindView(R.id.catalog_items_recycler_view)
    RecyclerView recyclerView;

    private SellerDashboardTweetAdapter adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_dashboard_tweets, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SellerDashboardTweetAdapter(getContext());
        //set presenter's view
        homeTweetPresenter.setView(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fillStocks Recycler
        initRecyclerView();
        // Get Tweets from cache
//        adapter.setTweets(tweetCacheManager.getTweetCache());
    }

    @Override
    public void onResume() {
        super.onResume();
        // fillStocks the tweet list
        homeTweetPresenter.initSocket();
    }

    @Override
    public void onPause() {
        super.onPause();
        homeTweetPresenter.disconnectSocket(new HomeTweetPresenter.DisconnectTweetSocketCallback(){
            @Override
            public void onDisconnect() {
                //do nothing
            }
        });
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                getResources().getDimension(R.dimen.contextual_small_margin),
                getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing, true, true));
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void onConnect() {
        reconnectingView.setVisibility(View.GONE);
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
    }

    @Override
    public void onConnectionError() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onTweets(List<Tweet> tweets) {
//        adapter.addTweets(tweets);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNewTweet(Tweet newTweet) {
//        adapter.addTweet(newTweet);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTweetEdited(Tweet editedTweet) {
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTweetDeleted(String tweetUid) {
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearTweets() {
        adapter.notifyDataSetChanged();
//        adapter.clearTweets();
    }

    @Override
    public void showLoading() {
        // If cache we show 'reconnect'
        if (tweetCacheManager.getTweetCache() != null && tweetCacheManager.getTweetCache().size() > 0) {
            reconnectingView.setVisibility(View.VISIBLE);
            stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
            adapter.notifyDataSetChanged();
        }
        else { // loading view
            reconnectingView.setVisibility(View.GONE);
            stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
    }

    @Override
    public void showLoadingReconnect() {
        reconnectingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
    }

}
