package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ESearchSuggestionMode;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractCustomerSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.TextSearchField;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;

/**
 * Created by damien on 02/05/16.
 */
public class CustomerSearchActivity extends AbstractCustomerSearchActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customersRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    protected void initListeners() {
        emailSearch.setCustomerSearchFieldListener(new TextSearchField.CustomerSearchFieldListener() {
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
        boolean result = lastNameSearch.hasMinimumLength(3, true);
        result = firstNameSearch.hasMinimumLength(3, true) && result;
        result = emailSearch.isValidEmail(true) && result;
        return result;
    }


    protected void searchCustomer() {
        if (customerHistoryContainer != null) {
            customerHistoryContainer.setVisibility(View.GONE);
        }
        setResource(new ArrayList<Customer>());
        if (validateForm()) {
            currentMode = ESearchSuggestionMode.SEARCH;
            ActivityHelper.hideSoftKeyboard(this);
            super.onResourceRequest(new ResourceRequestEvent<List<Customer>>(EResourceType.CUSTOMERS));

            Map<String, String> searchParameters = new HashMap<>();
            if (firstNameSearch != null) {
                searchParameters.put("details__first_name", firstNameSearch.getValue());
            }
            if (lastNameSearch != null) {
                searchParameters.put("details__last_name", lastNameSearch.getValue());
            }
            if (emailSearch != null) {
                searchParameters.put("details__email__personal", emailSearch.getValue());
            }

            customerSearchPresenter.searchCustomer(searchParameters);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------


}
