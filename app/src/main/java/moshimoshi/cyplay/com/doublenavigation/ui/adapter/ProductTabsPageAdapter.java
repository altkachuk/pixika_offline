package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductAvailabilityFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRelatedFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSubstitutionFragment;

public class ProductTabsPageAdapter extends FragmentPagerAdapter {

    private final Context context;

    // Fragments
    private ProductRelatedFragment productRelatedFragment;
    private ProductAvailabilityFragment productAvailabilityFragment;
    private ProductSubstitutionFragment productSubstitutionFragment;

    // Product
    private Product product;

    public ProductTabsPageAdapter(Context context, FragmentManager fm, Product product) {
        super(fm);
        this.context = context;
        this.product = product;
        if (product != null && product.getRelated_prod_ids() != null && product.getRelated_prod_ids().size() > 0)
            productRelatedFragment = new ProductRelatedFragment();
        productAvailabilityFragment = new ProductAvailabilityFragment();
        if (product != null && product.getSubstitute_prod_ids() != null && product.getSubstitute_prod_ids().size() > 0)
            productSubstitutionFragment = new ProductSubstitutionFragment();
    }

    /**
     * Return fragment with respect to Position .
     */
    @Override
    public Fragment getItem(int position) {
        // Get fragment accordingly to the fact that there is or isn't related product and/or substitution product
        if (position == 0) {
            if (productRelatedFragment != null)
                return productRelatedFragment;
            return productAvailabilityFragment;
        } else if (position == 1) {
            if (productRelatedFragment != null)
                return productAvailabilityFragment;
            return productSubstitutionFragment;
        } else if (position == 2) {
            // if 3 items no need to ic_filter_check, it's not null
            return productSubstitutionFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (productRelatedFragment != null)
            count++;
        if (productAvailabilityFragment != null)
            count++;
        if (productSubstitutionFragment != null)
            count++;
        return count;
    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {
        // Get title accordingly to the fact that there is or isn't related product and/or substitution product
        if (position == 0) {
            if (productRelatedFragment != null)
                return context.getString(R.string.product_tab_related_product_title);
            return context.getString(R.string.product_tab_availability_title);
        } else if (position == 1) {
            if (productRelatedFragment != null)
                return context.getString(R.string.product_tab_availability_title);
            return context.getString(R.string.product_tab_substitution_title);
        } else if (position == 2) {
            return context.getString(R.string.product_tab_substitution_title);
        }
        return null;
    }
}