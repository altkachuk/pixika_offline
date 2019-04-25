package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 16/11/16.
 */
@Parcel
public class CustomerMeasurementsShoeSize {
    String reference;
    Double value;

    public String getReference() {
        return reference;
    }

    public Double getValue() {
        return value;
    }
}
