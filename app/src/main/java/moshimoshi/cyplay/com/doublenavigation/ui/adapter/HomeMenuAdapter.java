package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_MenuItem;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.AHomeMenuViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.HomeMenuItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.HomeMenuSeparatorViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

public class HomeMenuAdapter extends RecyclerView.Adapter<AHomeMenuViewHolder> {

    @Inject
    ConfigHelper configHelper;

    private final static int TYPE_ITEM = 0;
    private final static int TYPE_SEPARATOR = 1;

    private Context context;

    // Items
    private List<PR_MenuItem> menuItems;

    // Highlighted item
    private String selectedItem = "&é!$INVALID";

    // Customer identified?
    private CustomerContext customerContext;

    public HomeMenuAdapter(Context ctx) {
        this.context = ctx;
        PlayRetailApp.get(context).inject(this);
    }

    public void setMenuItems(List<PR_MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public void setCustomerContext(CustomerContext customerContext) {
        this.customerContext = customerContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (menuItems != null && menuItems.get(position).getSeparator() != null)
            return TYPE_SEPARATOR;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int listSize = 0;
        if (menuItems != null) {
            int visibleElements = 0;
            for (int i = 0; i < menuItems.size(); i++)
                if (menuItems.get(i).getVisibility())
                    visibleElements++;
            listSize = visibleElements;
        }
        return listSize;
    }

    @Override
    public AHomeMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AHomeMenuViewHolder holder = null;

        View v;
        switch (viewType) {
            case TYPE_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_home_menu_item, parent, false);
                holder = new HomeMenuItemViewHolder(v);
                break;
            case TYPE_SEPARATOR:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_separator, parent, false);
                holder = new HomeMenuSeparatorViewHolder(v);
                break;
        }
        return holder;
    }

    public PR_MenuItem getVisibleItemAtPosition(int position) {
        PR_MenuItem foundItem = null;
        int visibleCount = 0;
        for (int i = 0; i < menuItems.size(); i++) {
            PR_MenuItem item = menuItems.get(i);
            // Check if found the visible item at position P
            if (item.getVisibility() && visibleCount == position) {
                foundItem = item;
                break;
            }
            // increment count of visible item
            if (item.getVisibility())
                visibleCount++;
        }
        return foundItem;
    }

    @Override
    public void onBindViewHolder(AHomeMenuViewHolder holder, int position) {
        PR_MenuItem item = getVisibleItemAtPosition(position);
        if (item != null)
            if (getItemViewType(position) == TYPE_ITEM) {
                ((HomeMenuItemViewHolder) holder).setSelectedItem(selectedItem);
                holder.setItem(item);
            }
    }

}