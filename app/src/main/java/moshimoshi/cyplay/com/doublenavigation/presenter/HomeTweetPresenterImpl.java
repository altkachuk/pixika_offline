package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.view.HomeTweetView;
import ninja.cyplay.com.apilibrary.domain.interactor.TweetInteractor;
import ninja.cyplay.com.apilibrary.models.business.Tweet;

/**
 * Created by anishosni on 17/06/15.
 */
public class HomeTweetPresenterImpl extends BasePresenter implements HomeTweetPresenter {

    private TweetInteractor tweetInteractor;

    private HomeTweetView homeTweetView;

    HomeTweetPresenter.DisconnectTweetSocketCallback disconnectCallback;

    public HomeTweetPresenterImpl(Context context, TweetInteractor tweetInteractor) {
        super(context);
        this.tweetInteractor = tweetInteractor;
    }

    @Override
    public void setView(HomeTweetView view) {
        homeTweetView = view;
    }

    @Override
    public void initSocket() {
        homeTweetView.showLoading();
        tweetInteractor.initScoket(new TweetInteractor.TweetSocketCallback() {

            @Override
            public void onConnect() {
                Log.e("Tweeet Presenter", "Socket server Connected");
                //hide all old views
                homeTweetView.onConnect();
//                homeTweetView.hideErrorView();
            }

            @Override
            public void onDisconnect() {
                homeTweetView.onDisconnect();
                // show error
//                homeTweetView.showTweetListView();
//                homeTweetView.onError();
            }

            @Override
            public void onConnetionError() {
                Log.e("Tweeet Presenter", "Socket server Connection Error");
                homeTweetView.onConnectionError();
                // show error
//                homeTweetView.showTweetListView();
//                homeTweetView.onError();
            }

            @Override
            public void onReconnecting() {
                homeTweetView.showLoadingReconnect();
                // reconnecting event after loosing connexion with the server
//                homeTweetView.showTweetListView();
//                homeTweetView.showLoadingReconnect();
            }


            @Override
            public void onInitTweets(List<Tweet> tweets) {
                homeTweetView.onTweets(tweets);
                // hide all view in case it is the first tweet after an empty view or error view and reconnection view
//                homeTweetView.hideErrorView();
//                homeTweetView.hideEmpty();
//                homeTweetView.hideLoading();
//                 show the list
//                homeTweetView.showTweetListView();
                // add the tweets list to the list
//                homeTweetView.add_tweets(tweets);
            }

            @Override
            public void onNewTweet(Tweet tweet) {
                homeTweetView.onNewTweet(tweet);
                // hide all view in case it is the first tweet after an empty view or error view and reconnection view
//                homeTweetView.hideErrorView();
//                homeTweetView.hideEmpty();
//                homeTweetView.hideLoading();
//                // show the list in case it was hidden
//                homeTweetView.showTweetListView();
//                // add the tweet to the list`
//                homeTweetView.add_tweet(tweet);
            }

            @Override
            public void onEditedTweet(Tweet tweet) {
                homeTweetView.onTweetEdited(tweet);
                // show the list in case it was hidden
//                homeTweetView.showTweetListView();
//                 edit the tweet in the list
//                homeTweetView.edit_tweet(tweet);
            }

            @Override
            public void onDeletedTweet(String tweetUid) {
                homeTweetView.onTweetDeleted(tweetUid);
                // show the list in case it was hidden
//                homeTweetView.showTweetListView();
                // delete the tweet from the list
//                if (homeTweetView.delete_tweet(tweet_uid))
//                    homeTweetView.showEmpty();
            }

            @Override
            public void onEmptyTweetList() {
                homeTweetView.clearTweets();
                //delete all local tweets and hide all view
//                homeTweetView.clear_tweets();
//                homeTweetView.hideErrorView();
//                homeTweetView.hideLoading();
//                homeTweetView.hideTweetListView();
//                // show empty view
//                homeTweetView.showEmpty();
            }
        });
    }


    @Override
    public void disconnectSocket(DisconnectTweetSocketCallback callback) {
        this.disconnectCallback = callback;
        tweetInteractor.disconnectSocket(new TweetInteractor.DisconnectTweetSocketCallback() {
            @Override
            public void onDisconnect() {
                disconnectCallback.onDisconnect();
            }
        });
    }

    @Override
    public void markTweetAsRead(String uid) {
        tweetInteractor.readTweetEvent(uid);
    }

}
