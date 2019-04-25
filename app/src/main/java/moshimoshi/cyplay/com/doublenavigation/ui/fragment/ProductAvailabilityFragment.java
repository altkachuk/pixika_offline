package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SkaViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;


/**
 * Created by damien on 17/04/16.
 */
public class ProductAvailabilityFragment extends ResourceBaseFragment<Product> implements DisplayEventFragment {

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.product_availability_recycler_view)
    RecyclerView recyclerView;

    private Sku sku;

    private ItemAdapter<Ska, SkaViewHolder> adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_availability, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initAdapter() {
        adapter = new ItemAdapter<Ska, SkaViewHolder>(this.getContext()) {
            @Override
            public SkaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_ska, parent, false);
                return new SkaViewHolder(v);
            }

            @Override
            public void onBindViewHolder(SkaViewHolder holder, int position) {
                Ska item = items.get(position);
                if (item != null) {
                    holder.setParentSku(sku);
                    holder.setItem(item);
                    //noinspection ResourceAsColor
                    holder.itemView.setBackgroundColor(position % 2 == 0 ?
                            ContextCompat.getColor(ProductAvailabilityFragment.this.getContext(), android.R.color.transparent)
                            : ContextCompat.getColor(ProductAvailabilityFragment.this.getContext(), R.color.dim04));


                }
            }
        };
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        // Add Animation
        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void updateSku() {
        if (sku != null) {
            if (sku.getAvailabilities() != null) {
                double minStockDisplay = configHelper.getMinStockDisplay();
                List<Shop> notFilterableShops = new ArrayList<>();
                notFilterableShops.add(configHelper.getCurrentShop());
                sku.filterAvailabilities(notFilterableShops, minStockDisplay);
                adapter.setItems(sku.getAvailabilities());
            }
        }
    }

    public List<Ska> getSkaInOtherShops(List<Ska> allSka) {
        List<Ska> result = new ArrayList<>();
        if (allSka != null) {
            for (Ska ska : allSka) {
                if (configHelper.getCurrentShop() != null
                        && !configHelper.getCurrentShop().getId().equals(ska.getShop_id())) {
                    result.add(ska);
                }
            }
        }
        return result;
    }


    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    protected void setResource(Product product) {
        if (product != null) {
            sku = ((AbstractProductActivity) this.getActivity()).getSelectedSku();
            updateSku();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        initRecycler();
    }

    @Override
    public Product getCachedResource() {
        Product product = ((ResourceActivity<Product>) this.getActivity()).getResource();
        sku = ((AbstractProductActivity) this.getActivity()).getSelectedSku();
        if (product != null) {
            return product.isSkuAvailabilitesFilled(this.sku) ? product : null;
        }
        return null;
    }

    @Override
    public void loadResource() {

    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------


    @Override
    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Product> resourceResponseEvent) {
        if (EResourceType.PRODUCT_STOCK == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Product> resourceRequestEvent) {
        if (EResourceType.PRODUCT_STOCK == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Subscribe
    public void onSkuChange(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        this.sku = productSelectedSkuChangeEvent.getSku();
        updateSku();
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.PRODUCT)
                .setAttribute(EResourceAttribute.SKUS.getCode())
                .setSubAttribute("availabilities")
                .setValue(sku!=null ? sku.getId():null)
                .setStatus(EActionStatus.SUCCESS));
    }
}
