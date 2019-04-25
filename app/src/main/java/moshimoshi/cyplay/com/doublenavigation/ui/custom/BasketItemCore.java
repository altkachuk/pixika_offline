package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.PurchaseCollectionModeStock;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.QuantityStock;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.ThreeSixtyStocks;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.PurchaseCollectionStockSpinnerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SkuQuantitySpinnerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.spinner.PurchaseCollectionStockView;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by romainlebouc on 20/01/2017.
 */

public class BasketItemCore extends LinearLayout {

    private static final String MODE_ONLY_READ = "only_read";
    private static final String MODE_READ_AND_CHANGE = "read_change";

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.purchase_collection_mode_spinner_container)
    View purchaseCollectionModeSpinnerContainer;

    @BindView(R.id.purchase_collection_mode_spinner)
    Spinner purchaseCollectionModeSpinner;

    @BindView(R.id.purchase_collection_mode_spinner_icon)
    ImageView purchaseCollectionModeSpinnerIcon;

    @Nullable
    @BindView(R.id.basket_item_before_discount_price)
    public TextView price;

    @Nullable
    @BindView(R.id.basket_item_after_discount_price)
    public TextView discountPrice;

    @BindView(R.id.sku_quantity_spinner)
    public Spinner skuQuantitySpinner;

    @BindView(R.id.increase_sku_quantity)
    ImageView increaseSkuQuantity;

    @BindView(R.id.product_item_core)
    ProductItemCore productItemCore;

    BasketItem basketItem;

    @BindView(R.id.alterable_quantity_layout)
    View alterableQuantityLayout;

    @BindView(R.id.unalterable_quantity_layout)
    View unAlterableQuantityLayout;

    @BindView(R.id.basket_offers_container)
    View offersContainer;

    @BindView(R.id.sku_quantity_textview)
    TextView tvSkuQuantity;

    @BindView(R.id.basket_item_unit_offer)
    ImageView unitOfferIcon;

    @BindView(R.id.basket_item_percent_offer)
    TextView percentOffer;

    @BindView(R.id.basket_item_amount_offer)
    TextView amountOffer;

    @BindView(R.id.basket_item_container)
    View basketItemContainer;

    private String currentMode = MODE_READ_AND_CHANGE;

    private SkuQuantitySpinnerAdapter skuQuantitySpinnerAdapter;
    private PurchaseCollectionStockSpinnerAdapter purchaseCollectionStockSpinnerAdapter;

    private ThreeSixtyStocks threeSixtyStocks;
    private List<QuantityStock> possibleQuantities = null;

    private BasketItem basketItemOnModification;

    private BasketItemChangeListener basketItemChangeListener;

    private boolean isSelected = false;

    public BasketItemCore(Context context) {
        this(context, null);
    }

    public BasketItemCore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasketItemCore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PlayRetailApp.get(context).inject(this);
        initAttr(context, attrs);
        initView(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BasketItemCore);

        currentMode = ta.getString(R.styleable.BasketItemCore_mode);

        ta.recycle();
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_basket_item_core, this, true);
        ButterKnife.bind(this);
        if (currentMode == null) {
            currentMode = MODE_READ_AND_CHANGE;
        }

        if (currentMode.isEmpty()) {
            currentMode = MODE_READ_AND_CHANGE;
        }
        switch (currentMode) {
            case MODE_ONLY_READ:
                unAlterableQuantityLayout.setVisibility(VISIBLE);
                alterableQuantityLayout.setVisibility(GONE);

                purchaseCollectionModeSpinnerContainer.setClickable(false);
                purchaseCollectionModeSpinner.setClickable(false);
                purchaseCollectionModeSpinner.setEnabled(false);
                purchaseCollectionModeSpinnerIcon.setVisibility(GONE);
                break;
            case MODE_READ_AND_CHANGE:
                unAlterableQuantityLayout.setVisibility(GONE);
                alterableQuantityLayout.setVisibility(VISIBLE);

                purchaseCollectionModeSpinnerContainer.setClickable(true);
                purchaseCollectionModeSpinner.setClickable(true);
                purchaseCollectionModeSpinner.setEnabled(true);
                purchaseCollectionModeSpinnerIcon.setVisibility(VISIBLE);
                break;
        }
    }

    public void setBasketItem(BasketItem basketItem) {
        this.basketItem = basketItem;
        initItem();
    }

    public void setBasketItemWithOutOffers(BasketItem basketItem) {
        this.basketItem = basketItem;
        initItemWithOutOffers();
    }

    public void fillStocks(ThreeSixtyStocks threeSixtyStocks, boolean emptyEnable, BasketItem basketItemOnModification) {
        this.basketItemOnModification = basketItemOnModification;
        if (basketItem != null) {
            this.threeSixtyStocks = threeSixtyStocks;
            this.setUpPurchaseCollectionStockSpinner(basketItem, threeSixtyStocks);
            if (emptyEnable && currentMode.equals(MODE_READ_AND_CHANGE)) {
                productItemCore.setRemoveProductItemListener(new ProductItemCore.RemoveProductItemListener() {
                    @Override
                    public void onRemoveProductItem() {
                        onChangeQuantity(0, null);
                    }
                });
            }
        }
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }

    private void setUpPurchaseCollectionStockSpinner(final BasketItem basketItem, final ThreeSixtyStocks threeSixtyStocks) {
        purchaseCollectionStockSpinnerAdapter = new PurchaseCollectionStockSpinnerAdapter(this.getContext(),
                threeSixtyStocks.getPurchaseCollectionModeStocks(),
                basketItem.getPurchaseCollection().getEPurchaseCollectionMode());
        purchaseCollectionModeSpinner.setAdapter(purchaseCollectionStockSpinnerAdapter);

        purchaseCollectionModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PurchaseCollectionModeStock purchaseCollectionModeStock = threeSixtyStocks.getPurchaseCollectionModeStocks().get(position);
                possibleQuantities = purchaseCollectionModeStock.getQuantityStocks();
                setUpSkuQuantitySpinner(basketItem);
                updateChangeQuantityButtons(purchaseCollectionModeStock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        purchaseCollectionModeSpinner.setSelection(PurchaseCollectionModeStock.getPurchaseCollectionModePosition(threeSixtyStocks.getPurchaseCollectionModeStocks(),
                basketItem.getPurchaseCollection().getEPurchaseCollectionMode()),
                false);

        changeSpinnerTextColor();
    }

    private void updateChangeQuantityButtons(PurchaseCollectionModeStock purchaseCollectionModeStock) {
        boolean canHaveMore = purchaseCollectionModeStock.getMaxPossibleQuantity() > basketItem.getQty();
        increaseSkuQuantity.setClickable(canHaveMore ? true : false);
        increaseSkuQuantity.setAlpha(canHaveMore ? 1f : 0.5f);
    }

    private void setUpSkuQuantitySpinner(final BasketItem basketItem) {

        skuQuantitySpinnerAdapter = new SkuQuantitySpinnerAdapter(this.getContext(),
                possibleQuantities, basketItem.getQty());
        skuQuantitySpinner.setAdapter(skuQuantitySpinnerAdapter);

        skuQuantitySpinner.setOnItemSelectedListener(null);
        skuQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PurchaseCollectionModeStock purchaseCollectionModeStock = (PurchaseCollectionModeStock) purchaseCollectionModeSpinner.getSelectedItem();
                QuantityStock quantityStock = possibleQuantities.get(position);
                if (quantityStock.getQuantity() != basketItem.getQty()
                        || !purchaseCollectionModeStock.getePurchaseCollectionMode().equals(basketItem.getPurchaseCollection().getEPurchaseCollectionMode())) {
                    onChangeQuantity(
                            quantityStock.getQuantity(),
                            threeSixtyStocks.getPurchaseCollection(purchaseCollectionModeStock.getePurchaseCollectionMode()));
                }

                View selectedView = purchaseCollectionModeSpinner.getSelectedView();
                if (selectedView instanceof PurchaseCollectionStockView) {
                    PurchaseCollectionStockView purchaseCollectionStockView = (PurchaseCollectionStockView) selectedView;
                    purchaseCollectionStockView.updateMinStockThresholds(quantityStock.getQuantity(),
                            quantityStock.getQuantity());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        skuQuantitySpinner.setSelection(QuantityStock.getPositionForQuantity(possibleQuantities, basketItem.getQty()), false);
        changeSpinnerTextColor();
    }


    private void initItemWithOutOffers() {
        if (basketItem != null) {

            // Display informations
            Product p = basketItem.getProduct();

            if (p != null) {
                productItemCore.fillProductItem(p, basketItem.getSku_id());
                productItemCore.setItemPrice(configHelper.formatPrice(basketItem.getUnit_price()));
            }

            String priceBuilder = (configHelper.formatPrice(basketItem.getUnit_price() * basketItem.getQty()));
            if (discountPrice != null) {
                discountPrice.setText(priceBuilder);
            }

            tvSkuQuantity.setText(basketItem.getQty() + "");
        }

    }

    private void initItem() {
        if (basketItem != null) {

            // Display informations
            Product p = basketItem.getProduct();

            if (p != null) {
                productItemCore.fillProductItem(p, basketItem.getSku_id());
                productItemCore.setItemPrice(configHelper.formatPrice(basketItem.getUnit_price()));
            }

            initOfferIndicator(basketItem);

            // Unit Price
            if (basketItem.getTotal_discount() != null && basketItem.getTotal_discount() > 0) {
                String priceBuilder = ((basketItem.getItem_total_amount() != null) ?
                        configHelper.formatPrice(basketItem.getItem_total_amount())
                        : "");
                if (price != null) {
                    price.setText(priceBuilder);
                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    price.setVisibility(VISIBLE);
                }

            } else {
                if (price != null) {
                    price.setVisibility(GONE);
                }
            }

            String discountPriceBuilder = ((basketItem.getPriceTotal() != null) ?
                    configHelper.formatPrice(basketItem.getPriceTotal())
                    : "-");
            if (discountPrice != null) {
                discountPrice.setText(discountPriceBuilder);
            }

            tvSkuQuantity.setText(basketItem.getQty() + "");
        }

    }

    private void initOfferIndicator(final BasketItem basketItem) {
        final BasketOffer unitOffer = basketItem.getOfferByDiscountType(EOfferDiscountType.UNIT);
        if (unitOffer != null) {
            unitOfferIcon.setVisibility(VISIBLE);
            CompatUtils.setDrawableTint(unitOfferIcon.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorAccent));
        } else {
            unitOfferIcon.setVisibility(GONE);
        }

        BasketOffer percentOffer = basketItem.getOfferByDiscountType(EOfferDiscountType.PERCENT);
        if (percentOffer != null) {
            String percent = String.valueOf(percentOffer.getOffer().getDiscount_value());
            this.percentOffer.setText(getContext().getString(R.string.product_discount_percent, StringUtils.getPrecentStr(percent)));
            this.percentOffer.setVisibility(VISIBLE);
        } else {
            this.percentOffer.setVisibility(GONE);
        }

        BasketOffer amountOffer = basketItem.getOfferByDiscountType(EOfferDiscountType.AMOUNT);
        if (amountOffer != null) {
            String amount = configHelper.formatPrice(basketItem.getTotal_discount() != null ?
                    basketItem.getTotal_discount() : amountOffer.getOffer().getDiscount_value());
            this.amountOffer.setText(getContext().getString(R.string.product_discount_percent, amount));
            this.amountOffer.setVisibility(VISIBLE);
        } else {
            this.amountOffer.setVisibility(GONE);
        }

        if (basketItem.getItem_offers().size() > 0) {
            offersContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    OfferDetailDialog offerDetailDialog = new OfferDetailDialog(getContext());
                    offerDetailDialog.buildAlertDialog(basketItem.getItem_offers()).show();
                }
            });
        }

    }

    public void setBasketItemChangeListener(BasketItemChangeListener basketItemChangeListener) {
        this.basketItemChangeListener = basketItemChangeListener;
    }

    public void setOnSelectListener(OnClickListener listener) {
        productItemCore.setOnClickListener(listener);
    }

    public interface BasketItemChangeConfirmationListener {
        void onConfirm(boolean result);
    }

    public interface BasketItemChangeListener {
        void onChangeBasketItem(int diff, PurchaseCollection purchaseCollection, final BasketItemCore.BasketItemChangeConfirmationListener listener);
    }


    @OnClick(R.id.decrease_sku_quantity)
    public void onDecreaseItemQuantity() {
        onChangeQuantity(basketItem.getQty() - 1 >= 0 ? basketItem.getQty() - 1 : 0, basketItem.getPurchaseCollection());
    }

    @OnClick(R.id.increase_sku_quantity)
    public void onIncreaseItemQuantity() {
        onChangeQuantity(basketItem.getQty() + 1, basketItem.getPurchaseCollection());
    }

    private void onChangeQuantity(final int quantity, PurchaseCollection purchaseCollection) {

        if (basketItemChangeListener != null) {
            basketItemChangeListener.onChangeBasketItem(quantity, purchaseCollection, new BasketItemChangeConfirmationListener() {
                @Override
                public void onConfirm(boolean confirm) {
                    if (confirm) {
                        double price = quantity * basketItem.getUnit_price();
                        discountPrice.setText(configHelper.formatPrice(price));
                        if (quantity > 0) {
                            int position = QuantityStock.getPositionForQuantity(possibleQuantities, basketItem.getQty());
                            if (position >= 0) {
                                BasketItemCore.this.skuQuantitySpinner.setSelection(position);
                            }
                        }
                    }
                }
            });
        }

    }

    public void setQuantity(int quantity) {
        skuQuantitySpinnerAdapter.setValue(quantity);
    }

    @OnClick(R.id.purchase_collection_mode_spinner_container)
    public void dropDownPurchaseCollectionModeSpinner() {
        purchaseCollectionModeSpinner.performClick();
    }

    public void hideDiscountIndicator() {
        unitOfferIcon.setVisibility(GONE);
        percentOffer.setVisibility(GONE);
        amountOffer.setVisibility(GONE);
        price.setVisibility(GONE);
    }


    public void onSelectStateChange(boolean select) {
        isSelected = select;
        changeBasketItemTextsColor();
        productItemCore.onSelectStateChange(isSelected);
        changeSpinnerTextColor();
    }

    private void changeSpinnerTextColor() {
        if (skuQuantitySpinnerAdapter != null) {
            skuQuantitySpinnerAdapter.changeTextColor(ContextCompat.getColor(getContext(), isSelected ? R.color.white : R.color.brownish_grey));
        }

        if (purchaseCollectionStockSpinnerAdapter != null) {
            purchaseCollectionStockSpinnerAdapter.changeTextColor(ContextCompat.getColor(getContext(), isSelected ? R.color.white : R.color.brownish_grey));
        }
    }

    private void changeBasketItemTextsColor() {

        //background
        basketItemContainer.setBackgroundColor(ContextCompat.getColor(getContext(),
                isSelected ? R.color.colorAccent : R.color.transparent));

        offersContainer.setSelected(isSelected);
        discountPrice.setSelected(isSelected);

        CompatUtils.setDrawableTint(unitOfferIcon.getDrawable(),
                ContextCompat.getColor(getContext(), isSelected ? R.color.white : R.color.colorAccent));

    }
}
