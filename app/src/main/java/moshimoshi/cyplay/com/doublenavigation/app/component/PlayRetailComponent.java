package moshimoshi.cyplay.com.doublenavigation.app.component;

import javax.inject.Singleton;

import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.scope.RuntimeScope;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.DashboardEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.AdyenPosPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketOfferPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CalendarPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CataloguePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CreateCustomerInfoPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerCompletionPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerAddressesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetConfigPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeliveryModesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeviceConfigurationPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetEventsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetSalesHistoryPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.HomeTweetPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ImportPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.SynchronizationPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.OffersPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.SalesPresneterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentTransactionPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductReviewPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSearchPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSuggestionPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProvisionDevicePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ScannerPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.SellerEventsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.UpdateCustomerInfoPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.AdyenTerminalConfigurationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.BasketActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.BasketOffersActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CalendarActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CalendarReportActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ClickAndCollectWebviewActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ConfigurationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SynchronizationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ImportActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SalesHistoryActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SupportActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerAssociationFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerPaymentActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.DeviceRegistrationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenEditTextActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ItemPurchaseConfirmationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.OffersActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.PanelFilteringActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.PaymentDeliveryStepsActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.OffersByTypeActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.PopupCustomerSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductGalleryActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductWithSelectorsActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductsSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.RendezVousInstitutWebViewActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenFilteringActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ScanActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerDashboardActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerLogInActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerLoginWithTemporaryPasswordChangeActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerTeamActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.AddressFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SplashScreenActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.WishlistShareActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BasketBasedActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.BasketItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CalendarEventAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerOffersAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.GalleryPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.HomeMenuAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductSkuSelectorAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductSpecificationAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SaleAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.SellerDashboardTweetAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.AdyenRegistrationDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketItemCore;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.HomeTileView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PaymentSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.ProductItemCore;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.ProductSkuheader;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.SelectableThumbnailView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.SpecSelectorView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormAddressAutoCompleteLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormCityAutoCompleteLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormEditTextInputLayoutLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormEventBusView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormImagesBase64Layout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormPostalCodeAutoFillLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSectionCardView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSelector;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSubView;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketAdyenPaiementFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketOfferChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.BasketPaiementTypeChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CalendarBrowserFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CalendarFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerAutocompleteListFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerInfoFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerPaymentsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerPreviewListFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerSalesCustomNocibeFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerSalesHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerSearchedHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerTimelineFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerWishlistFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.DeliveryAddressFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.DeliveryModesFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.DeviceRegistrationFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.EventListFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.LocalDBFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.PaymentSummaryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.OffersByTypeFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductFilterSortFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.FilterableSortableProductsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.OffersFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductAvailabilityFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductInfosFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductItemFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductItemsRelatedFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductLongTextDescFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductAttributeReviewsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductPictureWithActionFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRelatedAssortmentFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRelatedFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductRetailFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSelectorFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSkuHeaderFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSkuSkusRelatedFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSubstitutionFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSuggestionsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.PurchaseCollectionChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ResourcesFilterMultipleChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ResourcesFilterSingleChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SaleFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SalesFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerDashboardCustomerHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerDashboardStatsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerDashboardTweetsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerLogInFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerLogInWithTempPasswordChangeFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerTeamFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SplashScreenFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.BasketItemOfferViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.BasketItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryAddressHeadViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.DeliveryAddressItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.FeeViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.HomeMenuItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.OffersViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.OrderHeaderViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.PaymentsViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ProductItemGridThumbnailViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ProductItemListThumbnailViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SaleBasketItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SaleViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SkaViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SkuSpecificationSectionViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.StockByShopCategoryViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TicketLineViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.TransactionViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;
import ninja.cyplay.com.apilibrary.domain.component.ExecutorComponent;
import ninja.cyplay.com.apilibrary.domain.interactor.TweetSocketIOInteractorImpl;
import ninja.cyplay.com.apilibrary.models.service.ReportDataAlarmReceiver;

/**
 * Created by damien on 19/04/16.
 */
@RuntimeScope
@Component(
        dependencies = {
                BusComponent.class,
                ApplicationComponent.class,
                RuntimeComponent.class,
                PresenterComponent.class,
                ExecutorComponent.class,
                RepositoryComponent.class,
                InteractorComponent.class,
                CalendarComponent.class,
                DBComponent.class,
                OfflineInteractorComponent.class
        }
)

@Singleton
public interface PlayRetailComponent {

    void inject(BaseFragment baseFragment);

    void inject(BaseActivity baseActivity);

    //TODO Update this list at each inject in new Fragment Or Activity or Anything else
    // Activities
    void inject(SellerDashboardActivity activity);

    void inject(CalendarActivity activity);

    void inject(CatalogActivity activity);

    void inject(FullScreenFilteringActivity activity);

    void inject(PanelFilteringActivity activity);

