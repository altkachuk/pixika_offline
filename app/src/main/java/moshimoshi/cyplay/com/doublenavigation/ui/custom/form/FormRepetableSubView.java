package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.RepetableFormFieldAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;

/**
 * Created by damien on 12/05/16.
 */
public class FormRepetableSubView extends moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSubView {

    @Nullable
    @BindView(R.id.repetable_form_row_recycler_view)
    RecyclerView repetableRowRecyclerView;

    private RepetableFormFieldAdapter adapter;

    public FormRepetableSubView(Context context) {
        super(context);
        init();
    }

    public FormRepetableSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FormRepetableSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private boolean isloaded = false;

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.form_repetable_layout, this);
        ButterKnife.bind(this);
        isloaded = true;
        updateInfo();
    }

    private void updateInfo() {
        if (isloaded && row != null) {
            // Init recycler view
            initRecycler();
            // fillStocks Adapter
            adapter = new RepetableFormFieldAdapter(getContext(), row, descriptor);
            // Set Adapter
            repetableRowRecyclerView.setAdapter(adapter);
            List<String> values = ReflectionUtils.get(customer, row.getTag());
            // assign results
            adapter.setValues(values);
        }
    }

    private void initRecycler() {
        if (repetableRowRecyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            // optimization
            repetableRowRecyclerView.setHasFixedSize(false);
            // Optimisation inside scrollview
            repetableRowRecyclerView.setNestedScrollingEnabled(false);
            // set layout
            repetableRowRecyclerView.setLayoutManager(layoutManager);
            // Add Separator
            repetableRowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.simple_list_divider));
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void setRow(PR_FormRow row) {
        super.setRow(row);
        updateInfo();
    }

    @Override
    public View runValidation() {
        View view = adapter.runValidation();
        // set results
        ReflectionUtils.set(customer, row.getTag(), adapter.getValues());
        return view;
    }

    @Override
    public void setValue(Object val) {

    }

    @Override
    public String getValue() {
        return null;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @Nullable
    @OnClick(R.id.add_field_button)
    public void onAddClick() {
        adapter.addOneField();
        adapter.notifyDataSetChanged();
    }
}
