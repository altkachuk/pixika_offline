package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 09/08/16.
 */
@Parcel
public class PricePerUnit{
    public String unit;
    public Double price;
    public Double crossed_price;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCrossed_price() {
        return crossed_price;
    }

    public void setCrossed_price(Double crossed_price) {
        this.crossed_price = crossed_price;
    }
}