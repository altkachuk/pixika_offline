package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

@Parcel
public class CustomerKpi {

    String segment;
    Float average_basket;
    Integer visit_freq;
    String rfm;
    String main_shop_id;


    public String getSegment() {
        return segment;
    }

    public Float getAverage_basket() {
        return average_basket;
    }

    public Integer getVisit_freq() {
        return visit_freq;
    }

    public String getRfm() {
        return rfm;
    }

    public String getMain_shop_id() {
        return main_shop_id;
    }
}
