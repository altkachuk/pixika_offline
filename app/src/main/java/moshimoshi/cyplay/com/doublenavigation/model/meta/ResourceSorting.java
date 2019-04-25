package moshimoshi.cyplay.com.doublenavigation.model.meta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.utils.ListUtils;
import ninja.cyplay.com.apilibrary.models.meta.ESortingWay;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by romainlebouc on 18/08/16.
 */
public class ResourceSorting<Resource> {


    // TODO : put that in the configuration on the server
    public static final List<ResourceFieldSorting> PRODUCT_SORTING = new ArrayList<>();

    public final static ResourceFieldSorting PRODUCT_SKUS__POPULARITY_SORTING = new ResourceFieldSorting("skus__popularity", R.string.filtering_relevance, ESortingWay.DESC);

    public static final ResourceSorting<Ticket> TICKET_SORTING = new ResourceSorting<>();

    public static final ResourceSorting<Ticket> OFFER_SORTING = new ResourceSorting<>();

    static {
        /**
         * Products
         */
        // Default sorting : server decides the best sorting !!
        PRODUCT_SORTING.add(PRODUCT_SKUS__POPULARITY_SORTING);

        PRODUCT_SORTING.add(new ResourceFieldSorting("brand", R.string.brand_filtering_asc, ESortingWay.ASC));
        PRODUCT_SORTING.add(new ResourceFieldSorting("brand", R.string.brand_filtering_desc, ESortingWay.DESC));

        PRODUCT_SORTING.add(new ResourceFieldSorting("skus__availabilities__price", R.string.price_filtering_asc, ESortingWay.ASC));
        PRODUCT_SORTING.add(new ResourceFieldSorting("skus__availabilities__price", R.string.price_filtering_desc, ESortingWay.DESC));

        /**
         * Tickets
         */
        TICKET_SORTING.getFields().add(new ResourceFieldSorting("purchase_date", R.string.purchase_date_filtering_desc, ESortingWay.DESC, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket lhs, Ticket rhs) {
                return -ListUtils.compare(lhs.getPurchaseDate(), rhs.getPurchaseDate());
            }
        }));
        TICKET_SORTING.getFields().add(new ResourceFieldSorting("purchase_date", R.string.purchase_date_filtering_asc, ESortingWay.ASC, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket lhs, Ticket rhs) {
                return ListUtils.compare(lhs.getPurchaseDate(), rhs.getPurchaseDate());
            }
        }));

        TICKET_SORTING.getFields().add(new ResourceFieldSorting("items__qty", R.string.purchase_items_quantity_filtering_desc, ESortingWay.DESC, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket lhs, Ticket rhs) {
                return -ListUtils.compare(lhs.getItems() != null && !lhs.getItems().isEmpty() ? lhs.getItems().get(0).getQty() : 0,
                        rhs.getItems() != null && !rhs.getItems().isEmpty() ? rhs.getItems().get(0).getQty() : 0);
            }
        }));

        TICKET_SORTING.getFields().add(new ResourceFieldSorting("items__qty", R.string.purchase_items_quantity_filtering_asc, ESortingWay.ASC, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket lhs, Ticket rhs) {
                return ListUtils.compare(lhs.getItems() != null && !lhs.getItems().isEmpty() ? lhs.getItems().get(0).getQty() : 0,
                        rhs.getItems() != null && !rhs.getItems().isEmpty() ? rhs.getItems().get(0).getQty() : 0);
            }
        }));

        /**
         * Offers
         */
        OFFER_SORTING.getFields().add(new ResourceFieldSorting("type", R.string.filtering_relevance, ESortingWay.ASC, new Comparator<IOffer>() {
            @Override
            public int compare(IOffer iLhs, IOffer iRhs) {
                Offer lhs = iLhs.getOffer();
                Offer rhs = iRhs.getOffer();
                int compare =  ListUtils.compare(lhs!=null && lhs.getType() != null ? lhs.getType().getRelevance() : Integer.MAX_VALUE,
                        rhs!=null && rhs.getType() != null ? rhs.getType().getRelevance() : Integer.MAX_VALUE);

                if (compare == 0){
                    if ( lhs!=null && rhs!=null){
                        return ListUtils.compare(lhs.getTo_date(), rhs.getTo_date());
                    }else{
                        return 0;
                    }
                }else{
                    return compare;
                }


            }
        }));

        OFFER_SORTING.getFields().add(new ResourceFieldSorting("to_date", R.string.offer_to_date_filtering_desc, ESortingWay.DESC, new Comparator<IOffer>() {
            @Override
            public int compare(IOffer iLhs, IOffer iRhs) {
                Offer lhs = iLhs.getOffer();
                Offer rhs = iRhs.getOffer();
                return -ListUtils.compare(lhs.getTo_date(), rhs.getTo_date());
            }
        }));

        OFFER_SORTING.getFields().add(new ResourceFieldSorting("to_date", R.string.offer_to_date_filtering_asc, ESortingWay.ASC, new Comparator<IOffer>() {
            @Override
            public int compare(IOffer iLhs, IOffer iRhs) {
                Offer lhs = iLhs.getOffer();
                Offer rhs = iRhs.getOffer();
                return ListUtils.compare(lhs.getTo_date(), rhs.getTo_date());
            }
        }));

    }

    private final List<ResourceFieldSorting> fields = new ArrayList<>();

    public List<ResourceFieldSorting> getFields() {
        return fields;
    }

}
