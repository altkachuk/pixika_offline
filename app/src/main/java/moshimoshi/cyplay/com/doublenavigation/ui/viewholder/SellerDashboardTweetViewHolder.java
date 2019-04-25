package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by damien on 13/04/16.
 */
public class SellerDashboardTweetViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.tweet_title)
    public TextView title;

    @Nullable
    @BindView(R.id.tweet_author)
    public TextView author;

    @Nullable
    @BindView(R.id.tweet_date)
    public TextView date;

    @Nullable
    @BindView(R.id.tweet_content)
    public TextView content;


    public SellerDashboardTweetViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

}
