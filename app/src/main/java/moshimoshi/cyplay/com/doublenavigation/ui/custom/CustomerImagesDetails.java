package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerInfoImageAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.MesuarementUtil;

/**
 * Created by romainlebouc on 16/08/16.
 */
public class CustomerImagesDetails extends LinearLayout {

    @Nullable
    @BindView(R.id.customer_images_details_icon)
    public ImageView icon;

    @Nullable
    @BindView(R.id.image_list_recycler_view)
    public RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.customer_images_details_title)
    public TextView title;

    private CustomerInfoImageAdapter adapter;
    private List<Media> value = new ArrayList<>();

    public CustomerImagesDetails(Context context) {
        this(context, null);
    }

    public CustomerImagesDetails(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public CustomerImagesDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutResourceId(), this, true);
        // Bind xml
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        adapter = new CustomerInfoImageAdapter(this.getContext(), value);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        int columns = 3;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), columns);
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Add Decorator
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columns, MesuarementUtil.dpToPixel(getContext(), 6f), true));
    }

    public void setTitle(int stringResourceId) {
        if (this.title != null) {
            this.title.setText(stringResourceId);
        }
    }

    public void setValue(List<Media> value) {
        if (this.value != null) {
            adapter.setMedias(value);
        }

    }

    public void setIcon(int iconResourceId) {
        if (icon != null) {
            this.icon.setVisibility(VISIBLE);
            this.icon.setImageResource(iconResourceId);
        }

    }

    private int getLayoutResourceId() {
        return R.layout.custom_customer_images_details;
    }

}
