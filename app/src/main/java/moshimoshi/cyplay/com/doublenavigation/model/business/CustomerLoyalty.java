package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Date;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ECustomerLoyaltyState;

/**
 * Created by romainlebouc on 29/09/16.
 */
@Parcel
public class CustomerLoyalty {
    String id;
    String program;
    Double points;
    String state;
    Date start_date;
    Date end_date;

    public String getId() {
        return id;
    }

    public String getProgram() {
        return program;
    }

    public String getActiveProgram() {
        ECustomerLoyaltyState eCustomerLoyaltyState = getECustomerLoyaltyState();
        if (eCustomerLoyaltyState == null || eCustomerLoyaltyState != ECustomerLoyaltyState.ACTIVE) {
            return null;
        } else {
            return program;
        }

    }

    public Double getPoints() {
        return points;
    }

    public String getState() {
        return state;
    }

    public ECustomerLoyaltyState getECustomerLoyaltyState() {
        return ECustomerLoyaltyState.getECustomerLoyaltyState(this.state);
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
