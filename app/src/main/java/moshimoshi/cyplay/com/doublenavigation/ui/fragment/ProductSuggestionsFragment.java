package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSuggestion;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductAutoCompleteConfig;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ProductAutoCompleteItemConfig;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FilterableSortableProductsComponent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ProductSearch;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CategoriesSuggestionAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductTextSuggestionAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;

/**
 * Created by romainlebouc on 19/08/16.
 */
public class ProductSuggestionsFragment extends ResourceBaseFragment<ProductSuggestion> {

    @BindView(R.id.product_suggestions_view)
    View productSuggestionsView;

    // Product suggestions
    @BindView(R.id.product_suggestion_recycler_view)
    RecyclerView productSuggestionsRecyclerView;

    private ProductItemThumbnailAdapter productSuggestionAdapter;

    // Brand suggestions
    @Nullable
    @BindView(R.id.brand_suggestions_container)
    View brandSuggestionsContainer;

    @BindView(R.id.brands_recycler_view)
    RecyclerView brandsSuggestionsRecyclerView;

    @BindView(R.id.brands_loading_view)
    LoadingView brandsLoadingView;

    private ProductTextSuggestionAdapter brandsSuggestionAdapter;

    // Categories suggestions
    @Nullable
    @BindView(R.id.category_suggestions_container)
    View categorySuggestionsContainer;

    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;

    @BindView(R.id.categories_loading_view)
    LoadingView categoriesLoadingView;

    private CategoriesSuggestionAdapter categoriesSuggestionAdapter;

    private boolean productsSuggestionsEnabled = true;
    private boolean brandsSuggestionsEnabled = true;
    private boolean categoriesSuggestionsEnabled = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productSuggestionAdapter = new ProductItemThumbnailAdapter(this.getContext(), this.getResources().getInteger(R.integer.product_suggestions_columns_count));
        brandsSuggestionAdapter = new ProductTextSuggestionAdapter(this.getActivity());
        categoriesSuggestionAdapter = new CategoriesSuggestionAdapter(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_suggestions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manageSuggestionsActivations();
        initSuggestionRecyclerView();
        initSuggestionCategoriesRecyclerView();
        initSuggestionBrandsRecyclerView();
    }

    private void manageSuggestionsActivations() {

        ProductAutoCompleteConfig productAutoCompleteConfig = configHelper.getProductAutoCompleteConfig();
        if (productAutoCompleteConfig != null) {
            ProductAutoCompleteItemConfig productsAutoCompleteItemConfig = productAutoCompleteConfig.getProducts();
            if (productsAutoCompleteItemConfig != null) {
                productsSuggestionsEnabled = productsAutoCompleteItemConfig.getEnabled();
            }

            ProductAutoCompleteItemConfig brandsAutoCompleteItemConfig = productAutoCompleteConfig.getBrands();
            if (brandsAutoCompleteItemConfig != null) {
                brandsSuggestionsEnabled = brandsAutoCompleteItemConfig.getEnabled();
            }

            ProductAutoCompleteItemConfig categoriesAutoCompleteItemConfig = productAutoCompleteConfig.getFamilies();
            if (categoriesAutoCompleteItemConfig != null) {
                categoriesSuggestionsEnabled = categoriesAutoCompleteItemConfig.getEnabled();
            }

        }
    }

