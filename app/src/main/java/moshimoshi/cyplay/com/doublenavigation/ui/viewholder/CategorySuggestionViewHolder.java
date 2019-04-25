package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;

/**
 * Created by romainlebouc on 18/08/16.
 */
public class CategorySuggestionViewHolder extends ItemViewHolder<Category>{

    private Category category;

    @Nullable
    @BindView(R.id.product_text_suggestion)
    TextView suggestion;

    public CategorySuggestionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(Category category) {
        this.category = category;
        String categoryStr = category.getPath();
        if (categoryStr == null || categoryStr.isEmpty()) {
            categoryStr = category.getName();
        }
        suggestion.setText(categoryStr);
    }
}

