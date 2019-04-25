package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import com.adyen.adyenpos.generic.Preferences;
import com.adyen.adyenpos.generic.TerminalConnectionStatus;
import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.DeviceInfo;
import com.adyen.library.TransactionListener;
import com.adyen.library.TransactionRequest;
import com.adyen.library.callbacks.PrintReceiptListener;
import com.adyen.library.callbacks.PrintReceiptRequest;
import com.adyen.library.callbacks.PrintReceiptResponse;
import com.adyen.library.callbacks.SignatureListener;
import com.adyen.library.callbacks.SignatureRequest;
import com.adyen.library.callbacks.SignatureResponse;
import com.adyen.library.callbacks.TerminalConnectionListener;
import com.adyen.library.exceptions.AlreadyProcessingTransactionException;
import com.adyen.library.exceptions.AppNotRegisteredException;
import com.adyen.library.exceptions.InvalidTransactionRequestException;
import com.adyen.library.exceptions.NoDefaultDeviceException;
import com.adyen.library.exceptions.NotYetRegisteredException;
import com.adyen.library.real.LibraryReal;
import com.adyen.library.util.TenderOptions;
import com.adyen.services.posregister.TenderStates;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.AdditionalData;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransactionStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.PR_ShopGeoLocation;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ETransactionStatus;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.adyen.AdyenTransactionCompletionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.adyen.AdyenTransactionProgressEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.adyen.AdyenTransactionStateChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.AdyenPosView;
import moshimoshi.cyplay.com.doublenavigation.view.PaymentTransactionView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.requests.ResourceRequest;

/**
 * Created by romainlebouc on 09/03/2017.
 */

public class AdyenPosPresenterImpl extends BasePresenter implements AdyenPosPresenter {


    @Inject
    protected ConfigHelper configHelper;

    @Inject
    SellerContext sellerContext;

    @Inject
    EventBus bus;

    @Inject
    PaymentTransactionPresenter paymentTransactionPresenter;

    @Inject
    PaymentContext paymentContext;

    private int addCustomerPaymentTransactionRequestId;
    private int updateOnErrorCustomerPaymentTransactionRequestId;

    private AdyenPosView adyenPosView;
    private AdyenLibraryInterface adyenLibraryInterface;

    private final static boolean PRINT_ON_DEVICE = true;


    private PrintReceiptListener printReceiptListener = new PrintReceiptListener() {
        @Override
        public PrintReceiptResponse onPrintReceiptRequested(final PrintReceiptRequest req) {
            PrintReceiptResponse response = new PrintReceiptResponse();
            response.setStatus(com.adyen.services.posregister.PrintReceiptResponse.Status.Printed);
            return response;
        }
    };

    private SignatureListener signatureListener = new SignatureListener() {
        @Override
        public SignatureResponse onSignatureRequested(SignatureRequest signatureRequest) {
            SignatureResponse signatureResponse = new SignatureResponse();
            signatureResponse.setAccepted(true);
            return signatureResponse;
        }

        @Override
        public void onSignatureTimedOut() {
        }

    };


