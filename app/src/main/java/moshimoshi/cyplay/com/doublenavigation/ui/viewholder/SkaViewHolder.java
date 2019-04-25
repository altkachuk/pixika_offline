package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;


import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.SkaRestocking;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.StockIndicator;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.StockLabel;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.PhoneUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by damien on 17/04/16.
 */
public class SkaViewHolder extends ItemViewHolder<Ska> {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.ska_container)
    View skaContainer;

    @BindView(R.id.ska_shop_name)
    TextView skaStoreName;

    @Nullable
    @BindView(R.id.stock_indicator)
    StockIndicator stockIndicator;

    @Nullable
    @BindView(R.id.ska_stock_label)
    TextView skaStockLabel;

    @Nullable
    @BindView(R.id.stock_label)
    StockLabel stockLabel;

    @BindView(R.id.ska_stock)
    TextView skaStock;

    @Nullable
    @BindView(R.id.ska_restocking)
    View skaRestocking;

    @Nullable
    @BindView(R.id.last_order_label)
    StockLabel lastOrderLabel;

    @Nullable
    @BindView(R.id.last_order_date)
    TextView lastOrderDate;

    @Nullable
    @BindView(R.id.last_order_quantity)
    TextView lastOrderQuantity;

    @Nullable
    @BindView(R.id.ska_shop_address_container)
    View skaShopAddressContainer;

    @BindView(R.id.ska_shop_address_icon)
    ImageView skaShopAddressIcon;

    @BindView(R.id.ska_shop_address)
    TextView skaShopAddress;

    @Nullable
    @BindView(R.id.ska_landline_phone_container)
    View skaShopLandlinePhoneContainer;

    @BindView(R.id.ska_landline_phone_icon)
    ImageView skaShopLandlinePhoneIcon;

    @BindView(R.id.ska_landline_phone)
    TextView skaShopLandlinePhone;

    @BindView(R.id.ska_shop_distance_container)
    View skaShopDistanceContainer;

    @BindView(R.id.ska_shop_distance)
    TextView skaShopDistance;

    private Sku sku;
    private Ska ska;

    private static final NumberFormat DISTANCE_NUMBER_FORMAT = NumberFormat.getNumberInstance();

    private final DateFormat lastOrderDateFormat;

    static {
        DISTANCE_NUMBER_FORMAT.setMaximumFractionDigits(1);
    }

    public SkaViewHolder(View view) {
        super(view);
        lastOrderDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, context.getResources().getConfiguration().locale);
    }

    @Override
    public void setItem(Ska ska) {
        this.ska = ska;
        if (ska != null) {
            Shop shop = ska.getShop();
            EShopType eShopType = shop != null && shop.getEShopType() != null ? shop.getEShopType() : EShopType.REGULAR;

            setSkaAddress(shop, eShopType);
            setSkaStock(ska, eShopType);

        }
    }

    public void setParentSku(Sku sku) {
        this.sku = sku;
    }

    @SuppressWarnings("ResourceType")
    private void setSkaAddress(Shop shop, EShopType eShopType) {
        skaStoreName.setText(shop != null ? shop.getLong_name() : StringUtils.EMPTY_STRING);

        String phoneNumber = shop != null
                && shop.getContact() != null
                && shop.getContact().getPhone_number() != null ? PhoneUtils.formatPhoneNumber(shop.getContact().getPhone_number()) : null;

        String shopAddress;
        if (ska.getShop() != null && ska.getShop().getMail() != null) {
            shopAddress = shop != null ? shop.getMail().getFormattedAddress() : "-";
        } else {
            shopAddress = "-";
        }
        skaShopAddress.setText(shopAddress);
        switch (eShopType) {
            case REGULAR:
                skaShopAddressContainer.setVisibility(View.VISIBLE);
                skaShopLandlinePhone.setText(phoneNumber != null ? phoneNumber : "-");
                skaShopLandlinePhoneContainer.setVisibility(View.VISIBLE);
                break;
            case WAREHOUSE:
                skaShopAddressContainer.setVisibility(this.context.getResources().getInteger(R.integer.warehouse_web_address_visibility));

                skaShopLandlinePhone.setText(phoneNumber != null ? phoneNumber : StringUtils.EMPTY_STRING);
                skaShopLandlinePhoneContainer.setVisibility(StringUtils.isEmpty(phoneNumber) ?
                        View.GONE :
                        View.VISIBLE);

                break;
            case WEB:
                skaShopAddressContainer.setVisibility(this.context.getResources().getInteger(R.integer.warehouse_web_address_visibility));

                skaShopLandlinePhone.setText(phoneNumber != null ? phoneNumber : StringUtils.EMPTY_STRING);
                skaShopLandlinePhoneContainer.setVisibility(StringUtils.isEmpty(phoneNumber) ? this.context.getResources().getInteger(R.integer.warehouse_web_phone_visibility) : View.VISIBLE);
                break;
        }

        if (ska.getShop() != null) {
            Double distanceInMeter = configHelper.getDistanceFromCurrentShop(ska.getShop());
            if (distanceInMeter != null
                    && ska.getShop() != null
                    && !ska.getShop().getId().equals(configHelper.getCurrentShop().getId())) {
                skaShopDistanceContainer.setVisibility(View.VISIBLE);
                skaShopDistance.setText(this.context.getString(R.string.product_shop_distance, DISTANCE_NUMBER_FORMAT.format(distanceInMeter / 1000)));
            } else {
                skaShopDistanceContainer.setVisibility(View.INVISIBLE);
            }
        } else {
            skaShopDistanceContainer.setVisibility(View.INVISIBLE);
        }

    }

    //TODO : this is linked to NOCIBE : put that in the conf ??
    private void setSkaStock(Ska ska, EShopType eShopType) {
        // Display stock message
        Double stock = ska.getStock() != null ? ska.getStock() : 0;
        switch (eShopType) {
            case REGULAR:
                skaStock.setText(ska.getStringStock());
                if (skaStockLabel != null) {
                    skaStockLabel.setVisibility(View.VISIBLE);
                }

                break;
            case WEB:
            case WAREHOUSE:

                skaStock.setText(stock <= 0 ?
                        this.context.getResources().getString(R.string.product_out_of_stock, ska.getStringStock())
                        : this.context.getResources().getString(R.string.product_in_stock, ska.getStringStock()));

                this.context.getResources().getString(stock <= 0 ? R.string.product_out_of_stock : R.string.product_in_stock, "2");
                if (skaStockLabel != null) {
                    skaStockLabel.setVisibility(View.GONE);
                }
                break;

        }

        if (stockIndicator != null) {
            stockIndicator.setStock(stock.intValue());
        }
        if (stockLabel != null) {
            stockLabel.setStock(stock.intValue());
        }

        if (skaRestocking != null) {
            skaRestocking.setVisibility(ska.getRestocking() != null && ska.getRestocking().size() > 0 ? View.VISIBLE : View.INVISIBLE);
        }


        if (lastOrderDate != null || lastOrderQuantity != null || lastOrderLabel != null) {
            SkaRestocking skaRestocking = getFirstSkaRestockingWithValidDate(ska);
            if (lastOrderDate != null && skaRestocking != null) {
                lastOrderDate.setText(this.context.getString(R.string.last_order_date,
                        lastOrderDateFormat.format(skaRestocking.getReceiving_date())));
            } else {
                lastOrderDate.setText(null);
            }

            if (lastOrderQuantity != null && skaRestocking != null) {
                if (skaRestocking.getQuantity() != null) {
                    lastOrderQuantity.setText(this.context.getString(R.string.last_order_quantity,
                            String.valueOf(skaRestocking.getQuantity().intValue())));
                    lastOrderLabel.setStock(skaRestocking.getQuantity().intValue());
                }
            } else {
                lastOrderQuantity.setText(null);
            }

            if (lastOrderLabel != null) {
                lastOrderLabel.setVisibility(skaRestocking != null ? View.VISIBLE : View.GONE);
            }

        }

        // Highlight when stock are too low
        if (sku != null
                && ska != null
                && sku.getNotify_stock() != null
                && ska.getStock() != null
                && ska.getStock() <= sku.getNotify_stock()) {
            skaContainer.setBackgroundColor(ContextCompat.getColor(this.context, R.color.notify_low_stock));
        } else {
            skaContainer.setBackgroundColor(ContextCompat.getColor(this.context, android.R.color.transparent));
        }
    }


    public static SkaRestocking getFirstSkaRestockingWithValidDate(Ska ska) {
        SkaRestocking result = null;
        if (ska != null
                && ska.getRestocking() != null
                && !ska.getRestocking().isEmpty()
                && ska.getRestocking().get(0).getReceiving_date() != null) {
            result = ska.getRestocking().get(0);
        }
        return result;

    }


}
