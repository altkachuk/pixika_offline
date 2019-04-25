package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSize;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;

public class ImageForGalleryFragment extends BaseFragment {

    @BindView(R.id.image_view)
    ImageView imageView;

    private ArrayList<String> images;
    private Integer position;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_for_gallery, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(IntentConstants.EXTRA_PRODUCT_IMGS))
            images = getArguments().getStringArrayList(IntentConstants.EXTRA_PRODUCT_IMGS);
        if (getArguments() != null && getArguments().containsKey(IntentConstants.EXTRA_PRODUCT_IMGS_POS))
            position = getArguments().getInt(IntentConstants.EXTRA_PRODUCT_IMGS_POS);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (images != null && images.size() > position)
            Picasso.get()
                    .load(ImageUtils.getImageUrl(this.getContext(),images.get(position), EMediaSize.FULL, configHelper))
                    .placeholder(R.drawable.empty_img)
                    .error(R.drawable.empty_img)
                    .noFade()
                    .into(imageView);
    }


}
