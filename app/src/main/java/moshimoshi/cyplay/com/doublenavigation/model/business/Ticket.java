package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ATicket;

@Parcel
@ModelResource
public class Ticket extends PR_ATicket implements ProductItem {

    String customer_id;
    String seller_id;
    Double total;
    Date purchase_date;
    String shop_id;

    @ReadOnlyModelField
    Shop shop;

    @ReadOnlyModelField
    Seller seller;

    String channel;

    public List<TicketLine> items;

    public String getCustomer_id() {
        return customer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public Double getTotal() {
        return total;
    }

    public List<TicketLine> getItems() {
        return items;
    }

    public Date getPurchaseDate() {
        return purchase_date;
    }

    public Shop getShop() {
        return shop;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    public Product getProduct() {
        if (items != null && items.size() > 0) {
            return items.get(0).getProduct();
        }
        return null;
    }

    @Override
    public String getSkuId() {
        if (items != null && items.size() > 0) {
            return items.get(0).getSku_id();
        }
        return null;
    }

    @Override
    public Date getDate() {
        return purchase_date;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public static int getTicketCountByChannel(List<Ticket> offerList, String type) {
        int count = 0;
        if (offerList != null && type != null) {
            for (int i = 0; i < offerList.size(); i++)
                if (type.equals(offerList.get(i).getChannel()))
                    count++;
        }
        return count;
    }
}