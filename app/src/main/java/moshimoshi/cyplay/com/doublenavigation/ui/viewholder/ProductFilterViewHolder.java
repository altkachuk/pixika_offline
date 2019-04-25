package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by romainlebouc on 30/08/16.
 */
public class ProductFilterViewHolder<TResourceFilterValue extends ResourceFilterValue> extends AbstractExpandableItemViewHolder {

    @Nullable
    @BindView(R.id.criteria_name)
    TextView criteriaName;

    @Nullable
    @BindView(R.id.selector_indicator)
    ImageView selectorIndicator;

    private Context context;

    private boolean open = false;
    private List<ResourceFilter<ResourceFilter, TResourceFilterValue>> selectedFilters;

    private ResourceFilter resourceFilter;
    private final static int INDICATOR_ANIMATION_DURATION = 200;

    private final View.OnClickListener cellClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AnimationSet animationSet = new AnimationSet(true);
            RotateAnimation r = new RotateAnimation(open ? 90f : 0f, open ? 0 : 90f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);

            r.setDuration(INDICATOR_ANIMATION_DURATION);
            animationSet.addAnimation(r);

            animationSet.setFillEnabled(true);
            animationSet.setFillAfter(true);

            selectorIndicator.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //for some tables, if change the tint, the arrow will be disappeared
                    //TODO: find a solution
                    //CompatUtils.setDrawableTint(selectorIndicator.getDrawable(), ContextCompat.getColor(ProductFilterViewHolder.this.itemView.getContext(), !open ? R.color.colorAccent : R.color.Gray));
                    open = !open;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

        }
    };

    public ProductFilterViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(cellClickListener);
    }

    public void setItem(ResourceFilter productFilter) {
        this.resourceFilter = productFilter;
        if (selectedFilters != null && selectedFilters.contains(productFilter)) {
            this.criteriaName.setTextColor(ContextCompat.getColor(this.itemView.getContext(), R.color.colorAccent));
        } else {
            this.criteriaName.setTextColor(ContextCompat.getColor(this.itemView.getContext(), R.color.Gray));
        }
        this.criteriaName.setText(productFilter.getLabel(context));
    }

    public void setSelectedFilters(List<ResourceFilter<ResourceFilter, TResourceFilterValue>> productFilters) {
        this.selectedFilters = productFilters;
    }

}
