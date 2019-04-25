package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_HomeTile;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.HomeTileView;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.content.res.Configuration.ORIENTATION_UNDEFINED;

public class SellerDashboardActivity extends MenuBaseActivity {

    @Inject
    APIValue apiValue;

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.home_grid_view_container)
    RelativeLayout gridViewContainer;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        gridViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridViewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initTiles();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_HOME.getCode();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------


    private void initTiles() {

        if (configHelper != null
                && configHelper.getFeature() != null) {

            List<PR_HomeTile> configTiles;
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == ORIENTATION_UNDEFINED || orientation == ORIENTATION_PORTRAIT) {
                configTiles = configHelper.getConfig().getFeature().getHomeConfig().getGrid().getTiles();
            } else {
                configTiles = configHelper.getConfig().getFeature().getHomeConfig().getGrid().getTiles_landscape();
                if (configTiles == null) {
                    configTiles = configHelper.getConfig().getFeature().getHomeConfig().getGrid().getTiles();
                }
            }

            int configWidth = PR_HomeTile.getGridWidth(configTiles);
            int configHeight = PR_HomeTile.getGridHeight(configTiles);


            if (configTiles != null) {
                for (final PR_HomeTile pr_homeTile : configTiles) {

                    HomeTileView iv = new HomeTileView(this);
                    iv.setItem(pr_homeTile);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMenuAction eMenuAction = EMenuAction.getEMenuActionFromCode(pr_homeTile.getAction());
                            if (eMenuAction != null) {
                                eMenuAction.onActionSelected(SellerDashboardActivity.this, drawerLayout, mainContent, authenticationPresenter);
                            }
                        }
                    });

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            (gridViewContainer.getWidth() * pr_homeTile.getSize().getW()) / configWidth,
                            (gridViewContainer.getHeight() * pr_homeTile.getSize().getH()) / configHeight);
                    params.leftMargin = (gridViewContainer.getWidth() * pr_homeTile.getPosition().getX()) / configWidth;
                    params.topMargin = (gridViewContainer.getHeight() * pr_homeTile.getPosition().getY()) / configHeight;
                    gridViewContainer.addView(iv, params);


                }


            }

        }

    }


    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    protected void applyTintColorToMenuItems(Menu menu, int[] menuItemIds) {
        for (int menuId : menuItemIds) {
            MenuItem menuItem = menu.findItem(menuId);
            if (menuItem != null) {
                if (menuItem.getIcon() != null) {
                    CompatUtils.setDrawableTint(menuItem.getIcon(), ContextCompat.getColor(this, R.color.seller_dashboard_menu_item_color));
                }
            }
        }
    }

}
