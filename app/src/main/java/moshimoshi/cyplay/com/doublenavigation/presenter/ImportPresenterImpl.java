package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import atproj.cyplay.com.dblibrary.util.FileReader;
import atproj.cyplay.com.dblibrary.util.FileUtil;
import moshimoshi.cyplay.com.doublenavigation.imageloader.ImageLoader;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.utils.V1V2Converter;
import moshimoshi.cyplay.com.doublenavigation.view.ImportView;
import ninja.cyplay.com.apilibrary.domain.interactor.SynchronizationInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Contact;

/**
 * Created by andre on 18-Mar-19.
 */

public class ImportPresenterImpl extends BasePresenter implements ImportPresenter {

    @Inject
    IDatabaseHandler databaseHandler;

    private SynchronizationInteractor synchronizationInteractor;

    private ImportView importView;

    private ImageLoader imageLoader;

    private DecimalFormat formatter = new DecimalFormat("###.00");

    private List<Contact> contacts;
    private List<Sale> sales;

    public ImportPresenterImpl(Context context, SynchronizationInteractor synchronizationInteractor) {
        super(context);
        this.synchronizationInteractor = synchronizationInteractor;

        initImageLoader();
    }

    private void initImageLoader() {
        imageLoader = new ImageLoader(getContext(), new ImageLoader.ImageLoaderListener() {
            @Override
            public void onProcess(final double percentage) {
                double progress = percentage * 100;
                importView.onLoading("Pixika image loading: " + formatter.format(progress) + "%");
            }

            @Override
            public void onComplete() {
                importView.onLoadedCmplete();
            }
        });
        imageLoader.setChunkSize(1000);
    }

    public void setView(ImportView view) {
        importView = view;
    }

    public void loadConfig() {
        if (!databaseHandler.configurationPopulated()) {
            PixicaDbConfigTask dbTask = new PixicaDbConfigTask();
            dbTask.setListener(new OnConfigTaskListener() {
                @Override
                public void onComplete() {
                    ;
                }

                @Override
                public void onError() {
                    ;
                }
            });
            dbTask.execute();
        }
    }

    public void getDataOut() {
        importView.onLoading("Pixika DB start loading");

        synchronizationInteractor.getDataOut(new AbstractResourceRequestCallback<String>() {
            @Override
            public void onSuccess(String text) {
                importView.onLoading("DB is loaded");
                PixicaDbTask dbTask = new PixicaDbTask();
                dbTask.setListener(onDbTaskListener);
                importView.onLoading("DB start populaing");
                dbTask.execute(text);
            }

            @Override
            public void onError(BaseException e) {
                importView.onLoading("Internet connection problem!");
                importView.onLoadedError();
            }
        });

    }

    private OnDbTaskListener onDbTaskListener = new OnDbTaskListener() {
        @Override
        public void onComplete(List<String> images) {
            importView.onLoading("DB is populated");
            importView.onLoading("Pixika Images start loading");
            imageLoader.load(images);
        }

        @Override
        public void onError() {
            databaseHandler.clearBusinessData();
            importView.onLoading("DB loading failed");
            importView.onLoadedError();
        }
    };

    // -------------------------------------------------------------------------------------------
    // Task(s)

    class PixicaDbConfigTask extends AsyncTask<String, Void, Boolean> {
        private OnConfigTaskListener _listener;

        @Override
        protected Boolean doInBackground(String... sources) {
            try {
                String dbSource = FileReader.readTextFromAssets(getContext(), "pixika_configuration.json");
                databaseHandler.populateConfigDB(dbSource);
                return true;
            } catch (Exception e) {
                Log.d("Exception Error", e.getMessage());
            }

            return false;
        }

        public void setListener(OnConfigTaskListener listener) {
            _listener = listener;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (_listener != null) {
                if (result)
                    _listener.onComplete();
                else
                    _listener.onError();
            }
        }
    }

    class PixicaDbTask extends AsyncTask<String, Void, Boolean> {
        private OnDbTaskListener _listener;
        private List<String> images;

