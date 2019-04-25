package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.AttributeReview;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReview;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductReviewPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenEditTextActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.SpaceItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.CommentableFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.AttributeReviewViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

import static android.app.Activity.RESULT_OK;

/**
 * Created by romainlebouc on 26/12/2016.
 */

public class ProductAttributeReviewsFragment extends ResourceBaseFragment<ProductReview> implements ResourceView<ProductReview>, CommentableFragment, DisplayEventFragment {

    @Inject
    CustomerContext customerContext;

    @Inject
    ProductReviewPresenter productReviewPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.product_reviews_recycler_view)
    RecyclerView productReviewsRecyclerView;

    private ProductReview resource;
    private ItemAdapter<AttributeReview, AttributeReviewViewHolder> itemAdapter;
    private Sku sku;

    private boolean isWritingComment = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_opinion, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemAdapter = new ItemAdapter<AttributeReview, AttributeReviewViewHolder>(this.getContext()) {
            @Override
            public AttributeReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_attribute_review, parent, false);
                return new AttributeReviewViewHolder(v, ProductAttributeReviewsFragment.this.getTag());
            }

            @Override
            public void onBindViewHolder(AttributeReviewViewHolder holder, int position) {
                AttributeReview item = resource.getAttribute_reviews().get(position);
                if (item != null) {
                    holder.setItem(item);
                }
            }
        };

        productReviewPresenter.setView(this);
        productReviewPresenter.setCreateView(new ResourceView<ProductReview>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<ProductReview> resourceResponseEvent) {

            }
        });
        initRecyclerView();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getCachedResource() == null) {
            loadResource();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // optimization
        productReviewsRecyclerView.setHasFixedSize(true);
        // set layout
        productReviewsRecyclerView.setLayoutManager(layoutManager);
        productReviewsRecyclerView.setAdapter(itemAdapter);
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        productReviewsRecyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
        // add onclick listener
        productReviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Subscribe
    @Override
    public void onResourceResponseEvent(ResourceResponseEvent<ProductReview> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.PRODUCT_REVIEW) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Subscribe
    @Override
    public void onResourceRequestEvent(ResourceRequestEvent resourceRequestEvent) {

    }

    @Override
    protected void setResource(ProductReview productReview) {
        resource = productReview;
        if (productReview != null) {
            itemAdapter.setItems(productReview.getAttribute_reviews());
        }
    }

    @Override
    public ProductReview getCachedResource() {
        return resource;
    }

    @Override
    public void loadResource() {
        if (this.getActivity() instanceof AbstractProductActivity) {
            AbstractProductActivity abstractProductActivity = (AbstractProductActivity) this.getActivity();
            if (abstractProductActivity.getProduct() != null && abstractProductActivity.getSelectedSku() != null) {
                sku = abstractProductActivity.getSelectedSku();
                productReviewPresenter.getProductReview(
                        customerContext.getCustomerId(),
                        abstractProductActivity.getProduct().getId(),
                        abstractProductActivity.getSelectedSku().getId());
            }
        }
    }

    @Subscribe
    public void onSkuSelectedEvent(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        loadResource();
    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<ProductReview> resourceResponseEvent) {
        if (resource != null) {
            resource.resetUpdated();
        }
        bus.post(resourceResponseEvent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (resource != null && resource.isUpdated()) {
            if (resource.getId() != null) {
                productReviewPresenter.updateReview(resource);
            } else {
                productReviewPresenter.addReview(resource);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IntentConstants.EDIT_TEXT_RESULT) {
            if (resultCode == RESULT_OK) {
                if (resource != null && itemAdapter != null) {
                    String id = data.getStringExtra(IntentConstants.EXTRA_EDIT_ID);
                    String updatedText = data.getStringExtra(IntentConstants.EXTRA_EDIT_TEXT);

                    AttributeReview attributeReview = resource.getAttributeReviewForAttributeId(id);
                    attributeReview.setComment(updatedText);
                    this.itemAdapter.notifyDataSetChanged();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        isWritingComment = false;
    }

    @Override
    public void triggerWriteComment(String editId, String editComment) {
        isWritingComment = true;
        Intent intent = new Intent(this.getActivity(), FullScreenEditTextActivity.class);
        intent.putExtra(IntentConstants.EXTRA_EDIT_TEXT, editComment);
        intent.putExtra(IntentConstants.EXTRA_EDIT_ID, editId);
        this.startActivityForResult(intent, IntentConstants.EDIT_TEXT_RESULT);
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.PRODUCT)
                .setAttribute(EResourceAttribute.REVIEWS.getCode())
                .setValue(sku != null ? sku.getId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }
}
