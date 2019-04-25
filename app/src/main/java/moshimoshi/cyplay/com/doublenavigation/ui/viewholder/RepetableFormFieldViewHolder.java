package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by damien on 12/05/16.
 */
public class RepetableFormFieldViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.field_delete_icon)
    public ImageView deleteIcon;

    @Nullable
    @BindView(R.id.repeated_field_container)
    public FrameLayout container;

    public RepetableFormFieldViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

}
