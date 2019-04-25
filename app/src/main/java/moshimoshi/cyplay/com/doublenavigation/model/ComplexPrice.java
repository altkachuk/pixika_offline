package moshimoshi.cyplay.com.doublenavigation.model;

/**
 * Created by romainlebouc on 06/01/2017.
 */

public class ComplexPrice {

    Double normalPrice;
    Double discountPrice;

    public void setNormalPrice(Double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getFinalPrice() {
        return discountPrice == null ? normalPrice : discountPrice;
    }

    public Double getPriceBeforeDiscount(){
        return discountPrice !=null ? normalPrice :null;
    }

    public Double getNormalPrice() {
        return normalPrice;
    }
}
