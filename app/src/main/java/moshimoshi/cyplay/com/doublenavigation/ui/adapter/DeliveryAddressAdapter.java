package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EDeliveryAddressPattern;
import moshimoshi.cyplay.com.doublenavigation.model.custom.ModelsWithType;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.NextButtonEnableHandler;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryAddressHeadViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryAddressItemViewHolder;

import static moshimoshi.cyplay.com.doublenavigation.ui.Constants.TYPE_BILLING_ADDRESS;
import static moshimoshi.cyplay.com.doublenavigation.ui.Constants.TYPE_SHIPPING_ADDRESS;

/**
 * Created by wentongwang on 22/03/2017.
 */

public class DeliveryAddressAdapter extends AbstractExpandableItemAdapter<DeliveryAddressHeadViewHolder, DeliveryAddressItemViewHolder> {

    private final static int SHIPPING_ADDRESS_GROUP_POSITION = 0;
    private final static int BILLING_ADDRESS_GROUP_POSITION = 1;

    private static int DEFAULT_ADDRESS_CHILD_POSITION = 0;

    private List<ModelsWithType<Address>> deliveryAddressList;

    private NextButtonEnableHandler nextButtonEnableHandler;

    private boolean shippingAddressSelected = false;
    private boolean billingAddressSelected = false;

    private EDeliveryAddressPattern showPattern = EDeliveryAddressPattern.SHIPPING_AND_BILLING;

    public DeliveryAddressAdapter(NextButtonEnableHandler handler, EDeliveryAddressPattern pattern) {
        this.nextButtonEnableHandler = handler;
        this.showPattern = pattern;
        setHasStableIds(true);
        initDeliveryAddress();
    }

    private void initDeliveryAddress() {
        deliveryAddressList = new ArrayList<>();

        switch (showPattern) {
            case SHIPPING_AND_BILLING:
                deliveryAddressList.add(shippingAddress());
                deliveryAddressList.add(billingAddress());
                break;
            case SHIPPING:
                deliveryAddressList.add(shippingAddress());
                break;
            case BILLING:
                deliveryAddressList.add(billingAddress());
                break;
        }
    }

    private ModelsWithType<Address> shippingAddress() {
        ModelsWithType<Address> shippingAddress = new ModelsWithType<>();
        shippingAddress.setType(TYPE_SHIPPING_ADDRESS);
        return shippingAddress;
    }

    private ModelsWithType<Address> billingAddress() {
        ModelsWithType<Address> billingAddress = new ModelsWithType<>();
        billingAddress.setType(TYPE_BILLING_ADDRESS);
        return billingAddress;
    }

    public void fillDeliveryAddress(Customer customer) {

        nextButtonEnableHandler.setEnable(false);
        clearDeliveryAddressList();

        fillDefaultAddress(customer);
        if (customer.getAddresses() != null) {
            fillShippingAddress(customer);
            fillBillingAddress(customer);
        }
        notifyDataSetChanged();
    }

    private void clearDeliveryAddressList() {
        for (ModelsWithType model : deliveryAddressList) {
            model.clearModels();
        }
    }

    private void fillDefaultAddress(Customer customer) {
        Address defaultAddress = new Address(customer, EAddressType.DEFAULT);

        if (getAddressListByType(TYPE_SHIPPING_ADDRESS) != null) {
            getAddressListByType(TYPE_SHIPPING_ADDRESS).add(defaultAddress);
        }

        if (getAddressListByType(TYPE_BILLING_ADDRESS) != null) {
            getAddressListByType(TYPE_BILLING_ADDRESS).add(defaultAddress);
        }
    }

    private void fillShippingAddress(Customer customer) {
        if (getAddressListByType(TYPE_SHIPPING_ADDRESS) != null && customer.getAddresses() != null) {
            getAddressListByType(TYPE_SHIPPING_ADDRESS).addAll(customer.getAddresses());
        }
    }

