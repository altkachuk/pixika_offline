package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;

import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_MenuItem;

public class HomeMenuSeparatorViewHolder extends AHomeMenuViewHolder {

    public HomeMenuSeparatorViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void setItem(PR_MenuItem pr_menuItem) {

    }

}