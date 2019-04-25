package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractCatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CatalogCategoryAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CatalogCategoryViewHolder;

/**
 * Created by damien on 28/07/16.
 */
public class CatalogCategoriesFragment extends BaseFragment {

    @BindView(R.id.parent_category_root)
    View parentCategoryView;

    @BindView(R.id.parent_category_title)
    TextView parentCategoryTitle;

    @BindView(R.id.category_list_recycler_view)
    RecyclerView recyclerView;

    private Category categoryParent;

    private Category categoryCurrent;

    private List<Category> subCategories;

    private CatalogCategoryAdapter adapter;

    ActionBar actionBar;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CatalogCategoryAdapter(this.getContext(), subCategories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog_categories, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateActionBar();
        updateParentCategory();
        initDataSet();
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateActionBar();
        updateParentCategory();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initDataSet() {
        adapter.setCategories(subCategories);
    }

    private void updateActionBar(){
        if (categoryCurrent != null && categoryCurrent.getName() != null && actionBar != null) {
            actionBar.setTitle(this.categoryCurrent.getName());
        }
    }
    private void updateParentCategory() {
        if (categoryParent != null && categoryParent.getName() != null) {
            parentCategoryView.setVisibility(View.VISIBLE);
            parentCategoryTitle.setText(categoryParent.getName());
        } else {
            parentCategoryView.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Add Decorator
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        // OnClick action
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new CategoryClick()));
    }

    public void setCategoryParent(Category categoryParent) {
        this.categoryParent = categoryParent;
    }

    public void setCategoryCurrent(Category categoryCurrent) {
        this.categoryCurrent = categoryCurrent;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.parent_category_root)
    public void onParentCategoryClick() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    private CatalogCategoryViewHolder selectedCategory;

    private class CategoryClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            Category categoryClicked = subCategories.get(position);
            if (categoryClicked.getHas_sub_families()) {
                selectedCategory = new CatalogCategoryViewHolder(view);
                selectedCategory.loading.setVisibility(View.VISIBLE);
            }
            if (getActivity() != null && !getActivity().isDestroyed()) {
                ((AbstractCatalogActivity) getActivity()).displayNextCatalogPage(subCategories.get(position));
            }

        }
    }

    public void onCatalogCategoriesFragment() {
        if (selectedCategory != null) {
            selectedCategory.loading.setVisibility(View.INVISIBLE);
        }
    }

    public Category getCategoryCurrent() {
        return categoryCurrent;
    }

    public Category getCategoryParent() {
        return categoryParent;
    }

}
