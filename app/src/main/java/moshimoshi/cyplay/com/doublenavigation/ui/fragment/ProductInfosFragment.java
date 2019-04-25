package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.FeesAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SkuSpecificationsAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;


/**
 * Created by romainlebouc on 11/08/16.
 */
public class ProductInfosFragment extends ResourceBaseFragment<Product>  implements DisplayEventFragment{

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.product_desc_container)
    View productDescriptionContainer;

    @BindView(R.id.product_desc)
    TextView productDescription;

    @BindView(R.id.product_comp_desc)
    TextView productComplementaryDescription;

    @Nullable
    @BindView(R.id.product_ref)
    TextView productRef;

    @BindView(R.id.product_specs)
    RecyclerView productSpecsRecyclerView;

    @BindView(R.id.special_fees_container)
    View feesContainer;

    @BindView(R.id.extra_fees)
    RecyclerView feesRecyclerView;

    private SkuSpecificationsAdapter skuSpecificationsAdapter;
    private FeesAdapter feesAdapter;
    private Product product;
    private String refStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_infos, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        skuSpecificationsAdapter = new SkuSpecificationsAdapter(configHelper);
        feesAdapter = new FeesAdapter(this.getContext());
        initRecyclerView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (productRef != null && refStr != null && !refStr.isEmpty()) {
            productRef.setText(refStr);
        }
    }

    private void initRecyclerView() {
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.contextual_default_margin), getResources().getDisplayMetrics());

        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            // optimization
            productSpecsRecyclerView.setHasFixedSize(false);
            // set layout
            productSpecsRecyclerView.setLayoutManager(layoutManager);
            productSpecsRecyclerView.setAdapter(skuSpecificationsAdapter);
            productSpecsRecyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
            productSpecsRecyclerView.setNestedScrollingEnabled(false);
        }

        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            // optimization
            feesRecyclerView.setHasFixedSize(true);
            // set layout
            feesRecyclerView.setLayoutManager(layoutManager);
            feesRecyclerView.setAdapter(feesAdapter);
            feesRecyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
            feesRecyclerView.setNestedScrollingEnabled(false);
        }

    }

    @Override
    public void onDestroyView() {
        if (productRef != null)
            refStr = productRef.getText().toString();
        super.onDestroyView();
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

    protected void setResource(Product resource) {
        product = resource;
        if (product != null) {

            if (product.getShort_desc() != null || product.getLong_desc() != null) {
                productDescriptionContainer.setVisibility(View.VISIBLE);
            } else {
                productDescriptionContainer.setVisibility(View.GONE);
            }

            // Display Short Desc
            if (product.getShort_desc() != null) {
                productDescription.setText(Html.fromHtml(product.getShort_desc()));
            } else {
                productDescription.setVisibility(View.GONE);
            }

            // Display Long Desc
            if (product.getLong_desc() != null) {
                productComplementaryDescription.setText(Html.fromHtml(product.getLong_desc()));
            } else {
                productComplementaryDescription.setVisibility(View.GONE);
            }

            skuSpecificationsAdapter.setProductSku(resource, ((AbstractProductActivity) this.getActivity()).getSelectedSku());
        }
    }


    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Override
    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        } else if (EResourceType.FEES == resourceResponseEvent.getEResourceType()) {
            List<Fee> fees = deleteNoValueFee((List<Fee>) resourceResponseEvent.getResource());
            feesContainer.setVisibility(fees != null && !fees.isEmpty() ? View.VISIBLE : View.GONE);
            feesAdapter.setItems(fees);
        }
    }

    private List<Fee> deleteNoValueFee(List<Fee> fess) {
        List<Fee> result = new ArrayList<>();
        if (fess == null) {
            return result;
        }
        for (Fee fee : fess) {
            if (fee.getValue() != null) {
                result.add(fee);
            }
        }
        return result;
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Product> resourceRequestEvent) {
        if (EResourceType.PRODUCT == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        } else if (EResourceType.FEES == resourceRequestEvent.getEResourceType()) {
            feesAdapter.setItems(new ArrayList<Fee>());
        }
    }

    @Subscribe
    public void onSkuChange(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        skuSpecificationsAdapter.setProductSku(product, productSelectedSkuChangeEvent.getSku());
        if (productRef != null) {
            String ref = productSelectedSkuChangeEvent.getSku().getRef(this.getContext(), product);
            refStr = ref;
            productRef.setText(ref);
            productRef.setVisibility(ref != null && !ref.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.PRODUCT)
                .setAttribute(EResourceAttribute.SKUS.getCode())
                .setSubAttribute("infos")
                .setValue(product!=null ? product.getId():null)
                .setStatus(EActionStatus.SUCCESS));
    }

}
