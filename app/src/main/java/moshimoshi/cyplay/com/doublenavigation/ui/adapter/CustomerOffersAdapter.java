package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.OfferViewHolder;

import moshimoshi.cyplay.com.doublenavigation.view.BasketView;


/**
 * Created by damien on 13/04/16.
 */
public class CustomerOffersAdapter extends ItemAdapter<CustomerOfferState, OfferViewHolder> {

    private List<CustomerOfferState> offers;
    private BasketPresenter basketPresenter;

    public CustomerOffersAdapter(Context ctx, BasketPresenter basketPresenter) {
        super(ctx);
        PlayRetailApp.get(ctx).inject(this);
        this.basketPresenter = basketPresenter;
    }

    public void setOffers(List<CustomerOfferState> offers) {
        this.offers = offers;
        notifyDataSetChanged();
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_offer, parent, false);
        OfferViewHolder holder = new OfferViewHolder(v, basketPresenter);
        basketPresenter.setView(new BasketView() {
            @Override
            public void onStockResponse(final BasketItem basketItem, ResourceResponseEvent<Product> resourceResponseEvent) {

            }

            @Override
            public void onBasketItemsStocksChecked(boolean success) {

            }

            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Basket> resourceResponseEvent) {

            }

            @Override
            public void onValidateCartResponse() {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        CustomerOfferState customerOfferState = offers.get(position);
        holder.setItem(customerOfferState);


    }

    public BasketPresenter getBasketPresenter() {
        return basketPresenter;
    }

    @Override
    public int getItemCount() {
        return offers != null ? offers.size() : 0;
    }


}
