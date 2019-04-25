package moshimoshi.cyplay.com.doublenavigation.model.events;

/**
 * Created by wentongwang on 04/04/2017.
 */

public class UpdateCustomerOfferStatusEvent {
    private String id;
    private String offer_id;
    private boolean isActive;

    public UpdateCustomerOfferStatusEvent(boolean isActive) {
        this.isActive = isActive;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public boolean isActive() {
        return isActive;
    }
}
