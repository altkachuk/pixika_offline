package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SellerDashboardTweetAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;

/**
 * Created by damien on 13/04/16.
 */
public class SellerDashboardDirectMessages extends BaseFragment {

    @BindView(R.id.catalog_items_recycler_view)
    RecyclerView recyclerView;

    private SellerDashboardTweetAdapter adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_dashboard_direct_messages, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Direct Messages
//        adapter = new SellerDashboardTweetAdapter(getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ArrayList<Tweet> tweets = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//
//            Tweet tweet = new Tweet();
//            tweet.setTitle("Tweet[" + i + "]");
//            tweet.setContent("Artisan pour-over kickstarter austin pitchfork gentrify, gluten-free jean shorts keytar. Marfa chia austin distillery.");
//            tweet.setDate("1 jan. 1970");
//
//            tweets.add(tweet);
//        }
//        adapter.addTweets(tweets);

        initRecyclerView();
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
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
    }

}
