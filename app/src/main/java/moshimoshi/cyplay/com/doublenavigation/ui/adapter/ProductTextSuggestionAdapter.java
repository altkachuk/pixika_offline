package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ProductTextSuggestionViewHolder;


/**
 * Created by romainlebouc on 17/08/16.
 */
public class ProductTextSuggestionAdapter extends ItemAdapter<String,ProductTextSuggestionViewHolder> {

    public ProductTextSuggestionAdapter(Context ctx) {
       super(ctx);
    }
    @Override
    public ProductTextSuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_product_text_suggestion, parent, false);
        return new ProductTextSuggestionViewHolder(v);
    }
}
