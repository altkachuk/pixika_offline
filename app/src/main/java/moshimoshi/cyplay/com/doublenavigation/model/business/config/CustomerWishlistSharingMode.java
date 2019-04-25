package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EChannel;

/**
 * Created by romainlebouc on 21/09/16.
 */
@Parcel
public class CustomerWishlistSharingMode {

    String channel;
    Boolean selection;

    public String getChannel() {
        return channel;
    }

    public EChannel getEChannel(){
        return EChannel.getEChannelFromCode(channel);
    }

    public Boolean getSelection() {
        return selection;
    }
}
