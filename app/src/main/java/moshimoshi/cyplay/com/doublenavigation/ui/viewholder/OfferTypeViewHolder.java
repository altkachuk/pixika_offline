package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class OfferTypeViewHolder extends ItemViewHolder<String> {

    @BindView(R.id.type)
    TextView tvOfferType;

    public OfferTypeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(String s) {
        tvOfferType.setText(s);
    }
}
