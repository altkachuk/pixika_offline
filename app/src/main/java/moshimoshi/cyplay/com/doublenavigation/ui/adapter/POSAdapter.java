package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.DeviceInfo;

import org.greenrobot.eventbus.EventBus;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.POSViewHolder;

/**
 * Created by romainlebouc on 24/11/2016.
 */

public class POSAdapter extends ItemAdapter<DeviceInfo, POSViewHolder> {

    private final AdyenLibraryInterface adyenLibraryInterface;
    private final EventBus bus;
    private final boolean chooserMode;

    public POSAdapter(Context context, AdyenLibraryInterface adyenLibraryInterface, EventBus bus, boolean chooserMode) {
        super(context);
        this.adyenLibraryInterface = adyenLibraryInterface;
        this.bus = bus;
        this.chooserMode = chooserMode;
    }

    @Override
    public POSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_pos, parent, false);
        return new POSViewHolder(v, adyenLibraryInterface, bus, chooserMode);
    }
}
