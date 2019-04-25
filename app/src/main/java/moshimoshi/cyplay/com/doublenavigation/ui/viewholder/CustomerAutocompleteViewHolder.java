package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by damien on 02/05/16.
 */
public class CustomerAutocompleteViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.customer_name)
    public TextView name;

    public CustomerAutocompleteViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

}
