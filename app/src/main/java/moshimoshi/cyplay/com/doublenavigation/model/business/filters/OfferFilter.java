package moshimoshi.cyplay.com.doublenavigation.model.business.filters;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;

/**
 * Created by romainlebouc on 05/09/16.
 */
@Parcel
public class OfferFilter extends ResourceFilter<OfferFilter,OfferFilterValue> {

    public List<OfferFilterValue> values;

    @Override
    public ResourceFilter copyWithValue(ResourceFilterValue filterValue) {
        OfferFilter filter = new OfferFilter();
        filter.label = this.label;
        filter.key = this.key;
        filter.level = this.level;
        filter.setValues(  new ArrayList<OfferFilterValue>());
        filter.getValues().add((OfferFilterValue)filterValue);
        return filter;
    }

    public List<OfferFilterValue> getValues() {
        return values;
    }

    public void setValues(List<OfferFilterValue> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OfferFilter that = (OfferFilter) o;

        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return level != null ? level.equals(that.level) : that.level == null;

    }

    /*public static List<Offer> filterOffers(List<Offer> offers, List<OfferFilter> offerFilters){
        List<Offer> result = new ArrayList<>(offers);
        if (offerFilters != null && !offerFilters.isEmpty()) {
            for (final OfferFilter filter : offerFilters) {
                if (filter.getValues() != null) {
                    for (final OfferFilterValue filterValue : filter.getValues()) {
                        // do filtering over the same list
                        result = FluentIterable.from(result).filter(new Predicate<Offer>() {
                            public boolean apply(Offer input) {
                                return filterValue.getKey().equals(ReflectionUtils.get(input, filter.getKey()));
                            }
                        }).toList();
                    }
                }

            }
        }
        // Because FluentIterable is sending back a immutable List
        return  new ArrayList(result);
    }*/




    /*
    public static List<Offer> filterOffers(List<Offer> offers, List<OfferFilter> offerFilters) {

        if (offerFilters != null && !offerFilters.isEmpty()) {
            List<Offer> result = new ArrayList<>();
            for (OfferFilter offerFilter : offerFilters) {
                if ("shops__shop_type".equals(offerFilter.getKey())) {
                    filterShopType(offerFilter, result, offers);
                }
            }
            return result;
        } else {
            return offers;
        }

    }


    private static void filterShopType(OfferFilter offerFilter, List<Offer> filteredOffer, List<Offer> offers) {
        for (OfferFilterValue offerFilterValue : offerFilter.getValues()) {
            String key = offerFilterValue.getKey();

            for (Offer offer : offers) {
                boolean include = false;
                for (Shop shop : offer.getShops()) {
                    if (key.equals(shop.getShop_type())) {
                        include = true;
                    }
                }
                if (include) {
                    filteredOffer.add(offer);
                }
            }
        }
    }*/
}