    void inject(ScanActivity activity);

    void inject(ProductGalleryActivity activity);

    void inject(ProductWithSelectorsActivity activity);

    void inject(CustomerSearchActivity activity);

    void inject(CustomerActivity activity);

    void inject(ProductsSearchActivity activity);

    void inject(CustomerFormActivity activity);

    void inject(AddressFormActivity activity);

    void inject(SplashScreenActivity activity);

    void inject(ImportActivity activity);

    void inject(SellerTeamActivity activity);

    void inject(BasketActivity activity);

    void inject(PaymentDeliveryStepsActivity activity);

    void inject(DeviceRegistrationActivity activity);

    void inject(CalendarReportActivity activity);

    void inject(OffersActivity activity);

    void inject(OffersByTypeActivity activity);

    void inject(WishlistShareActivity activity);

    void inject(SellerLogInActivity activity);

    void inject(AdyenTerminalConfigurationActivity activity);

    void inject(BasketBasedActivity activity);

    void inject(ConfigurationActivity activity);

    void inject(FullScreenEditTextActivity activity);

    void inject(RendezVousInstitutWebViewActivity activity);

    void inject(ClickAndCollectWebviewActivity activity);

    void inject(ItemPurchaseConfirmationActivity activity);

    void inject(SellerLoginWithTemporaryPasswordChangeActivity activity);

    void inject(BasketOffersActivity activity);

    void inject(CustomerPaymentActivity activity);

    void inject(PopupCustomerSearchActivity activity);

    void inject(CustomerAssociationFormActivity activity);

    void inject(SupportActivity activity);

    void inject(SynchronizationActivity activity);

    void inject(SalesHistoryActivity salesHistoryActivity);

    // Fragments
    void inject(SplashScreenFragment fragment);

    void inject(LocalDBFragment fragment);

    void inject(SellerTeamFragment fragment);

    void inject(SellerLogInFragment fragment);

    void inject(CalendarFragment fragment);

    void inject(SellerDashboardTweetsFragment fragment);

    void inject(CalendarBrowserFragment fragment);

    void inject(DeviceRegistrationFragment fragment);

    void inject(EventListFragment fragment);

    void inject(ProductPictureWithActionFragment fragment);

    void inject(ProductRelatedFragment fragment);

    void inject(ProductAvailabilityFragment fragment);

    void inject(ProductSubstitutionFragment fragment);

    void inject(ProductItemFragment fragment);

    void inject(ProductInfosFragment fragment);

    void inject(CustomerAutocompleteListFragment fragment);

    void inject(CustomerSearchedHistoryFragment fragment);

    void inject(CustomerPreviewListFragment fragment);

    void inject(CustomerWishlistFragment fragment);

    void inject(CustomerPaymentsFragment fragment);

    void inject(CustomerInfoFragment fragment);

    void inject(CustomerSalesHistoryFragment fragment);

    void inject(CustomerSalesCustomNocibeFragment fragment);

    void inject(OffersFragment fragment);

    void inject(OffersByTypeFragment fragment);

    void inject(CustomerTimelineFragment fragment);

    void inject(ProductLongTextDescFragment fragment);

    void inject(BasketFragment fragment);

    void inject(BasketAdyenPaiementFragment fragment);

    void inject(SellerDashboardStatsFragment fragment);

    void inject(SellerDashboardCustomerHistoryFragment fragment);

    void inject(ProductSuggestionsFragment fragment);

    void inject(FilterableSortableProductsFragment fragment);

    void inject(ProductFilterSortFragment fragment);

    void inject(ProductItemsRelatedFragment fragment);

    void inject(ProductSkuSkusRelatedFragment fragment);

    void inject(ProductRetailFragment fragment);

    void inject(BasketPaiementTypeChoiceFragment fragment);

    void inject(BasketOfferChoiceFragment fragment);

    void inject(ProductSelectorFragment fragment);

    void inject(ProductAttributeReviewsFragment fragment);

    void inject(ResourcesFilterSingleChoiceFragment fragment);

    void inject(ResourcesFilterMultipleChoiceFragment fragment);

    void inject(ProductSkuHeaderFragment fragment);

    void inject(SellerLogInWithTempPasswordChangeFragment fragment);

    void inject(ProductRelatedAssortmentFragment fragment);

    void inject(PaymentSummaryFragment fragment);

    void inject(PurchaseCollectionChoiceFragment fragment);

    void inject(DeliveryAddressFragment fragment);

    void inject(DeliveryModesFragment fragment);

    void inject(ProductHistoryFragment fragment);

    void inject(CustomerHistoryFragment fragment);

    void inject(SalesFragment salesFragment);

    void inject(SaleFragment saleFragment);

    // Adapter
    void inject(CalendarEventAdapter adapter);

    void inject(SellerDashboardTweetAdapter adapter);

    void inject(CustomerOffersAdapter adapter);

    void inject(ProductSkuSelectorAdapter adapter);

    void inject(HomeMenuAdapter adapter);

