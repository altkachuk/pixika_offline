package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;


/**
 * Created by wentongwang on 24/04/2017.
 */

public class PopupCustomerSearchActivity extends CustomerSearchActivity {

    private RecyclerItemClickListener itemClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*Toolbar actionBar = getActionBarToolbar();
        if (actionBar != null) {
            actionBar.setNavigationIcon(R.drawable.ic_close_black_24dp);
            setSupportActionBar(actionBar);
        }*/
    }

    /*protected boolean isLeftCrossClosable() {
        return false;
    }*/

    @Override
    protected boolean isLeftCrossClosable() {
        return true;
    }

    @Override
    protected boolean menuItemsVisible() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finishCustomerSearch();
                return true;
            case R.id.action_basket:
                setResult(Activity.RESULT_CANCELED);
                finishCustomerSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initPager() {
        super.initPager();
        customersRecyclerView.removeOnItemTouchListener(itemClickListener);
        itemClickListener = new RecyclerItemClickListener(this, new CustomerSearchItemClick());
        customersRecyclerView.addOnItemTouchListener(itemClickListener);
    }

    private class CustomerSearchItemClick implements RecyclerItemClickListener.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            if (customers != null && customers.size() > position) {
                customerContext.setCustomerId(customers.get(position).getId());
                setResult(Activity.RESULT_OK);
                finishCustomerSearch();
            }
        }
    }

    private void finishCustomerSearch() {
        supportFinishAfterTransition();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }
}
