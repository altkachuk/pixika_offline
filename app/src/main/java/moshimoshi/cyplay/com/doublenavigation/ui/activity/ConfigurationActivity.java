package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ConfigurationMenu;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ConfigurationItemAdapter;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;


/**
 * Created by romainlebouc on 23/11/16.
 */
public class ConfigurationActivity extends MenuBaseActivity {

    private ConfigurationItemAdapter adapter;

    @BindView(R.id.configuration_recycler_view)
    RecyclerView configurationRecyclerView;

    static List<ConfigurationMenu> CONFIGURATION_MENU_LIST = new ArrayList<>();

    static {
        ConfigurationMenu CONFIGURATION_MENU_PAIEMENT = new ConfigurationMenu();
        CONFIGURATION_MENU_PAIEMENT.setDrawableId(R.drawable.ic_point_of_sale_terminal_pos);
        CONFIGURATION_MENU_PAIEMENT.setLabel(R.string.payment_terminal);
        CONFIGURATION_MENU_PAIEMENT.setActivityClass(AdyenTerminalConfigurationActivity.class);
        CONFIGURATION_MENU_LIST.add(CONFIGURATION_MENU_PAIEMENT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        adapter = new ConfigurationItemAdapter(this);
        adapter.setItems(CONFIGURATION_MENU_LIST);
        initRecyclerView();
    }

    private void initRecyclerView() {

        int spacing = getResources().getDimensionPixelOffset(R.dimen.contextual_default_margin);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // set layout
        configurationRecyclerView.setLayoutManager(layoutManager);
        // optimization
        configurationRecyclerView.setHasFixedSize(true);
        // Add Decorator spacing between items
//        configurationRecyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.configuration_columns_count), spacing, false));
        // Add Animation
//        recyclerView.setItemAnimator(new FadeInAnimator());
        // link view with Adapter
        configurationRecyclerView.setAdapter(adapter);

    }

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_CONFIGURATION.getCode();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }
}
