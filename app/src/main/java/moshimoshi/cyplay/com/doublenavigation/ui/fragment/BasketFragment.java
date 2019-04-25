package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketComment;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItemOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetailsEmail;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_Form;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormSection;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EBasketItemOfferStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ECustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferType;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.BasketItemWithStock;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.PurchaseCollectionBasketItems;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketUpdatedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketUpdatingEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.CustomerLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.BasketActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.BasketOffersActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerAssociationFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.PopupCustomerSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerDashboardActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.BasketExpandableAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.WSErrorDialog;
import moshimoshi.cyplay.com.doublenavigation.utils.ActivityHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.JsonObjectHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.view.BasketView;
import moshimoshi.cyplay.com.doublenavigation.view.RemoveBasketItemsView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.utils.Constants;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_BASKET_OFFERS_CHOICE_RESULT;


/**
 * Created by damien on 01/04/16.
 */
public class BasketFragment extends ResourceBaseFragment<Basket> implements BasketView, RemoveBasketItemsView {

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    BasketPresenter basketPresenter;

    @Inject
    GetCustomerInfoPresenter getCustomerInfoPresenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.basket_items_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.empty_basket_warning)
    TextView emptyBasketWarning;

    @BindView(R.id.basket_comment_view)
    RelativeLayout basketCommentView;

    @BindView(R.id.basket_comment)
    EditText basketComment;

    @BindView(R.id.basket_upgrading)
    View basketUpgrading;

    @BindView(R.id.basket_upgrading_message)
    TextView basketUpgradingMessage;

    @BindView(R.id.basket_summary)
    BasketSummaryLayout basketSummary;

    @BindView(R.id.basket_paiement)
    TextView basketPaiement;

    @BindView(R.id.basket_offers_title)
    TextView tvBasketOffersTitle;

    @BindView(R.id.basket_offers_count)
    TextView tvBasketOffersCount;

    @BindView(R.id.basket_all_offers_count)
    TextView tvBasketAllOffersCount;

    @BindView(R.id.offers_and_tickets_choice)
    View offersContainer;

    private BasketExpandableAdapter adapter;
    private Basket basket;

    // The basket should be displayed
    private BasketItem basketItemToTreat;
    private BasketItem basketItemOnModification;
    private List<BasketItemWithStock> basketItemWithStockList = new ArrayList<>();
    private List<BasketItemOffer> offers = new ArrayList<>();

    private AlertDialog basketDialog;

    private boolean loadingForResult = false;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Update design
        updateDesign();
        basketPresenter.setView(this);
        basketPresenter.setRemoveBasketItemsView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(new ProgressBar(this.getContext()));
        basketItemToTreat = Parcels.unwrap(this.getActivity().getIntent().getParcelableExtra(IntentConstants.EXTRA_BASKET_ITEM));

        getCustomerInfoPresenter.setView(new ResourceView<Customer>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Customer> resourceResponseEvent) {
                if (EResourceType.CUSTOMER == resourceResponseEvent.getEResourceType()) {
                    if (resourceResponseEvent.getResourceException() == null) {
                        loadingForResult = false;
                        bus.post(new CustomerLoadedEvent(resourceResponseEvent.getResource(), false));
                        basketPresenter.linkSellerBasketToCustomer(resourceResponseEvent.getResource().getId());
                    } else {
                        //show error dialog
                        WSErrorDialog.showWSErrorDialog(getContext(),
                                getActivity().getString(R.string.basket_associate_error) + "\n" + resourceResponseEvent.getResourceException().toString(),
                                new WSErrorDialog.DialogButtonClickListener() {
                                    @Override
                                    public void retry() {
                                        getCustomerInfoPresenter.getCustomerInfo(customerContext.getCustomerId());
                                    }

                                    @Override
                                    public void cancel() {
                                        getActivity().supportFinishAfterTransition();
                                        getActivity().overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
                                    }
                                });

                    }

                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.basket, menu);
        MenuItem removeItem = menu.findItem(R.id.clear_basket);
        CompatUtils.setDrawableTint(removeItem.getIcon(), ContextCompat.getColor(getContext(), R.color.actionBarItem));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((BasketActivity) getActivity()).finishBasket();
                break;
            case R.id.clear_basket:
                onRemoveIconClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResourceResponseEvent(ResourceResponseEvent<Basket> resourceResponseEvent) {
        if (EResourceType.BASKET == resourceResponseEvent.getEResourceType()) {
            hideBasketUpdating();
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    public void onResourceRequestEvent(ResourceRequestEvent<Basket> resourceRequestEvent) {
        if (EResourceType.BASKET == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Override
    protected void setResource(Basket basket) {
        if (basket != null) {
            this.basket = basket;
        }
    }

    @Override
    public Basket getCachedResource() {
        // We do not cache basket, each time we access it, we load it from the server
        return basket;
    }

    @Override
    public void loadResource() {
        if (basketItemToTreat != null) {
            basketPresenter.addUpdateOrDeleteBasketItem(basketItemToTreat);
        } else {
            basketPresenter.getBasket();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.getCachedResource() != null && !loadingForResult) {
            hideBasketUpdating();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getCachedResource() == null) {
            loadResource();
        }
    }

    public BasketPresenter getBasketPresenter() {
        return basketPresenter;
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
        if (basketPresenter.isCustomerBasket()) {
            Basket basket = basketPresenter.getCachedBasket();

            if (basket != null) {
                basketPaiement.setEnabled(basket.getBasketItemsCount() > 0);
                basketPaiement.setText(getResources().getString(R.string.add_comment_button));

                initOffers(basket);
            } else {
                basketPaiement.setEnabled(false);
            }

            tvBasketOffersTitle.setText(getContext().getString(R.string.customer_offers));
            tvBasketOffersCount.setText(String.valueOf(getActiveOffersCount(offers)));
            tvBasketAllOffersCount.setText(String.valueOf(offers.size()));
            offersContainer.setVisibility(View.VISIBLE);
        } else {
            Basket basket = basketPresenter.getCachedBasket();

            if (basket != null) {
                basketPaiement.setEnabled(basket.getBasketItemsCount() > 0);
                basketPaiement.setText(getResources().getString(R.string.next));
            } else {
                basketPaiement.setEnabled(false);
            }

            offersContainer.setVisibility(View.GONE);
        }

        basketCommentView.setVisibility(View.GONE);

    }


    private void initOffers(Basket basket) {
        offers.clear();

        for (CustomerOfferState offer : customerContext.getCustomer().getOffers().getItems()) {
            ECustomerOfferState eCustomerOfferState = offer.getECustomerOfferState();
            if (eCustomerOfferState != null) {
                switch (eCustomerOfferState) {
                    case USED:
                        /*offers.add(new BasketItemOffer(offer.getOffer(),
                                basket.getBasketItemId(offer.getOffer()),
                                EBasketItemOfferStatus.AUTOMATIC));*/
                        break;
                    case AVAILABLE:
                    case IN_BASKET:
                        String basketItemId = basket.getBasketItemId(offer.getOffer());
                        offers.add(new BasketItemOffer(offer.getOffer(),
                                basket.getBasketItemId(offer.getOffer()),
                                basketItemId == null ? EBasketItemOfferStatus.AVAILABLE : EBasketItemOfferStatus.CHOSEN));
                }
            }
        }

        for (BasketOffer basketOffer : basket.getBasket_offers()) {
            if (EOfferType.BASKET.equals(basketOffer.getOffer().getType())) {
                offers.add(new BasketItemOffer(basketOffer.getOffer(), basketOffer.getId(), EBasketItemOfferStatus.AUTOMATIC));
            }
        }
    }

    private int getActiveOffersCount(List<BasketItemOffer> iOffers) {
        int count = 0;
        int size = iOffers.size();
        for (int i = 0; i < size; i++) {
            if (EBasketItemOfferStatus.CHOSEN.equals(iOffers.get(i).getStatus()) ||
                    EBasketItemOfferStatus.AUTOMATIC.equals(iOffers.get(i).getStatus())) {
                count++;
            }
        }
        return count;
    }

    protected void initRecyclerView() {

        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new BasketExpandableAdapter(basketPresenter);
        recyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);
    }


    private boolean isValidCustomerFields() {
        String formMode = "customerUpdateForm";
        PR_Form form = FormHelper.getFormFromKey(getActivity().getApplicationContext(), formMode, configHelper);

        if (form != null && form.getSectionsDescriptors() != null) {
            //sections = new ArrayList<>();
            for (int i = 0; i < form.getSectionsDescriptors().size(); i++) {
                // Set form's Section to the view
                String sectionTag = form.getSectionsDescriptors().get(i).getTag();
                PR_FormSection formSection = FormHelper.getSectionForTag(getActivity().getApplicationContext(), sectionTag, configHelper);

                for (PR_FormDescriptor formDescriptor : formSection.getRowsDescriptors()) {
                    if (formDescriptor.getPermissions() > 0) {
                        String tag = formDescriptor.getTag();

                        String value = JsonObjectHelper.getValue(customerContext.getCustomer(), tag);

                        if (value == null || value == "0" || value.length() == 0)
                            return false;
                    }
                }
            }
        }

        return true;
    }

    private void getCustomerValueByName() {

    }


    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------


    @Subscribe
    public void onBasketUpdatingEvent(BasketUpdatingEvent basketUpdatingEvent) {
        this.basketItemOnModification = basketUpdatingEvent.getBasketItem();
        showBasketUpdating(this.getString(R.string.basket_updating));
    }

    @Override
    public void onBasketItemsStocksChecked(boolean sucess) {

        hideBasketUpdating();
        if (sucess) {
            adapter.notifyDataSetChanged();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle(R.string.warning);
            builder.setMessage(R.string.basket_stock_failed_checked_warning);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BasketFragment.this.getActivity().supportFinishAfterTransition();
                }
            });
            builder.create().show();
        }

    }

    @Override
    public void onValidateCartResponse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.thank_you);
        builder.setMessage(R.string.basket_complete_warning);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), SellerDashboardActivity.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    @Subscribe
    public void onBasketUpdatedEvent(BasketUpdatedEvent basketUpdatedEvent) {
        // Ignore
        if (BasketUpdatedEvent.EBasketPersonType.SELLER.equals(basketUpdatedEvent.geteBasketPersonType())
                && customerContext.isCustomerIdentified()
                ) {

        } else {
            Basket basket = basketUpdatedEvent.getBasket();
            if (basket != null) {
                basketItemWithStockList.clear();
                for (BasketItem basketItem : basket.getItems()) {
                    basketItemWithStockList.add(new BasketItemWithStock(basketItem));
                }

                if (!basketItemWithStockList.isEmpty()) {
                    basketPresenter.checkBasketStock(basketItemWithStockList);
                    this.showBasketUpdating(this.getString(R.string.basket_checking_stocks));
                }
                adapter.setItemOnModification(basketItemOnModification);

                List<PurchaseCollectionBasketItems> items = PurchaseCollectionBasketItems.getPurchaseCollectionBasketItems(basketItemWithStockList);
                if (items == null || items.isEmpty()) {
                    emptyBasketWarning.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyBasketWarning.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.setItems(items);
                basketSummary.setItem(basket, false);
            }
            updateDesign();
        }
    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<Basket> resourceResponseEvent) {
        hideBasketUpdating();
        if (EResourceType.BASKET == resourceResponseEvent.getEResourceType()) {
            this.onResourceResponseEvent(resourceResponseEvent);
        }
    }

    @OnClick(R.id.basket_paiement)
    public void onPayBasket() {
        if (this.getContext() != null) {
            Shop currentShop = configHelper.getCurrentShop();
            if (basketPresenter.isCustomerBasket()
                    && customerContext != null
                    && customerContext.getCustomerId() != null
                    && currentShop != null
                    && basket.getItems() != null) {

                // TODO: need to test
                boolean isValid = isValidCustomerFields();

                if (isValid) {
                    CustomerDetailsEmail customerDetailsEmail = customerContext.getCustomer().getDetails().getEmail();
                    String email = customerDetailsEmail.getFirstEmail();

                    if (basket.hasDeliveries() && (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                        builder.setTitle(R.string.warning);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.email_needed_for_payment_message);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else if (basketCommentView.getVisibility() == View.GONE) {
                        recyclerView.setVisibility(View.GONE);
                        emptyBasketWarning.setVisibility(View.GONE);
                        offersContainer.setVisibility(View.GONE);
                        basketComment.setText("");
                        basketCommentView.setVisibility(View.VISIBLE);
                        basketPaiement.setText(getResources().getString(R.string.validate_button));
                    } else {
                        ActivityHelper.hideKeyboard(getActivity());
                        BasketComment basket = new BasketComment();
                        String comment = basketComment.getText().toString();
                        basket.setComment(comment);
                        basketPresenter.validateCart(customerContext.getCustomerId(), basket);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    basketDialog = builder.setTitle(R.string.needed_action)
                            .setMessage(R.string.update_empty_fields_explanation)
                            .setPositiveButton(R.string.update_customer, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // IF SELLER BASKET
                                    Intent intent = new Intent(BasketFragment.this.getContext(), CustomerFormActivity.class);
                                    Customer customer = customerContext.getCustomer();
                                    intent.putExtra(IntentConstants.EXTRA_CUSTOMER, Parcels.wrap(customer));
                                    intent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_UPDATE_KEY);
                                    BasketFragment.this.startActivity(intent);
                                }
                            })

                            .create();
                    basketDialog.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                basketDialog = builder.setTitle(R.string.needed_action)
                        .setMessage(R.string.link_basket_to_customer_explanation)
                        .setPositiveButton(R.string.link_basket_to_an_existing_customer, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // IF SELLER BASKET
                                Intent it = new Intent(BasketFragment.this.getContext(), PopupCustomerSearchActivity.class);
                                BasketFragment.this.startActivityForResult(it, IntentConstants.REQUEST_ASSOCIATE_CUSTOMER);
                                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
                                showBasketUpdating(BasketFragment.this.getString(R.string.basket_linking));
                                loadingForResult = true;
                            }
                        })
						.setNegativeButton(R.string.create_new_customer, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(BasketFragment.this.getActivity(), CustomerAssociationFormActivity.class);
                                intent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_CREATE_KEY);
                                BasketFragment.this.startActivityForResult(intent, IntentConstants.REQUEST_CREATE_CUSTOMER);
                                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
                                showBasketUpdating(BasketFragment.this.getString(R.string.basket_linking));
                                loadingForResult = true;
                            }
                        })

                        .create();
                basketDialog.show();
            }

        }
    }

    @OnClick(R.id.offers_and_tickets_choice)
    public void onChooseOffersAndTickers() {
        Intent intent = new Intent(this.getContext(), BasketOffersActivity.class);
        intent.putExtra(IntentConstants.EXTRA_BASKET_OFFERS, Parcels.wrap(offers));
        this.startActivityForResult(intent, EXTRA_BASKET_OFFERS_CHOICE_RESULT);

        loadingForResult = true;
        showBasketUpdating(null);
        this.getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
    }


    private void showBasketUpdating(String message) {
        basketUpgrading.setVisibility(View.VISIBLE);
        basketUpgradingMessage.setText(message);

    }

    private void hideBasketUpdating() {
        basketUpgrading.setVisibility(View.GONE);
    }

    @Override
    public void onStockResponse(final BasketItem basketItem, ResourceResponseEvent<Product> resourceResponseEvent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EXTRA_BASKET_OFFERS_CHOICE_RESULT) {
            switch (resultCode) {
                case IntentConstants.RESULT_OFFER_SELECTION_OK:
                    basketPresenter.getBasket();
                    break;
                case IntentConstants.RESULT_OFFER_SELECTION_KO:
                case IntentConstants.RESULT_OFFER_SELECTION_CANCELED:
                    loadingForResult = false;
                    break;
            }
        } else if (requestCode == IntentConstants.REQUEST_ASSOCIATE_CUSTOMER) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getCustomerInfoPresenter.getCustomerInfo(customerContext.getCustomerId());
                    break;
                case Activity.RESULT_CANCELED:
                    loadingForResult = false;
                    break;

            }
        } else if (requestCode == IntentConstants.REQUEST_CREATE_CUSTOMER) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getCustomerInfoPresenter.getCustomerInfo(customerContext.getCustomerId());
                    break;
                case Activity.RESULT_CANCELED:
                    loadingForResult = false;
                    break;
            }
        }
    }

    public void onRemoveIconClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        basketDialog = builder.setTitle(R.string.delete)
                .setMessage(adapter.getSelectedBasketItems().size() > 0 ?
                        R.string.want_to_remove_selected_item_in_basket :
                        R.string.want_to_clear_basket)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (adapter.getSelectedBasketItems().size() > 0) {
                            removeSelectedItems();
                        } else {
                            clearBasket();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        basketDialog.show();
    }

    private void removeSelectedItems() {
        showBasketUpdating(this.getString(R.string.basket_updating));
        basketPresenter.initBasketItemToRemove(adapter.getSelectedBasketItems());
        basketPresenter.removeBasketItems();
    }

    private void clearBasket() {
        basketPresenter.clearBasket();
    }

    @Override
    public void onRemoveSuccess() {
        adapter.clearSelectedItems();
    }

    //multi remove item error
    @Override
    public void onRemoveBasketItemsError(ResourceException e) {
        hideBasketUpdating();
        //show error dialog
        WSErrorDialog.showWSErrorDialog(getContext(),
                this.getString(R.string.clear_basket_error) + "\n" + e.toString(),
                new WSErrorDialog.DialogButtonClickListener() {
                    @Override
                    public void retry() {
                        basketPresenter.removeBasketItems();
                    }

                    @Override
                    public void cancel() {

                    }
                });
    }

    //clear basket error
    @Override
    public void onClearError(ResourceException e) {
        hideBasketUpdating();
        //show error dialog
        WSErrorDialog.showWSErrorDialog(getContext(),
                this.getString(R.string.clear_basket_error) + "\n" + e.toString(),
                new WSErrorDialog.DialogButtonClickListener() {
                    @Override
                    public void retry() {
                        clearBasket();
                    }

                    @Override
                    public void cancel() {

                    }
                });
    }
}
