package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.PaymentDeliveryItem;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.PurchaseCollectionViewHolder;

/**
 * Created by wentongwang on 15/03/2017.
 */

public class DeliveryItemExpandableAdapter extends AbstractExpandableItemAdapter<PurchaseCollectionViewHolder, DeliveryItemViewHolder> {

    private List<PaymentDeliveryItem> paymentDeliveryItems;

    public DeliveryItemExpandableAdapter(){
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        if (paymentDeliveryItems != null) {
            return paymentDeliveryItems.size();
        }
        return 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (paymentDeliveryItems != null) {
            if (paymentDeliveryItems.get(groupPosition).getItems() != null) {
                return paymentDeliveryItems.get(groupPosition).getItems().size();
            }
        }
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        if (paymentDeliveryItems != null) {
            return paymentDeliveryItems.get(groupPosition).hashCode();
        }
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (paymentDeliveryItems != null) {
            if (paymentDeliveryItems.get(groupPosition).getItems() != null) {
                return paymentDeliveryItems.get(groupPosition).getItems().get(childPosition).hashCode();
            }
        }
        return 0;
    }

    @Override
    public PurchaseCollectionViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_purchase_collection, parent, false);
        return new PurchaseCollectionViewHolder(view);
    }

    @Override
    public DeliveryItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_paiement_summary_item, parent, false);
        return new DeliveryItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(PurchaseCollectionViewHolder holder, int groupPosition, int viewType) {
        holder.setItem(paymentDeliveryItems.get(groupPosition).getDelivery());
    }

    @Override
    public void onBindChildViewHolder(DeliveryItemViewHolder holder, int groupPosition, int childPosition, int viewType) {
        holder.setItem(paymentDeliveryItems.get(groupPosition).getItems().get(childPosition));
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(PurchaseCollectionViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }


    public void setPaymentDeliveryItems(List<PaymentDeliveryItem> list) {
        paymentDeliveryItems = new ArrayList<>();
        paymentDeliveryItems.addAll(list);
        notifyDataSetChanged();
    }
}
