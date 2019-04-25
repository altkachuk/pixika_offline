package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ConfigurationMenu;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ConfigurationMenuViewHolder;


/**
 * Created by romainlebouc on 23/11/16.
 */
public class ConfigurationItemAdapter extends ItemAdapter<ConfigurationMenu, ConfigurationMenuViewHolder>{

    public ConfigurationItemAdapter(Context context) {
        super(context);
    }

    public ConfigurationMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_configuration_menu, parent, false);
        return new ConfigurationMenuViewHolder(v);
    }
}
