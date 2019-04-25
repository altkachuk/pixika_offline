package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.BasketItemWithStock;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.PurchaseCollectionBasketItems;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.BasketItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.PurchaseCollectionViewHolder;

/**
 * Created by romainlebouc on 27/01/2017.
 */

public class BasketExpandableAdapter extends AbstractExpandableItemAdapter<PurchaseCollectionViewHolder, BasketItemViewHolder> {


    List<PurchaseCollectionBasketItems> purchaseCollectionBasketItemsList;
    List<BasketItem> selectedBasketItemList;
    private BasketPresenter basketPresenter;
    private BasketItem itemOnModification;

    public BasketExpandableAdapter(BasketPresenter basketPresenter) {
        this.basketPresenter = basketPresenter;
        selectedBasketItemList = new ArrayList<>();
        setHasStableIds(true);
    }

    public void setItemOnModification(BasketItem itemOnModification) {
        this.itemOnModification = itemOnModification;
    }

    public void setItems(List<PurchaseCollectionBasketItems> purchaseCollectionBasketItemsList) {
        this.purchaseCollectionBasketItemsList = purchaseCollectionBasketItemsList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return purchaseCollectionBasketItemsList != null ? purchaseCollectionBasketItemsList.size() : 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return purchaseCollectionBasketItemsList != null ? purchaseCollectionBasketItemsList.get(groupPosition).getBasketItemWithStocks().size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // This method need to return unique value within all group items.
        return purchaseCollectionBasketItemsList.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // This method need to return unique value within the group.
        return purchaseCollectionBasketItemsList.get(groupPosition).getBasketItemWithStocks().get(childPosition).hashCode();
    }

    @Override
    public PurchaseCollectionViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_purchase_collection, parent, false);
        return new PurchaseCollectionViewHolder(view);
    }

    @Override
    public BasketItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_basket_item, parent, false);
        return new BasketItemViewHolder(view, basketPresenter);
    }

    @Override
    public void onBindGroupViewHolder(PurchaseCollectionViewHolder holder, int groupPosition, int viewType) {
        PurchaseCollection purchaseCollection = purchaseCollectionBasketItemsList.get(groupPosition).getPurchaseCollection();
        if (purchaseCollection != null) {
            holder.setItem(purchaseCollection);
        }
    }

    @Override
    public void onBindChildViewHolder(final BasketItemViewHolder holder, int groupPosition, int childPosition, int viewType) {
        final BasketItemWithStock basketItemWithStock = purchaseCollectionBasketItemsList.get(groupPosition).getBasketItemWithStocks().get(childPosition);
        if (basketItemWithStock != null) {
            final BasketItem basketItem = basketItemWithStock.getBasketItem();
            holder.setItem(basketItemWithStock, itemOnModification);
            holder.onSelectStateChange(selectedBasketItemList.contains(basketItem));
            holder.setOnSelectListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedBasketItemList.contains(basketItem)) {
                        holder.onSelectStateChange(false);
                        selectedBasketItemList.remove(basketItem);
                    } else {
                        holder.onSelectStateChange(true);
                        selectedBasketItemList.add(basketItem);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(PurchaseCollectionViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public List<BasketItem> getSelectedBasketItems() {
        return this.selectedBasketItemList;
    }

    public void clearSelectedItems() {
        selectedBasketItemList.clear();
    }
}