    void inject(GalleryPagerAdapter adapter);

    void inject(ProductSpecificationAdapter adapter);

    void inject(SaleAdapter saleAdapter);

    void inject(BasketItemAdapter basketItemAdapter);

    // View Holder

    void inject(BasketItemViewHolder viewHolder);

    void inject(ProductItemGridThumbnailViewHolder viewHolder);

    void inject(SkaViewHolder viewHolder);

    void inject(SkuSpecificationSectionViewHolder viewHolder);

    void inject(OrderHeaderViewHolder viewHolder);

    void inject(TicketLineViewHolder viewHolder);

    void inject(ProductItemListThumbnailViewHolder viewHolder);

    void inject(StockByShopCategoryViewHolder viewHolder);

    void inject(FeeViewHolder viewHolder);

    void inject(HomeMenuItemViewHolder viewHolder);

    void inject(DeliveryAddressHeadViewHolder viewHolder);

    void inject(DeliveryAddressItemViewHolder viewHolder);

    void inject(PaymentsViewHolder viewHolder);

    void inject(TransactionViewHolder viewHolder);

    void inject(OffersViewHolder viewHolder);

    void inject(BasketItemOfferViewHolder viewHolder);

    void inject(SaleViewHolder saleViewHolder);

    void inject(SaleBasketItemViewHolder saleBasketItemViewHolder);


    // Presenters
    void inject(GetConfigPresenterImpl presenter);

    void inject(GetDeviceConfigurationPresenterImpl presenter);

    void inject(GetShopsPresenterImpl presenter);

    void inject(CustomerCompletionPresenterImpl presenter);

    void inject(ProvisionDevicePresenterImpl presenter);

    void inject(AuthenticationPresenterImpl presenter);

    void inject(CataloguePresenterImpl presenter);

    void inject(GetProductsPresenterImpl presenter);

    void inject(GetProductPresenterImpl presenter);

    void inject(ProductSuggestionPresenterImpl presenter);

    void inject(CustomerSearchPresenterImpl presenter);

    void inject(ProductSearchPresenterImpl presenter);

    void inject(ProductSharePresenterImpl presenter);

    void inject(UpdateCustomerInfoPresenterImpl presenter);

    void inject(GetCustomerInfoPresenterImpl presenter);

    void inject(ProductReviewPresenterImpl presenter);

    void inject(CreateCustomerInfoPresenterImpl presenter);

    void inject(GetSalesHistoryPresenterImpl presenter);

    void inject(OffersPresenterImpl presenter);

    void inject(ScannerPresenterImpl presenter);

    void inject(WishlistPresenterImpl presenter);

    void inject(BasketPresenterImpl presenter);

    void inject(BasketOfferPresenterImpl presenter);

    void inject(CustomerAddressesPresenter presenter);

    void inject(HomeTweetPresenterImpl presenter);

    void inject(PaymentPresenterImpl presenter);

    void inject(PaymentTransactionPresenterImpl presenter);

    void inject(CalendarPresenterImpl presenter);

    void inject(SellerEventsPresenterImpl presenter);

    void inject(GetEventsPresenterImpl presenter);

    void inject(FeePresenterImpl presenter);

    void inject(AdyenPosPresenterImpl presenter);

    void inject(GetDeliveryModesPresenter presenter);

    void inject(SalesPresneterImpl presneter);

    void inject(ImportPresenterImpl presenter);

    void inject(SynchronizationPresenterImpl presenter);

    // Runtime component
    void inject(SellerContext dashboardEventList);

    void inject(CalendarEventList calendarEventList);

    void inject(DashboardEventList dashboardEventList);

    // Interactor
    void inject(TweetSocketIOInteractorImpl interactor);

    // Receiver
    void inject(ReportDataAlarmReceiver reportDataAlarmReceiver);

    // Dialog
    void inject(AdyenRegistrationDialog adyenRegistrationDialog);

    // Custom Views
    void inject(ProductSkuheader productSkuheader);

    void inject(BasketItemCore basketItemCore);

    void inject(ProductItemCore productItemCore);

    void inject(PaymentSummaryLayout paymentSummaryLayout);

    void inject(BasketSummaryLayout summaryLayout);

    void inject(SelectableThumbnailView selectableThumbnailView);

    void inject(FormSectionCardView formSectionCardView);

    void inject(FormSelector formSelector);

    void inject(HomeTileView homeTileView);

    void inject(SpecSelectorView specSelectorView);

    void inject(FormAddressAutoCompleteLayout formAddressAutoCompleteLayout);

    void inject(FormSubView formSubView);

    void inject(FormEventBusView formEventBusView);

    void inject(FormPostalCodeAutoFillLayout formPostalCodeAutoFillLayout);

    void inject(FormCityAutoCompleteLayout formCityAutoCompleteLayout);

    void inject(FormEditTextInputLayoutLayout formEditTextInputLayoutLayout);

    void inject(FormImagesBase64Layout layout);

    // Helpers
    void inject(ActionEventHelper actionEventHelper);

}
