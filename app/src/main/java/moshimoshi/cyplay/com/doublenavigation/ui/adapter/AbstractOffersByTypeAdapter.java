package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Collections;

import java.util.List;
import java.util.Set;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferType;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.OfferTypeViewHolder;

/**
 * Created by romainlebouc on 07/04/2017.
 */

public abstract class AbstractOffersByTypeAdapter<TOfferViewHolder extends ItemViewHolder, TOffer extends IOffer> extends AbstractExpandableItemAdapter<OfferTypeViewHolder, TOfferViewHolder> {
    protected final static int TYPE_CUSTOMER_OFFER = 0;
    protected final static int TYPE_BASKET_OFFER = 1;
    protected final static int TYPE_WEB_OFFER = 2;
    protected final static int TYPE_REGULAR_OFFER = 3;

    private List<AbstractOffersByTypeAdapter.Offers> groupList = new ArrayList<>();
    private Context mContext;

    public AbstractOffersByTypeAdapter(Context context) {
        this.mContext = context;
        setHasStableIds(true);
    }

    public void setItems(List<TOffer> offers) {
        groupList.clear();
        initOffersList(offers);
        sortOffers(offers);
        notifyDataSetChanged();
    }

    private void initOffersList(List<TOffer> offers) {
        Set<EOfferType> types = new HashSet<>();

        for (TOffer item : offers) {
            EOfferType itemType = item.getOffer().getType();
            types.add(itemType);
        }

        for (EOfferType type : types) {
            if (type != null) {
                AbstractOffersByTypeAdapter.Offers o = new AbstractOffersByTypeAdapter.Offers();
                o.type = type;
                o.subOffers = new ArrayList<>();

                groupList.add(o);
            }
        }

        Collections.sort(groupList);
    }

    private void sortOffers(List<TOffer> offers) {
        for (int i = 0; i < offers.size(); i++) {
            EOfferType itemType = offers.get(i).getOffer().getType();
            if (itemType != null && getSubOffersByType(itemType) != null) {
                getSubOffersByType(itemType).add(offers.get(i));
            }
        }
    }

    private List<TOffer> getSubOffersByType(EOfferType type) {
        if (type == null) {
            return null;
        }

        for (AbstractOffersByTypeAdapter.Offers item : groupList) {
            if (type.equals(item.type)) {
                return item.subOffers;
            }
        }
        return null;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        AbstractOffersByTypeAdapter.Offers item = groupList.get(groupPosition);
        if (item.subOffers != null) {
            return item.subOffers.size();
        }
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupList != null ? groupList.get(groupPosition).hashCode() : 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (groupList != null) {
            AbstractOffersByTypeAdapter.Offers item = groupList.get(groupPosition);
            return item.subOffers == null ? 0 : item.subOffers.get(childPosition).hashCode();
        } else {
            return 0;
        }
    }

    @Override
    public OfferTypeViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_offer_type, parent, false);
        return new OfferTypeViewHolder(view);
    }

    @Override
    public abstract TOfferViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindGroupViewHolder(OfferTypeViewHolder holder, int groupPosition, int viewType) {
        holder.setItem(mContext.getString(groupList.get(groupPosition).type.getLabelId()));
    }

    @Override
    public void onBindChildViewHolder(TOfferViewHolder holder, int groupPosition, int childPosition, int viewType) {
        holder.setItem(groupList.get(groupPosition).subOffers.get(childPosition));
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(OfferTypeViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        int itemType = 0;
        switch (groupList.get(groupPosition).type) {
            case CUSTOMER:
                itemType = TYPE_CUSTOMER_OFFER;
                break;
            case BASKET:
                itemType = TYPE_BASKET_OFFER;
                break;
        }
        return itemType;
    }

    class Offers implements Comparable<Offers> {
        EOfferType type;
        List<TOffer> subOffers;

        @Override
        public int compareTo(Offers o) {
            return type.getRelevance() - o.type.getRelevance();
        }
    }
}