        @Override
        protected Boolean doInBackground(String... sources) {
            try {
                images = new ArrayList<>();
                String source = sources[0];
                JSONObject jSource = new JSONObject(source);

                JSONObject shop = new JSONObject("{\"id\": \"MOBILE\",\"short_name\": \"PIXIKA\",\"long_name\": \"PIXIKA\",\"currency\": {  \"symbol\": \"€\",  \"label\": \"EURO\",  \"iso4217\": \"EUR\"},\"mail\": {  \"address1\": \"\",  \"address2\": \"\",  \"address3\": \"\",  \"zipcode\": \"\",  \"city\": \"PARIS\",  \"country\": \"France\"},\"contact\": {  \"phone_number\": \"01 44 54 81 62\",  \"fax_number\": \"\",  \"email\": \"info@pixika.com\"},\"geolocation\": {  \"longitude\": 2.294747,  \"latitude\": 48.858222},\"shop_type\": \"web\",\"active\": true,\"area\": 13200000.0    }");
                databaseHandler.addShop(shop);

                String language = Locale.getDefault().getISO3Language().substring(0,2);

                importView.onLoading("Sellers start populaing");
                JSONArray sellers = jSource.getJSONArray("sellers");
                for (int i = 0; i < sellers.length(); i++) {
                    JSONObject j2Seller = sellers.getJSONObject(i);
                    JSONObject j1Seller = V1V2Converter.sellerV22V1(j2Seller);
                    j1Seller.put("id", i+1);
                    databaseHandler.addSeller(j1Seller);
                }
                importView.onLoading("Sellers is populated");

                importView.onLoading("Customers start populaing");
                JSONArray customers = jSource.getJSONArray("contacts");
                JSONArray customersV1 = new JSONArray();
                for (int i = 0; i < customers.length(); i++) {
                    JSONObject j2Customer = customers.getJSONObject(i);
                    JSONObject j1Customer = V1V2Converter.customerV22V12(j2Customer);//V1V2Converter.customerV22V1(j2Customer);
                    customersV1.put(j1Customer);
                }
                databaseHandler.addCustomers(customersV1);
                importView.onLoading("Customers is populated");

                JSONObject familiesAndProducts = jSource.getJSONObject("products");

                importView.onLoading("Products start populaing");
                JSONObject topFamily = new JSONObject("{     \"id\": 0,     \"name\": [       {         \"lang\": \"en-US\",         \"text\": \"Catalog\"       }     ],     \"has_sub_families\": true   }");
                databaseHandler.addFamily(topFamily);
                JSONArray families = familiesAndProducts.getJSONArray("categories");
                for (int i = 0; i < families.length(); i++) {
                    JSONObject j2Family = families.getJSONObject(i);
                    JSONObject j1Family = V1V2Converter.familyV22V1(j2Family);
                    databaseHandler.addFamily(j1Family);
                }

                JSONArray products = familiesAndProducts.getJSONArray("list");
                for (int i = 0; i < products.length(); i++) {
                    JSONObject j2Product = products.getJSONObject(i);
                    List<String> productImages =  V1V2Converter.getMediasFromProductV2(j2Product);
                    images.addAll(productImages);
                    JSONObject j1Product = V1V2Converter.productV22V1(getContext(), j2Product, language);
                    databaseHandler.addProduct(j1Product);
                }
                importView.onLoading("Products is populated");
                return true;
            } catch (Exception e) {
                Log.d("Exception Error", e.getMessage());
                importView.onLoading(e.getMessage());

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();

                Log.d("Exception Error", sStackTrace);
            }

            return false;
        }

        public void setListener(OnDbTaskListener listener) {
            _listener = listener;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (_listener != null) {
                if (result)
                    _listener.onComplete(images);
                else
                    _listener.onError();
            }
        }
    }

    // Task(s)
    // -------------------------------------------------------------------------------------------

    interface OnConfigTaskListener {
        void onComplete();
        void onError();
    }

    interface OnDbTaskListener {
        void onComplete(List<String> images);
        void onError();
    }
}
