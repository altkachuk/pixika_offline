package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractCustomerSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerPreviewAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.TextSearchField;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;

/**
 * Created by damien on 02/05/16.
 */
public class CustomerSearchActivity extends AbstractCustomerSearchActivity {


    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customersRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    protected void initListeners() {
        zipcodeSearch.setCustomerSearchFieldListener(new TextSearchField.CustomerSearchFieldListener() {
            @Override
            public void onSearch() {
                searchCustomer();
            }
        });
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {

    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------


    private boolean validateForm() {
        boolean result = lastNameSearch.hasMinimumLength(3, false);
        result = firstNameSearch.hasMinimumLength(3, false) && result;
        result = zipcodeSearch.hasMinimumLength(2, false) && result;
        return result;
    }


    protected void searchCustomer() {
        setResource(new ArrayList<Customer>());
        if (validateForm()) {
            ActivityHelper.hideSoftKeyboard(this);
            super.onResourceRequest(new ResourceRequestEvent<List<Customer>>(EResourceType.CUSTOMERS));

            Map<String, String> searchParameters = new HashMap<>();
            if (firstNameSearch != null) {
                searchParameters.put("details__first_name", firstNameSearch.getValue());
            }
            if (lastNameSearch != null) {
                searchParameters.put("details__last_name", lastNameSearch.getValue());
            }
            if (zipcodeSearch != null) {
                searchParameters.put("details__mail__zipcode", zipcodeSearch.getValue());
            }

            customerSearchPresenter.searchCustomer(searchParameters);
        }
    }
    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------



}
