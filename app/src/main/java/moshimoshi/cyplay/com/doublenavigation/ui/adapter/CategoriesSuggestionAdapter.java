package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CategorySuggestionViewHolder;


/**
 * Created by romainlebouc on 18/08/16.
 */
public class CategoriesSuggestionAdapter extends ItemAdapter<Category,CategorySuggestionViewHolder> {

    public CategoriesSuggestionAdapter(Context ctx) {
        super(ctx);
        this.context = ctx;
    }

    @Override
    public CategorySuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_product_text_suggestion, parent, false);
        return new CategorySuggestionViewHolder(v);
    }
}

