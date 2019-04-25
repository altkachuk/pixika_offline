package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;

/**
 * Created by damien on 20/04/16.
 */
public class CatalogCategoryViewHolder extends ItemViewHolder<Category> {

    @Nullable
    @BindView(R.id.category_title)
    public TextView title;

    @Nullable
    @BindView(R.id.category_loading)
    public ProgressBar loading;

    public CatalogCategoryViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(Category category) {
        if (title != null) {
            if (category != null) {
                title.setText(category.getName());
            } else {
                title.setText(null);
            }
        }
    }

}