package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Menu;
import android.view.MenuItem;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItemOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.UpdateCustomerOfferStatusEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketOfferPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.BasketItemOffersByTypeAdapter;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by wentongwang on 09/03/2017.
 */

public class BasketOffersActivity extends BaseActivity {

    @Inject
    BasketOfferPresenter basketOfferPresenter;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.offers_recycler_view)
    RecyclerView recyclerView;

    private BasketItemOffersByTypeAdapter adapter;

    private List<BasketItemOffer> offers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_offers);
        initPresenters();

        offers = Parcels.unwrap(getIntent().getParcelableExtra(IntentConstants.EXTRA_BASKET_OFFERS));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(this.getResources().getQuantityString(R.plurals.offers_count, offers.size(), offers.size()));
        }

        adapter = new BasketItemOffersByTypeAdapter(this);
        initRecyclerView();

        adapter.setItems(offers);
    }

    private void initPresenters() {
        basketOfferPresenter.setView(new ResourceView<BasketOffer>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<BasketOffer> resourceResponseEvent) {
                // Offer added with success
                if (resourceResponseEvent.getResourceException() == null) {
                    overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
                    setResult(IntentConstants.RESULT_OFFER_SELECTION_OK);
                    BasketOffersActivity.this.supportFinishAfterTransition();

                }
                //Offer cannot be added
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BasketOffersActivity.this);
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.offer_cannot_be_applied);
                    builder.setCancelable(false);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(IntentConstants.RESULT_OFFER_SELECTION_KO);
                            overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
                            BasketOffersActivity.this.supportFinishAfterTransition();
                        }
                    });
                    builder.create().show();

                }
            }
        });
    }

    @Override
    protected boolean isLeftCrossClosable() {
        return true;
    }

    private void initRecyclerView() {
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new BasketItemOffersByTypeAdapter(this);
        recyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));
        recyclerView.setHasFixedSize(true);
        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed() {
        setResult(IntentConstants.RESULT_OFFER_SELECTION_CANCELED);
        super.onBackPressed();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe
    public void onUpdateCustomerOfferStatusEvent(UpdateCustomerOfferStatusEvent event) {
        if (event.isActive()) {
            basketOfferPresenter.activeCustomerOffer(event.getOffer_id());
        } else {
            basketOfferPresenter.inActiveCustomerOffer(event.getId());
        }
    }

}
