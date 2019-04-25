package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAction;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterNumberChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceActionEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FilterableSortableProductsComponent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductsSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ResourceSortSpinnerAdapter;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by romainlebouc on 23/12/2016.
 */

public class ProductFilterSortFragment extends BaseFragment {

    @Inject
    protected EventBus bus;

    @BindView(R.id.resource_sort_spinner)
    Spinner productSearchSortSpinner;

    @BindView(R.id.resource_sort_filter_view)
    View productSearchFilteringView;

    @Nullable
    @BindView(R.id.sortable_filterable_extra_margin)
    View extraMargin;

    @BindView(R.id.filter_number)
    TextView tvFilterNumber;

    @Nullable
    @BindView(R.id.back_button)
    View backBtn;


    private FilterableSortableProductsComponent filterableSortableProductsComponent;
    private FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent;
    private ESearchMode eSearchMode;

    private boolean animated = false;
    private boolean isExtended = false;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = inflater.inflate(R.layout.custom_sortable_filterable_resource, container, false);
        return inflater.inflate(R.layout.custom_sortable_filterable_resource, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (bus != null) {
            bus.register(this);
        }

        animated = this.getActivity().getResources().getBoolean(R.bool.product_search_filters_animated)
                && this.getActivity() instanceof ProductsSearchActivity;

        filterableSortableProductsComponent = ((FilterableSortableProductsComponent) this.getActivity());

        eSearchMode = (ESearchMode) this.getActivity().getIntent().getSerializableExtra(IntentConstants.EXTRA_SEARCH_MODE);
        eSearchMode = eSearchMode != null ? eSearchMode : ESearchMode.TEXT;
        initSortSpinner(eSearchMode);

        if (extraMargin != null && this.getActivity() instanceof ProductsSearchActivity) {
            extraMargin.setVisibility(View.VISIBLE);
        }

        if (backBtn != null && this.getActivity() instanceof CatalogActivity){
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterableSortableProductsComponent.onBackBtnClicked();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        if (bus != null) {
            bus.unregister(this);
        }
        super.onDestroy();
    }

    private void initSortSpinner(ESearchMode eSearchMode) {
        // Create adapter
        ResourceSortSpinnerAdapter spinnerArrayAdapter = new ResourceSortSpinnerAdapter(getContext(),
                configHelper.getProductSearchSorting(eSearchMode));

        // Set adapter
        productSearchSortSpinner.setAdapter(spinnerArrayAdapter);
        productSearchSortSpinner.setSelection(0, false);
        productSearchSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterableSortableProductsComponent.loadResource(Pagination.getInitialPagingation());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void clearSorting() {

        if (productSearchSortSpinner != null) {
            AdapterView.OnItemSelectedListener onItemSelectedListener = productSearchSortSpinner.getOnItemSelectedListener();
            productSearchSortSpinner.setOnItemSelectedListener(null);
            productSearchSortSpinner.setSelection(0, false);
            productSearchSortSpinner.setOnItemSelectedListener(onItemSelectedListener);
        }

    }

    @OnClick(R.id.filter_button_text_container)
    public void onFiltersClick() {
        bus.post(new ResourceActionEvent(EResourceAction.FILTER));
    }

    @OnClick(R.id.resource_sort_container)
    public void onResouceSort(){
        productSearchSortSpinner.performClick();
    }

    @Subscribe
    public void onResourceResponseEvent(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.PRODUCTS) {
            this.resourceResponseEvent = resourceResponseEvent;
            // We change sort/filter display only for the first request when paginated
            if (resourceResponseEvent.getPrevious() == null) {
                setVisible(resourceResponseEvent.getResource() != null && !resourceResponseEvent.getResource().isEmpty());
                filterableSortableProductsComponent.setAvailableFilters(this.resourceResponseEvent.getFilters());
            }

        }
    }

    public ResourceFieldSorting getResourceFieldSorting() {
        return productSearchSortSpinner != null ? (ResourceFieldSorting) productSearchSortSpinner.getSelectedItem() : null;
    }


    public void hide() {
        productSearchFilteringView.setVisibility(View.GONE);
    }

    public void setVisible(boolean visible) {
        if (animated) {
            productSearchFilteringView.setVisibility(visible ? View.VISIBLE : View.GONE);
            ValueAnimator anim = null;
            if (visible && !isExtended) {
                anim = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen.filter_sort_size));
                isExtended = !isExtended;
            } else if (!visible && isExtended) {
                anim = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen.filter_sort_size), 0);
                isExtended = !isExtended;
            }
            if (anim != null) {
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = productSearchFilteringView.getLayoutParams();
                        layoutParams.width = val;
                        productSearchFilteringView.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(128);
                anim.start();
            }
        } else {
            if (productSearchFilteringView != null) {
                productSearchFilteringView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }

        }


    }

    @Subscribe
    public void onFilterNumberChangeEvent(FilterNumberChangeEvent event){
        int filterNumber = event.getFilterNumber();
        if (filterNumber > 0) {
            tvFilterNumber.setVisibility(View.VISIBLE);
            tvFilterNumber.setText(event.getFilterNumber() + "");
        }else{
            tvFilterNumber.setVisibility(View.GONE);
        }


    }


}