    private void initSuggestionRecyclerView() {
        if (productsSuggestionsEnabled) {
            boolean includeEdge = true;
            int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
            GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), getResources().getInteger(R.integer.product_suggestions_columns_count));
            // set layout
            productSuggestionsRecyclerView.setLayoutManager(layoutManager);
            // Add Decorator spacing between items
            productSuggestionsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.product_suggestions_columns_count), spacing, includeEdge));
            // Add Animation
            productSuggestionsRecyclerView.setItemAnimator(new FadeInAnimator());
            // link view with Adapter
            productSuggestionsRecyclerView.setAdapter(productSuggestionAdapter);
        } else {
            productSuggestionsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void initSuggestionCategoriesRecyclerView() {
        if (categoriesSuggestionsEnabled) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            // optimization
            categoriesRecyclerView.setHasFixedSize(true);
            // set layout
            categoriesRecyclerView.setLayoutManager(layoutManager);
            // Add Animation
            categoriesRecyclerView.setItemAnimator(new FadeInAnimator());
            // link view with Adapter
            categoriesRecyclerView.setAdapter(categoriesSuggestionAdapter);

            categoriesRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    List<Category> categories = ((CategoriesSuggestionAdapter) categoriesRecyclerView.getAdapter()).getItems();
                    if (categories != null && categories.size() > position) {
                        if (categories.get(position).getId() != null) {
                            ProductSearch search = new ProductSearch(ESearchMode.CATEGORY,
                                    categories.get(position).getId(),
                                    categories.get(position).getName());
                            ((FilterableSortableProductsComponent) ProductSuggestionsFragment.this.getActivity()).loadProducts(search,
                                    Pagination.getInitialPagingation()
                            );
                        }
                    }
                }
            }));
        } else {
            categorySuggestionsContainer.setVisibility(View.GONE);
        }


    }


    private void initSuggestionBrandsRecyclerView() {

        if (brandsSuggestionsEnabled) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            // optimization
            brandsSuggestionsRecyclerView.setHasFixedSize(true);
            // set layout
            brandsSuggestionsRecyclerView.setLayoutManager(layoutManager);
            // Add Animation
            brandsSuggestionsRecyclerView.setItemAnimator(new FadeInAnimator());
            // link view with Adapter
            brandsSuggestionsRecyclerView.setAdapter(brandsSuggestionAdapter);

            brandsSuggestionsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.getContext(), new RecyclerItemClickListener.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    List<String> brands = ((ProductTextSuggestionAdapter) brandsSuggestionsRecyclerView.getAdapter()).getItems();
                    if (brands != null && brands.size() > position) {
                        ProductSearch search = new ProductSearch(ESearchMode.BRAND,
                                brands.get(position),
                                brands.get(position));
                        ((FilterableSortableProductsComponent) ProductSuggestionsFragment.this.getActivity()).loadProducts(search,
                                Pagination.getInitialPagingation());
                    }
                }
            }));
        } else {
            brandSuggestionsContainer.setVisibility(View.GONE);
        }


    }

    @Override
    protected void setResource(ProductSuggestion productSuggestion) {

    }

    @Subscribe
    @Override
    public void onResourceResponseEvent(ResourceResponseEvent<ProductSuggestion> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.PRODUCT_SUGGESTION) {
            ProductSuggestion productSuggestion = resourceResponseEvent.getResource();
            if (productSuggestion != null) {
                if ( productsSuggestionsEnabled){
                    this.productSuggestionAdapter.setItems((List<ProductItem>) (List<?>) productSuggestion.getProducts());
                }

                if ( brandsSuggestionsEnabled){
                    this.brandsSuggestionAdapter.setItems(productSuggestion.getBrands());
                    brandsLoadingView.setLoadingState(productSuggestion.getBrands() == null || productSuggestion.getBrands().isEmpty() ? LoadingView.LoadingState.NO_RESULT : LoadingView.LoadingState.LOADED);

                }

                if ( categoriesSuggestionsEnabled){
                    ProductSuggestionsFragment.this.categoriesSuggestionAdapter.setItems(productSuggestion.getFamilies());
                    categoriesLoadingView.setLoadingState(productSuggestion.getFamilies() == null || productSuggestion.getFamilies().isEmpty() ? LoadingView.LoadingState.NO_RESULT : LoadingView.LoadingState.LOADED);

                }

            }
        }
    }

    @Subscribe
    @Override
    public void onResourceRequestEvent(ResourceRequestEvent<ProductSuggestion> resourceRequestEvent) {

    }

    @Override
    public ProductSuggestion getCachedResource() {
        return null;
    }

    @Override
    public void loadResource() {

    }
}