    public AdyenPosPresenterImpl(Context context) {
        super(context);
        paymentTransactionPresenter.setView(new PaymentTransactionView() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<CustomerPaymentTransaction> resourceResponseEvent) {
                if (resourceResponseEvent.getEResourceType() == EResourceType.PAYMENT_TRANSACTION) {
                    if (resourceResponseEvent.getResourceRequestId() == addCustomerPaymentTransactionRequestId) {
                        if (resourceResponseEvent.getResourceException() == null) {
                            adyenPosView.setTransactionStatus(getContext().getString(R.string.starting_transaction));
                            AdyenPosPresenterImpl.this.pay(paymentContext.getPayment(), paymentContext.getCurrentPaymentTransaction());
                        } else {
                            adyenPosView.onCustomerPaymentTransactionCreationFailed();
                        }
                    } else if (resourceResponseEvent.getResourceRequestId() == updateOnErrorCustomerPaymentTransactionRequestId) {
                        // Even if we have an exception, we stop the process
                        // The state of the transaction will be in a incoherent state but we cannot block the seller here
                        adyenPosView.stopPaymentProcessus(EPaymentStatus.PAYMENT_CANCELED);
                    }
                }
            }
        });
    }

    public void init() {
        try {
            adyenLibraryInterface = LibraryReal.getLib();
            if (!PRINT_ON_DEVICE) {
                adyenLibraryInterface.setPrintReceiptListener(printReceiptListener);
            }

            adyenLibraryInterface.setSignatureListener(signatureListener);
        } catch (NotYetRegisteredException e) {
            adyenPosView.onAppNotRegistred();
        }
    }

    @Override
    public DeviceInfo getDefaultPOS() {
        try {
            return adyenLibraryInterface.getDefaultDeviceInfo();
        } catch (NoDefaultDeviceException e) {
            this.onNoDefaultDevice(true);
        }
        return null;
    }

    public AdyenLibraryInterface getAdyenLibraryInterface() {
        return adyenLibraryInterface;
    }

    public void onResume() {
        adyenLibraryInterface.enableEagerConnection();
    }

    public void onPause() {
        adyenLibraryInterface.disableEagerConnection();
    }

    @Override
    public void setView(AdyenPosView view) {
        this.adyenPosView = view;
    }

    public void choosePOS() {
        this.onNoDefaultDevice(false);
    }

    public void pay(Payment payment, CustomerPaymentTransaction customerPaymentTransaction) {
        try {

            final TransactionRequest tr = new TransactionRequest();
            String iso4217Currency = payment.getCurrency_iso4217();
            if (adyenLibraryInterface.getCurrencies().contains(iso4217Currency)) {
                tr.setCurrencyCode(iso4217Currency);
            } else {
                // MANAGE CURRENY EXCEPTION
            }

            tr.setValue((long) (customerPaymentTransaction.getAmount() * 100.0f));
            if (Locale.getDefault() != null) {
                tr.setShopperLocale(Locale.getDefault().toString());
            }

            tr.setCashRegisterMember(sellerContext.getFormatName());

            List<TenderOptions> options = new ArrayList<>();

            if (!PRINT_ON_DEVICE) {
                options.add(TenderOptions.ReceiptHandler);
            }

            PR_ShopGeoLocation geoLocation = configHelper.getCurrentShop().getGeolocation();
            if (geoLocation != null) {
                tr.setLatitude(geoLocation.getLatitude());
                tr.setLongitude(geoLocation.getLongitude());
            }

            tr.setTenderOptions(options);
            tr.setShopperReference(paymentContext.getCustomerId() + "\n"
                    + paymentContext.getPayment().getId() + "\n"
                    + paymentContext.getCurrentTransactionId());
            tr.setVendorDescription(paymentContext.getCurrentTransactionId());

            adyenLibraryInterface.initiateTransaction(tr, new TransactionListener() {
                @Override
                public void onTransactionComplete(CompletedStatus completedStatus, final String s, Integer integer, Map<String, String> map) {
                    try {
                        bus.post(new AdyenTransactionCompletionEvent(completedStatus, s, integer, map));
                        switch (completedStatus) {
                            case APPROVED:
                                bus.post(new PaymentStepEvent(EPaymentStep.BASKET_PAYMENT));

                                CustomerPaymentTransactionStatus customerPaymentTransactionStatus = new CustomerPaymentTransactionStatus();
                                customerPaymentTransactionStatus.setEPaymentStatus(ETransactionStatus.TRANSACTION_PAID);
                                customerPaymentTransactionStatus.setPayment_ref(map != null ? map.get("pspReference") : null);
                                ResourceRequest<CustomerPaymentTransactionStatus> resourceRequest = new ResourceRequest<CustomerPaymentTransactionStatus>()
                                        .id(paymentContext.getCustomerId())
                                        .secondLevelId(paymentContext.getPayment().getId())
                                        .thirdLevelId(paymentContext.getCurrentTransactionId())
                                        .body(customerPaymentTransactionStatus);

                                paymentTransactionPresenter.updateCustomerTransactionPayment(resourceRequest);
                                break;
                            case DECLINED:
                                break;
                            case CANCELLED:
                                break;
                            case ERROR:
                                break;
                            default:
                        }
                    } catch (Exception e) {
                        Log.e(AdyenPosPresenterImpl.class.getName(), "Error onTransactionComplete", e);
                    } finally {
                        adyenPosView.onTransactionStop();
                        paymentContext.setTransactionInProgress(false);
                    }

                }

                @Override
                public void onProgress(ProcessStatus processStatus, final String s) {
                    bus.post(new AdyenTransactionProgressEvent(processStatus, s));
                }

                @Override
                public void onStateChanged(TenderStates tenderStates, final String s, Map<String, String> map) {
                    bus.post(new AdyenTransactionStateChangeEvent(tenderStates, s, map));
                }
            });
            paymentContext.setTransactionInProgress(true);
            adyenPosView.onTransactionStart();
        } catch (NoDefaultDeviceException e) {
            this.onNoDefaultDevice(true);
        } catch (AppNotRegisteredException e) {
            adyenPosView.onAppNotRegistred();
        } catch (InvalidTransactionRequestException e) {
            adyenPosView.onInvalidTransactionRequest();
        } catch (AlreadyProcessingTransactionException e) {
            adyenPosView.onAlreadyProcessingTransaction();
        } catch (Exception e) {
            adyenPosView.onTransactionError();
        }

    }

    private void onNoDefaultDevice(boolean explanation) {
        adyenPosView.chooseDefaultPos(explanation);
    }


    public void startPaiementProcess() {
        DeviceInfo deviceInfo = getDefaultPOS();
        if (deviceInfo != null) {
            this.getAdyenLibraryInterface().registerTerminalConnectionListener(new TerminalConnectionListener() {
                private TerminalConnectionStatus lastStatus = null;

                @Override
                public void onStatusChanged(final TerminalConnectionStatus terminalConnectionStatus, final DeviceInfo deviceInfo, String s) {
                    if (deviceInfo.getDeviceId().equals(deviceInfo.getDeviceId()) && (lastStatus == null || terminalConnectionStatus != lastStatus)) {
                        switch (terminalConnectionStatus) {
                            case CONNECTED:
                                if (deviceInfo != null) {
                                    adyenPosView.setPosStatus(getContext().getString(R.string.terminal_connection_status_connected, deviceInfo.getFriendlyName()),
                                            R.color.success_green);
                                }
                                break;
                            case DISCONNECTED:
                                if (deviceInfo != null) {
                                    adyenPosView.setPosStatus(getContext().getString(R.string.terminal_connection_status_not_connected),
                                            R.color.Gray);
                                }
                                break;
                            case CONNECTING:
                                if (deviceInfo != null) {
                                    adyenPosView.setPosStatus(getContext().getString(R.string.terminal_connection_status_connecting, deviceInfo.getFriendlyName()),
                                            R.color.warning_yellow);
                                }
                                break;
                            case CONNECTING_FAILED:
                                if (deviceInfo != null) {
                                    adyenPosView.setPosStatus(getContext().getString(R.string.terminal_connection_status_connecting_failed, deviceInfo.getFriendlyName()),
                                            R.color.error_red);
                                }
                                break;
                            case ENABLING_BT:
                            case DISABLING_BT:
                            case OFF_BT:
                            case ON_BT:
                                //TODO : how do we manage that ??
                                break;
                            default:
                        }
                        lastStatus = terminalConnectionStatus;
                    }
                }

                @Override
                public void onConnecting(DeviceInfo deviceInfo, int i) {
                }

                @Override
                public void onConnectingFailed(DeviceInfo deviceInfo, String s) {
                }

                @Override
                public void onConnected(DeviceInfo deviceInfo) {
                }

                @Override
                public void onDisconnected(DeviceInfo deviceInfo) {
                }
            });
            initTransaction();
        }
    }

    public void initTransaction() {
        CustomerPaymentTransaction customerPaymentTransaction = new CustomerPaymentTransaction();
        customerPaymentTransaction.setEPaymentType(EPaymentType.CARD_BANK);
        customerPaymentTransaction.setETransactionStatus(ETransactionStatus.TRANSACTION_PENDING);
        customerPaymentTransaction.setAmount(paymentContext.getPayment().getTotal());
        List<AdditionalData> additionalDataList = new ArrayList<>();
        AdditionalData additionalData = new AdditionalData();
        additionalData.setKey(CustomerPaymentTransaction.MERCHANT_ACCOUNT_KEY);
        additionalData.setValue((new Preferences(this.getContext())).getMerchantAccount());
        additionalDataList.add(additionalData);
        customerPaymentTransaction.setAdditional_data(additionalDataList);

        ResourceRequest<CustomerPaymentTransaction> request = new ResourceRequest<>()
                .id(paymentContext.getCustomerId())
                .secondLevelId(paymentContext.getPayment().getId())
                .body(customerPaymentTransaction);
        addCustomerPaymentTransactionRequestId = request.getRequestId();

        paymentTransactionPresenter.addTransactionToCustomerPayment(request);
    }


    public void onTransactionCancelled() {
        paymentContext.getCurrentPaymentTransaction().setETransactionStatus(ETransactionStatus.TRANSACTION_CANCELED);

        final ResourceRequest<CustomerPaymentTransactionStatus> resourceRequest = new ResourceRequest<>()
                .id(paymentContext.getCustomerId())
                .secondLevelId(paymentContext.getPayment().getId())
                .thirdLevelId(paymentContext.getCurrentPaymentTransaction().getId())
                .body(new CustomerPaymentTransactionStatus(paymentContext.getCurrentPaymentTransaction()));
        updateOnErrorCustomerPaymentTransactionRequestId = resourceRequest.getRequestId();
        paymentTransactionPresenter.updateCustomerTransactionPayment(resourceRequest);
    }
}


