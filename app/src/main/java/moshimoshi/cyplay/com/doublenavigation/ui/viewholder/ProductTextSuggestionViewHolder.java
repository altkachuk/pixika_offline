package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;


/**
 * Created by romainlebouc on 17/08/16.
 */
public class ProductTextSuggestionViewHolder extends ItemViewHolder<String>{

    private String item;

    @Nullable
    @BindView(R.id.product_text_suggestion)
    TextView suggestion;

    public ProductTextSuggestionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(String s) {
        this.item = s;
        suggestion.setText(s);

    }
}
