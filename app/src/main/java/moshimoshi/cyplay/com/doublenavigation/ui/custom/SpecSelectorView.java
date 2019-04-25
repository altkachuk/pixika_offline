package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSpecificationSelectedEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductSpecificationAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.DisplayUtils;

/**
 * Created by damien on 16/11/16.
 */
public class SpecSelectorView extends FrameLayout {

    @BindView(R.id.spec_selector_spec_key)
    TextView specKey;

    @BindView(R.id.spec_selector_spec_value)
    TextView specValue;

    @BindView(R.id.spec_selector_choice)
    ImageView specSelectorChoice;

    @Inject
    EventBus bus;

    // Selected spec
    private ProductSpecification selectedSpec;

    // All Spec to select from
    private List<ProductSpecification> specifications;

    // Adatper
    private ProductSpecificationAdapter adapter;

    // popup view
    private ListPopupWindow popupWindow;

    // Deepness of spec
    private Integer level;

    public SpecSelectorView(Context context) {
        super(context);
        init();
    }

    public SpecSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpecSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init() {
        inflate(getContext(), R.layout.spec_selector_view, this);
        ButterKnife.bind(this);
        PlayRetailApp.get(this.getContext()).inject(this);
        // adapter
        adapter = new ProductSpecificationAdapter(getContext());
        // fillStocks views
        initViews();
    }

    public void initViews() {
        if (specKey != null) {
            // pop up
            popupWindow = createListPopupWindowForSpec(adapter, specKey);
            // click listener
            popupWindow.setOnItemClickListener(new SpecClickListener());
        }
    }

    private ListPopupWindow createListPopupWindowForSpec(ListAdapter adapter, View anchorView) {
        ListPopupWindow popupWindow;
        popupWindow = new ListPopupWindow(getContext());
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        popupWindow.setAnchorView(anchorView);
        popupWindow.setModal(true);
        return popupWindow;
    }

    private void selectSpecification(int position) {
        if (specifications != null && specifications.size() > position) {
            // store spec clicked
            selectedSpec = specifications.get(position);
            // Set selector text
            specKey.setText(selectedSpec.getSpec());
            specValue.setText(selectedSpec.getValueUnit());

            if (bus != null) {
                bus.post(new ProductSpecificationSelectedEvent(selectedSpec, level));
            }
        }
    }

    public void setDefaultSelection(Product product, Sku sku) {
        if (product != null && sku != null) {
            // get selector
            String selector = product.getSpecificationSelectorForLevel(level);
            // get selection
            ProductSpecification selectedSpecification = sku.getSpecificationForId(selector);
            // re-select the correct spec
            if (selectedSpecification != null && specifications.contains(selectedSpecification)) {
                selectSpecification(specifications.indexOf(selectedSpecification));
            }
            else {
                selectSpecification(0);
            }
        }
        else { // select first by default
            selectSpecification(0);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Get/Set
    // -------------------------------------------------------------------------------------------

    public ProductSpecification getSelectedSpec() {
        return selectedSpec;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /*public void setBus(EventBus bus) {
        this.bus = bus;
    }*/

    public void setSpecifications(List<ProductSpecification> specifications) {
        this.specifications = sort(specifications);
        adapter.setItems(this.specifications);
        // Updatep pop-up size
        popupWindow.setContentWidth(DisplayUtils.measureContentWidth(getContext(), adapter));

        // select by default
        // selectSpecification(0);
    }

    private List<ProductSpecification> sort(List<ProductSpecification> specifications) {
        Collections.sort(specifications);
        List<ProductSpecification> result = new ArrayList<>();
        int size = specifications.size();
        for (int i = size - 1; i >= 0; i--) {
            result.add(specifications.get(i));
        }
        return result;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.spec_selector_view)
    void onAnchorClick() {
        popupWindow.show();
    }


    private class SpecClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            popupWindow.dismiss();
            selectSpecification(position);
        }
    }

}
