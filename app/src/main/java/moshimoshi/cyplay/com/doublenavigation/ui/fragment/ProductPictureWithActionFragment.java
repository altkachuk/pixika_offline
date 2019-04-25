package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ImageSliderFragmentAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.FeatureColor;

/**
 * Created by damien on 15/04/16.
 */
public class ProductPictureWithActionFragment extends BaseFragment {

    @Inject
    EventBus bus;

    @BindView(R.id.pager_product_images)
    ViewPager pager;

    @BindView(R.id.pager_indicator)
    UnderlinePageIndicator pageIndicator;

    private Product product;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_picture_with_action, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPager();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (bus != null)
            bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus != null)
            bus.unregister(this);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initPager() {
        if (product != null && product.getMedias() != null && product.getMedias().size() > 0) {
            ArrayList<Media> imgArrayList = new ArrayList<>(product.getMedias().size());
            imgArrayList.addAll(product.getMedias());
            pager.setAdapter(new ImageSliderFragmentAdapter(getChildFragmentManager(), imgArrayList));
            pageIndicator.setViewPager(pager);
            pageIndicator.setFades(false);
            pageIndicator.setSelectedColor(Color.parseColor(FeatureColor.getColorHex(this.getContext(),FeatureColor.PRIMARY_DARK,configHelper)));
            pageIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        }
    }



    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------








    @Subscribe
    public void onProductLoaded(final ProductLoadedEvent productLoadedEvent) {
        // We ic_filter_check that the event correspond to the right product
        this.product = productLoadedEvent.getProduct();
        this.initPager();

    }

}
