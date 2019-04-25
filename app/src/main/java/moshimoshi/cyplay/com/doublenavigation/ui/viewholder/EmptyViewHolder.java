package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 10/06/16.
 */
public class EmptyViewHolder<T> extends IEmptyOrItemViewHolder<T> {

    @Nullable
    @BindView(R.id.empty_state_icon)
    ImageView emptyIcon;

    private Context context;

    private int resId = -1;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public EmptyViewHolder(View itemView, Context context, int drawableRessourceId) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.resId = drawableRessourceId;
    }

    @Override
    public void setItem(T o) {
        if (emptyIcon != null && resId !=-1)
            emptyIcon.setImageDrawable(ContextCompat.getDrawable(context, resId));
    }

}
