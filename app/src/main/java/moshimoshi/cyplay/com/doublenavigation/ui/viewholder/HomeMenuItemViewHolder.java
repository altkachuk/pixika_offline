package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;


import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_MenuItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.MenuActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.EMenuAction;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

public class HomeMenuItemViewHolder extends AHomeMenuViewHolder {

    @Inject
    EventBus bus;

    @Inject
    ConfigHelper configHelper;

    @Inject
    CustomerContext customerContext;

    @Nullable
    @BindView(R.id.highlight_bar)
    public View highlightBar;

    @Nullable
    @BindView(R.id.home_menu_icon)
    public ImageView icon;

    @Nullable
    @BindView(R.id.home_menu_title)
    public TextView title;

    private PR_MenuItem item;
    private String selectedItem;

    public HomeMenuItemViewHolder(View itemView) {
        super(itemView);
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setItem(PR_MenuItem item) {
        String itemText = StringUtils.getStringResourceByName(context,item.getLabel());
        title.setText(itemText);
        this.item = item;

        int id = this.context.getResources().getIdentifier(item.getIcon(), "drawable", this.context.getPackageName());
        if (id > 0) {
            Picasso.get()
                    .load(id)
                    .noFade()
                    .into(icon);
        } else {
            Picasso.get()
                    .load(ImageUtils.getIconUrl(this.context, item.getIcon(), configHelper))
                    .into(icon);
        }


        // Show customer name on the right item
        if (EMenuAction.NAVDRAWER_ITEM_CUSTOMER_IDENTIFIED.getCode().equals(item.getTag())) {
            if (customerContext != null && customerContext.isCustomerIdentified() && customerContext.getCustomer() != null)
                title.setText(customerContext.getCustomer().getDetails().getFormatName(this.context));
        }
        // Highlight current item
        if (selectedItem != null && selectedItem.endsWith(item.getTag())) {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            icon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
            highlightBar.setVisibility(View.VISIBLE);
        } else {
            // un-Highlight
            title.setTextColor(ContextCompat.getColor(context, R.color.black_2));
            icon.setColorFilter(ContextCompat.getColor(context, R.color.grey_1));
            highlightBar.setVisibility(View.INVISIBLE);
        }

    }

    @OnClick(R.id.root_view)
    public void onClick() {
        bus.post(new MenuActionEvent(item.getTag()));
    }
}
