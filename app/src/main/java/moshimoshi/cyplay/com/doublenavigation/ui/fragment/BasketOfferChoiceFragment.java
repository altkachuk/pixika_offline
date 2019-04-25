package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerOffersAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;


/**
 * Created by romainlebouc on 30/11/2016.
 */

public class BasketOfferChoiceFragment extends ResourceBaseFragment<List<CustomerOfferState>> {

    @Inject
    CustomerContext customerContext;

    @Inject
    BasketPresenter basketPresenter;

    @Inject
    GetCustomerInfoPresenter getCustomerInfoPresenter;

    @BindView(R.id.basket_offer_choice_recyclerview)
    RecyclerView deliveryChoiceRecyclerView;

    private CustomerOffersAdapter customerOffersAdapter;

    private List<CustomerOfferState> customerOfferStates;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_offer_choice, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        if (this.getCachedResource() == null) {
            loadResource();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        customerOffersAdapter = new CustomerOffersAdapter(this.getContext(), basketPresenter);
        customerOffersAdapter.setOffers(customerContext.getCustomer().getOffers().getItems());
        initRecyclerView();
        getCustomerInfoPresenter.setView(new ResourceView<Customer>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Customer> resourceResponseEvent) {
                if (resourceResponseEvent != null) {
                    Customer customer = resourceResponseEvent.getResource();
                    if (customer != null && customer.getOffers() != null) {

                        setResource(customer.getOffers().getItems());
                        // to notify resource Fragment
                        bus.post(new ResourceResponseEvent<>(customer.getOffers().getItems(), null, EResourceType.OFFERS));
                    }
                }
            }
        } );
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        // optimization
        deliveryChoiceRecyclerView.setHasFixedSize(false);
        // set layout
        deliveryChoiceRecyclerView.setLayoutManager(layoutManager);
        deliveryChoiceRecyclerView.setAdapter(customerOffersAdapter);

        // Add Decorator
        deliveryChoiceRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext()));
        // OnClick action
        //posRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new CatalogCategoriesFragment.CategoryClick()));
    }



    @Override
    public void onResourceResponseEvent(ResourceResponseEvent<List<CustomerOfferState>> resourceResponseEvent) {
        super.onResourceResponse(resourceResponseEvent);
    }

    @Override
    public void onResourceRequestEvent(ResourceRequestEvent<List<CustomerOfferState>> resourceRequestEvent) {
        super.onResourceRequest(resourceRequestEvent);
    }

    @Override
    protected void setResource(List<CustomerOfferState> offers) {
        customerOfferStates = offers;
        customerOffersAdapter.setOffers(offers);
    }

    @Override
    public List<CustomerOfferState> getCachedResource() {
        return customerOfferStates;
    }

    @Override
    public void loadResource() {
        if ( customerContext.getCustomer() !=null){
            getCustomerInfoPresenter.getCustomerOffer(customerContext.getCustomer().getId());
        }
    }


}
