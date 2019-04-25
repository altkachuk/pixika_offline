package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SellerDashboardTweetViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.business.Tweet;
import ninja.cyplay.com.apilibrary.models.singleton.TweetCacheManager;

/**
 * Created by damien on 13/04/16.
 */
public class SellerDashboardTweetAdapter extends RecyclerView.Adapter<SellerDashboardTweetViewHolder> {

    @Inject
    TweetCacheManager tweetCacheManager;

    private Context context;

    public SellerDashboardTweetAdapter(Context ctx) {
        this.context = ctx;
        PlayRetailApp.get(ctx).inject(this);
    }

    @Override
    public SellerDashboardTweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_tweet_dashboard_seller, parent, false);
        return new SellerDashboardTweetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SellerDashboardTweetViewHolder holder, int position) {
        Tweet tweet = tweetCacheManager.getTweetCache().get(position);
        if (tweet != null) {
            holder.title.setText(StringUtils.capitalize(tweet.title));
            if (tweet.sender != null)
                holder.author.setText(tweet.sender);
            else //TODO remove this
                holder.author.setText("Admin");
            // Date like : 4mn ago
            PrettyTime prettyTime = new PrettyTime();
            if (tweet.date != null) {
                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(prettyTime.format(DateUtils.parseDate(tweet.date)));
            }
            else
                holder.date.setVisibility(View.GONE);
            holder.content.setText(tweet.text);
        }
    }

    @Override
    public int getItemCount() {
        return tweetCacheManager.getTweetCache() != null ? tweetCacheManager.getTweetCache().size() : 0;
    }
}
