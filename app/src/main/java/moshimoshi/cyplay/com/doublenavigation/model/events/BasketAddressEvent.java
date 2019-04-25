package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Address;

/**
 * Created by wentongwang on 23/03/2017.
 */

public class BasketAddressEvent {
    public static final int CREATE_NEW_ADDRESS = 1;
    public static final int UPDATE_ADDRESS = 2;

    private EAddressType eAddressType;
    private int type;
    private Address address;

    public BasketAddressEvent(int type,
                              EAddressType eAddressType) {
        this.type = type;
        this.eAddressType = eAddressType;
    }

    public int getType() {
        return type;
    }

    public EAddressType geteAddressType() {
        return eAddressType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
