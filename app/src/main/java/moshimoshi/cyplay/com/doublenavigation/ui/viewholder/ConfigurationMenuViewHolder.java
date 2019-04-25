package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.ConfigurationMenu;

/**
 * Created by romainlebouc on 23/11/16.
 */
public class ConfigurationMenuViewHolder extends ItemViewHolder<ConfigurationMenu>{

    // Format : size : 96dp / pading 8dp
//    @BindView(R.id.configuration_menu_icon)
//    ImageView icon;

    @BindView(R.id.configuration_menu_label)
    TextView label;

    private ConfigurationMenu configurationMenu;

    public ConfigurationMenuViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(ConfigurationMenu configurationMenu) {
        this.configurationMenu = configurationMenu;
        label.setText(configurationMenu.getLabel());
//        icon.setImageResource(configurationMenu.getDrawableId());
    }

    @OnClick(R.id.configuration_menu_container)
    public void onConfigurationSelected(){
        Intent intent = new Intent(context, configurationMenu.getActivityClass());
        context.startActivity(intent);
    }
}
