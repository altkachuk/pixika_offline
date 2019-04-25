package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 16/11/16.
 */
@Parcel
public class CustomerMeasurements {

    CustomerMeasurementsShoeSize shoe_size;

    public CustomerMeasurementsShoeSize getShoe_size() {
        return shoe_size;
    }

    public CustomerMeasurements() {
        this.shoe_size = new CustomerMeasurementsShoeSize();
    }
}
