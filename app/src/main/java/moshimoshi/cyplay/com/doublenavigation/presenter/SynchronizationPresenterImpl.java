package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import atproj.cyplay.com.dblibrary.util.FileUtil;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.view.SynchronizationView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SaleInteractor;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_Sale;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Contact;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.utils.V1V2Converter;
import ninja.cyplay.com.apilibrary.domain.interactor.SynchronizationInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Order;

/**
 * Created by andre on 18-Mar-19.
 */

public class SynchronizationPresenterImpl extends BasePresenter implements SynchronizationPresenter {

    @Inject
    IDatabaseHandler databaseHandler;

    private CustomerInteractor customerInteractor;
    private SaleInteractor saleInteractor;
    private SynchronizationInteractor synchronizationInteractor;

    private SynchronizationView synchronizationView;

    private List<Contact> contacts;
    private List<Contact> contactSales;

    public SynchronizationPresenterImpl(Context context, CustomerInteractor customerInteractor, SaleInteractor saleInteractor, SynchronizationInteractor synchronizationInteractor) {
        super(context);
        this.synchronizationInteractor = synchronizationInteractor;
        this.customerInteractor = customerInteractor;
        this.saleInteractor = saleInteractor;
    }

    public void setView(SynchronizationView view) {
        synchronizationView = view;
    }

    public void loadSyncData() {
        customerInteractor.getUpdatedCustomers(new AbstractPaginatedResourceRequestCallbackImpl<PR_ACustomer>() {
            @Override
            public void onSuccess(List<PR_ACustomer> resource, Integer count, String previous, String next) {
                contacts = new ArrayList<>();
                for (PR_ACustomer r : resource) {
                    Customer customer = (Customer) r;
                    Contact contact = V1V2Converter.convertCustomer12Customer2(customer, true);
                    contacts.add(contact);
                }

                if (contactSales != null) {
                    synchronizationView.onLoadedSyncDataComlete(contacts.size(), contactSales.size());
                }
            }

            @Override
            public void onError(BaseException e) {
                synchronizationView.onLoadedSyncDataError();
            }
        });

        saleInteractor.getAllSales(new AbstractPaginatedResourceRequestCallbackImpl<PR_Sale>() {
            @Override
            public void onSuccess(List<PR_Sale> resource, Integer count, String previous, String next) {
                contactSales = new ArrayList<>();
                for (PR_Sale r : resource) {
                    Sale sale = (Sale) r;
                    Contact contact = V1V2Converter.convertSale12Customer2(sale);
                    contactSales.add(contact);
                }

                if (contacts != null) {
                    synchronizationView.onLoadedSyncDataComlete(contacts.size(), contactSales.size());
                }
            }

            @Override
            public void onError(BaseException e) {
                synchronizationView.onLoadedSyncDataError();
            }
        });
    }

    public void sendDataIn() {
        HashMap<String, Contact> contactHashMap = new HashMap<>();
        for (Contact contact : contacts) {
            contact.setOrders(new ArrayList<Order>());
            contactHashMap.put(contact.getId(), contact);
        }

        for (Contact contact : contactSales) {
            if (contactHashMap.containsKey(contact.getId())) {
                Contact oldContact = contactHashMap.get(contact.getId());
                List<Order> orders = oldContact.getOrders();
                orders.addAll(contact.getOrders());
                contact.setOrders(orders);
                contact.setIsUpdated(oldContact.getIsUpdated());
            }

            contactHashMap.put(contact.getId(), contact);
        }

        List<Contact> exportContats = new ArrayList<>();
        for (String id : contactHashMap.keySet()) {
            Contact contact = contactHashMap.get(id);
            if (contact.getSource() == null || contact.getSource().equals("null") || contact.getSource().length() == 0)
                contact.setSource("app");
            exportContats.add(contact);
        }

        synchronizationInteractor.sendDataIn(exportContats, new AbstractResourceRequestCallback<List<Contact>>() {
            @Override
            public void onSuccess(List<Contact> contacts) {
                synchronizationView.onExportLoadedCmplete();
            }

            @Override
            public void onError(BaseException e) {
                synchronizationView.onExportLoadedError();
            }
        });
    }

    public void clear() {
        databaseHandler.claerAll();
        //FileUtil.deleteDirFromAppDir(getContext(), "images");
    }
}
