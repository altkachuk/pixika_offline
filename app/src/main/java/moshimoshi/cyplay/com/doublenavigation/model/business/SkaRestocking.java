package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by romainlebouc on 31/08/16.
 */
@Parcel
public class SkaRestocking implements Comparable<SkaRestocking> {

    String id;
    Date receiving_date;
    Double quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReceiving_date() {
        return receiving_date;
    }

    public void setReceiving_date(Date receiving_date) {
        this.receiving_date = receiving_date;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(SkaRestocking another) {
        if (this.receiving_date == null) {
            if (another.receiving_date == null) {
                return 0; //equal
            }else{
                return -1;} // null is before other strings
        } else {// this.member != null
            if (another.receiving_date == null) {
                return 1;  // all other strings are after null
            }else{
                return this.receiving_date.compareTo(another.receiving_date);}
        }
    }
}
