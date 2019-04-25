package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.FilterFilterValue;

/**
 * Created by romainlebouc on 31/08/16.
 */
public class ActiveFilterValueViewHolder extends ItemViewHolder<FilterFilterValue>{

    @BindView(R.id.criteria_name)
    TextView criteriaName;

    @BindView(R.id.critera_remove)
    ImageView removeCriteria;

    public ActiveFilterValueViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(FilterFilterValue filterValue) {
        criteriaName.setText( filterValue.getResourceFilterValue().getValue());
    }
}

