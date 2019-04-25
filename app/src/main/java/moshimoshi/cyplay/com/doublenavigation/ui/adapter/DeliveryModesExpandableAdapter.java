package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.custom.ModelsWithType;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryModeHeadViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryModeItemViewHolder;

/**
 * Created by wentongwang on 27/03/2017.
 */

public class DeliveryModesExpandableAdapter extends AbstractExpandableItemAdapter<DeliveryModeHeadViewHolder, DeliveryModeItemViewHolder> {

    private List<ModelsWithType<DeliveryMode>> deliveryModesList;

    public DeliveryModesExpandableAdapter() {
        setHasStableIds(true);
    }

    public void init(Set<EPurchaseCollectionMode> modeList) {
        deliveryModesList = new ArrayList<>();
        for (EPurchaseCollectionMode mode : modeList) {
            createNewTypeForMode(mode.getCode());
        }
    }

    private void createNewTypeForMode(String type) {
        ModelsWithType<DeliveryMode> deliveryModes = new ModelsWithType<>();
        deliveryModes.setType(type);
        deliveryModesList.add(deliveryModes);
    }

    public void fillDeliveryModes(List<DeliveryMode> list) {
        for (DeliveryMode deliverMode : list) {
            addDeliveryModeToList(deliverMode);
        }
        notifyDataSetChanged();
    }

    private void addDeliveryModeToList(DeliveryMode deliveryMode) {
        for (String type : deliveryMode.getTarget_delivery_types()) {
            List<DeliveryMode> deliveryModes = getDeliveryModeListByType(EPurchaseCollectionMode.getEDeliveryDestinationTypeFromCode(type));
            if (deliveryModes != null) {
                deliveryModes.add(deliveryMode);
            }
        }
    }


    @Override
    public int getGroupCount() {
        return deliveryModesList != null ? deliveryModesList.size() : 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return deliveryModesList.get(groupPosition).getModels() != null ?
                deliveryModesList.get(groupPosition).getModels().size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public DeliveryModeHeadViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_offer_type, parent, false);
        return new DeliveryModeHeadViewHolder(view);
    }

    @Override
    public DeliveryModeItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_delivery_mode, parent, false);
        return new DeliveryModeItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(DeliveryModeHeadViewHolder holder, int groupPosition, int viewType) {
        holder.setItem(deliveryModesList.get(groupPosition).getType());
    }

    @Override
    public void onBindChildViewHolder(DeliveryModeItemViewHolder holder, final int groupPosition, final int childPosition, int viewType) {
        holder.setItem(deliveryModesList.get(groupPosition).getModels().get(childPosition));

        //if this position is the address you chose, selected it
        holder.setCheckBtn(deliveryModesList.get(groupPosition).getCurrentSelectedModelPos() == childPosition);
        holder.setOnCheckedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deliveryModesList.get(groupPosition).getCurrentSelectedModelPos() != childPosition) {
                    //save the address which you chose
                    deliveryModesList.get(groupPosition).setCurrentSelectedModelPos(childPosition);
                    if (listener != null) {
                        listener.onDeliveryModeChanged(deliveryModesList.get(groupPosition).getSelectedModel());
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(DeliveryModeHeadViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }

    private List getDeliveryModeListByType(EPurchaseCollectionMode type) {
        for (ModelsWithType item : deliveryModesList) {
            if (item.sameType(type.getCode())) {
                return item.getModels();
            }
        }
        return null;
    }

    public DeliveryMode getSelectedDeliveryModeByType(EPurchaseCollectionMode type) {
        DeliveryMode selectedDeliveryMode = null;
        for (ModelsWithType item : deliveryModesList) {
            if (item.sameType(type.getCode())) {
                selectedDeliveryMode = (DeliveryMode) item.getSelectedModel();
            }
        }
        return selectedDeliveryMode;
    }

    private OnDeliveryModeChangedListener listener;

    public void setOnDeliveryModeChangedListener(OnDeliveryModeChangedListener listener) {
        this.listener = listener;
    }

    public interface OnDeliveryModeChangedListener {
        void onDeliveryModeChanged(DeliveryMode deliveryMode);
    }

}