    private void fillBillingAddress(Customer customer) {
        if (getAddressListByType(TYPE_BILLING_ADDRESS) != null && customer.getAddresses() != null) {
            getAddressListByType(TYPE_BILLING_ADDRESS).addAll(customer.getAddresses());
        }
    }

    @Override
    public int getGroupCount() {
        return deliveryAddressList != null ? deliveryAddressList.size() : 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return deliveryAddressList.get(groupPosition).getModels() != null ? deliveryAddressList.get(groupPosition).getModels().size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return deliveryAddressList != null ? deliveryAddressList.get(groupPosition).hashCode() : 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return deliveryAddressList.get(groupPosition).getModels() != null ?
                deliveryAddressList.get(groupPosition).getModels().get(childPosition).hashCode() : 0;
    }

    @Override
    public DeliveryAddressHeadViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_delivery_address_head, parent, false);
        return new DeliveryAddressHeadViewHolder(view);
    }

    @Override
    public DeliveryAddressItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_delivery_address_item, parent, false);
        return new DeliveryAddressItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(DeliveryAddressHeadViewHolder holder, int groupPosition, int viewType) {
        holder.setItem(deliveryAddressList.get(groupPosition).getType());
    }

    @Override
    public void onBindChildViewHolder(final DeliveryAddressItemViewHolder holder, final int groupPosition, final int childPosition, final int viewType) {
        holder.setItem(deliveryAddressList.get(groupPosition).getModels().get(childPosition));
        //the group=0 child=0 is the default address, it can't change
        //holder.setArrowMoreVisibility(childPosition != DEFAULT_ADDRESS_CHILD_POSITION);
        //if this position is the address you chose and the address is valid, select it
        if (holder.isCheckBtnEnable() &&
                deliveryAddressList.get(groupPosition).getCurrentSelectedModelPos() == childPosition) {
            holder.setCheckBtn(true);
            addressSelected(groupPosition);
        } else {
            holder.setCheckBtn(false);
        }


        holder.setOnCheckedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deliveryAddressList.get(groupPosition).getCurrentSelectedModelPos() != childPosition) {
                    //save the address which you chose
                    deliveryAddressList.get(groupPosition).setCurrentSelectedModelPos(childPosition);

                    addressSelected(groupPosition);

                    notifyDataSetChanged();
                }
            }
        });
    }

    private void addressSelected(int groupPosition) {
        switch (showPattern) {
            case BILLING:
                nextButtonEnableHandler.setEnable(true);
                break;
            case SHIPPING:
                nextButtonEnableHandler.setEnable(true);
                break;
            case SHIPPING_AND_BILLING:

                switch (getGroupItemViewType(groupPosition)) {
                    case SHIPPING_ADDRESS_GROUP_POSITION:
                        shippingAddressSelected = true;
                        break;
                    case BILLING_ADDRESS_GROUP_POSITION:
                        billingAddressSelected = true;
                        break;
                }

                nextButtonEnableHandler.setEnable(shippingAddressSelected && billingAddressSelected);
                break;
        }

    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(DeliveryAddressHeadViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }

    public Address getSelectedShippingAddress() {
        for (ModelsWithType<Address> deliveryAddress : deliveryAddressList) {
            if (TYPE_SHIPPING_ADDRESS.equals(deliveryAddress.getType())) {
                return deliveryAddress.getSelectedModel();
            }
        }
        return null;
    }

    public Address getSelectedBillingAddress() {
        for (ModelsWithType<Address> deliveryAddress : deliveryAddressList) {
            if (TYPE_BILLING_ADDRESS.equals(deliveryAddress.getType())) {
                return deliveryAddress.getSelectedModel();
            }
        }
        return null;
    }

    private List<Address> getAddressListByType(String type) {
        for (ModelsWithType<Address> deliveryAddress : deliveryAddressList) {
            if (type.equals(deliveryAddress.getType())) {
                return deliveryAddress.getModels();
            }
        }
        return null;

    }
}
