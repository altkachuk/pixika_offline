package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EFeature;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPriceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.ThreeSixtyStocks;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSpecificationSelectedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ItemPurchaseConfirmationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductSkuSelectorAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.SpecificationSelector;

/**
 * Created by romainlebouc on 19/12/2016.
 */

public class ProductSelectorFragment extends ResourceBaseFragment<Product> {

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Nullable
    @BindView(R.id.specification_selector)
    SpecificationSelector specificationSelector;

    @Nullable
    @BindView(R.id.product_sku_spinner)
    Spinner skuSpinner;

    @Nullable
    @BindView(R.id.product_sku_spinner_container)
    LinearLayout productSkuSpinnerContainer;

    @Nullable
    @BindView(R.id.product_price)
    TextView price;

    @Nullable
    @BindView(R.id.crossed_product_price)
    TextView crossedPrice;

    @Nullable
    @BindView(R.id.add_to_basket_container)
    LinearLayout addToBasketContainer;

    @Nullable
    @BindView(R.id.add_to_basket_button)
    TextView addToBasketButton;

    private ThreeSixtyStocks threeSixtyStocks;
    private Sku selectedSku;
    private Product product;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.updateBasketActivationDisplay(false);
        addToBasketContainer.setActivated(configHelper.isFeatureActivated(EFeature.BASKET));
        price.setActivated(configHelper.isFeatureActivated(EFeature.BASKET));
        addToBasketButton.setActivated(configHelper.isFeatureActivated(EFeature.BASKET));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_selector, container, false);
    }

    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Product> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        } else if (EResourceType.PRODUCT_STOCK == resourceResponseEvent.getEResourceType()) {
            if (configHelper.shouldDisplayBasketFeatures(sellerContext, customerContext)) {
                threeSixtyStocks = new ThreeSixtyStocks(selectedSku, configHelper);
                updateBasketActivationDisplay(threeSixtyStocks.getPurchaseCollectionModeStocks() != null
                        && !threeSixtyStocks.getPurchaseCollectionModeStocks().isEmpty());
            }
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crossedPrice.setPaintFlags(crossedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Product> resourceRequestEvent) {
        if (EResourceType.PRODUCT == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public Product getCachedResource() {
        return ((ResourceActivity<Product>) this.getActivity()).getResource();
    }

    @Override
    public void loadResource() {
        ((AbstractProductActivity) this.getActivity()).loadResource();
    }

    @Override
    protected void setResource(Product resourceProduct) {
        // re-get sku selected to re-create spinner pre-selected
        if (getActivity() != null && getActivity() instanceof AbstractProductActivity) {
            // get Sku if we changed view and come back on it
            selectedSku = ((AbstractProductActivity) getActivity()).getSelectedSku();
        }

        // get product
        if (resourceProduct != null) {
            this.product = resourceProduct;
            if (product.getSelectors() != null && !product.getSelectors().isEmpty()) {
                specificationSelector.setVisibility(View.VISIBLE);
                productSkuSpinnerContainer.setVisibility(View.GONE);

                if ( selectedSku ==null){
                    int position = ((AbstractProductActivity) this.getActivity()).getInitialSelectedSkuPosition();
                    if (position >=0){
                        selectedSku = product.getSkus().get(position);
                    }
                }

                if (selectedSku == null) {
                    selectedSku = Product.getFirstSkuMatchingCustomer(product.getSkus(), customerContext.getCustomer());
                }

                if (selectedSku == null) {
                    selectedSku = Product.getFirstSkuMatchingSpecification(product, configHelper.getProductConfig().getSpecifications().getDefault_selection());
                }

                specificationSelector.fill(product, selectedSku);
            } else {
                specificationSelector.setVisibility(View.GONE);
                productSkuSpinnerContainer.setVisibility(View.VISIBLE);
                initSkuSelector();
            }
        }
    }

    private void initSkuSelector() {
        // Display a sku selector
        ProductSkuSelectorAdapter spinnerArrayAdapter = new ProductSkuSelectorAdapter(getContext(), product);
        // Set adapter
        skuSpinner.setAdapter(spinnerArrayAdapter);
        skuSpinner.setOnItemSelectedListener(new SkuSelectedListener());
        skuSpinner.setEnabled(!(product.getSkus() != null && product.getSkus().size() <= 1));
    }

    private class SkuSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position < product.getSkus().size()) {
                Sku selectedSku = product.getSkus().get(position);
                bus.post(new ProductSelectedSkuChangeEvent(selectedSku));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    @Subscribe
    public void onSpecSelectedEvent(ProductSpecificationSelectedEvent event) {
        if (event != null && event.getSpecification() != null) {
            // all spec until the one that change
            List<ProductSpecification> selectedSpecs = new ArrayList<>();
            for (int i = 0; i < specificationSelector.getSize(); i++) {
                // get all specs until changes
                selectedSpecs.add((specificationSelector.getSpecSelectorViewAt(i)).getSelectedSpec());
                if (i == event.getLevel()) {
                    // update spec info value
                    // we got all previous specs
                    List<Sku> skusWithSpecifications = product.getSkusWithSpecifications(selectedSpecs);
                    if (specificationSelector.getSize() > i + 1) {
                        // We change a spec (not the last one) so we update the next in list
                        (specificationSelector.getSpecSelectorViewAt(i + 1)).setSpecifications(product.getSpecsForLevelFromSkus(skusWithSpecifications, i + 1));
                        // set default
                        (specificationSelector.getSpecSelectorViewAt(i + 1)).setDefaultSelection(product, selectedSku);
                    } else if (i + 1 == specificationSelector.getSize()) {
                        // Last spec changed, we update the sku
                        if (skusWithSpecifications.size() > 0) {
                            bus.post(new ProductSelectedSkuChangeEvent(skusWithSpecifications.get(0)));
                        }
                    }
                    break;
                }
            }
        }
    }


    @Subscribe
    public void onProductSelectedSkuChangeEvent(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        this.selectedSku = productSelectedSkuChangeEvent.getSku();
        // on sku changes
        if (selectedSku != null) {
            updateSkuPrices();   // We disable button and we reactivate it when we get stock
            updateBasketActivationDisplay(false);
        }
    }

    private void updateBasketActivationDisplay(boolean isBasketActivated) {
        addToBasketContainer.setEnabled(isBasketActivated);
        addToBasketButton.setVisibility(isBasketActivated ? View.VISIBLE : View.GONE);
    }

    private void updateSkuPrices() {
        if (selectedSku != null) {
            addToBasketContainer.setVisibility(View.VISIBLE);
            Double skuCrossedPrice = null;
            Double skuPrice = null;
            if (selectedSku.getCurrent_shop_availability() != null) {
                Ska currentShopAvailability = selectedSku.getCurrent_shop_availability();
                if (customerContext != null
                        && customerContext.getCustomer() != null
                        && customerContext.getCustomer().getLoyalty() != null) {
                    skuPrice = currentShopAvailability.getPrice(configHelper.getProductConfig().getPrices(),
                            EPriceType.FINAL,
                            customerContext.getCustomer().getLoyalty().getActiveProgram());
                    skuCrossedPrice = currentShopAvailability.getPrice(configHelper.getProductConfig().getPrices(),
                            EPriceType.BEFORE_DISCOUNT,
                            customerContext.getCustomer().getLoyalty().getActiveProgram());
                } else {
                    skuPrice = currentShopAvailability.getPrice();
                    skuCrossedPrice = currentShopAvailability.getCrossed_price();
                }
            }
            price.setText(configHelper.formatPrice(skuPrice));
            crossedPrice.setText(configHelper.formatPrice(skuCrossedPrice));
            crossedPrice.setVisibility(skuCrossedPrice == null ? View.GONE : View.VISIBLE);
        }
    }

    @Nullable
    @OnClick(R.id.add_to_basket_container)
    void onAddToBasketButton() {
        Intent intent = new Intent(this.getActivity(), ItemPurchaseConfirmationActivity.class);
        intent.putExtra(IntentConstants.EXTRA_SKU, Parcels.wrap(selectedSku));
        intent.putExtra(IntentConstants.EXTRA_PRODUCT, Parcels.wrap(product));
        intent.putExtra(IntentConstants.EXTRA_PURCHASE_COLLECTION_360_STOCKS, Parcels.wrap(threeSixtyStocks));
        this.getActivity().startActivity(intent);
    }

}
