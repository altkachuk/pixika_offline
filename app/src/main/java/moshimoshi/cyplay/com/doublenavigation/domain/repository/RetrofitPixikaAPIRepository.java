package moshimoshi.cyplay.com.doublenavigation.domain.repository;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CipherSuite;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.TlsVersion;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import ninja.cyplay.com.apilibrary.domain.repository.PlayRetailRequestInterceptor;
import ninja.cyplay.com.apilibrary.domain.repository.RetrofitPlayRetailAPIRepository;
import ninja.cyplay.com.apilibrary.domain.repository.RetrofitPlayRetailService;
import ninja.cyplay.com.apilibrary.models.abstractmodels.WSReport;
import ninja.cyplay.com.apilibrary.models.business.reporting.ReportData;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;
import ninja.cyplay.com.apilibrary.utils.RealmHelper;
import ninja.cyplay.com.apilibrary.utils.SafeOkHttpClientUtil;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by andre on 22-Mar-19.
 */

public class RetrofitPixikaAPIRepository extends RetrofitPlayRetailAPIRepository {

    public RetrofitPixikaAPIRepository(Context context,
                                       String endpoint,
                                       RequestInterceptor requestInterceptor,
                                       APIValue apiValue) {
        super(context, endpoint, requestInterceptor, apiValue);
    }

    @Override
    protected void init() {
        OkHttpClient safeOkHttpClient = new OkHttpClient();

        safeOkHttpClient.setRetryOnConnectionFailure(true);

        int cacheSize = 128 * 1024 * 1024; // 64 MiB
        File cacheDirectory = new File(context.getCacheDir().getAbsolutePath(), "HttpCache");
        Cache cache = new Cache(cacheDirectory, cacheSize);
        safeOkHttpClient.setCache(cache);

        //TODO Change remove this or Define real results
        safeOkHttpClient.setConnectTimeout(120, TimeUnit.SECONDS); // connect timeout
        safeOkHttpClient.setReadTimeout(120, TimeUnit.SECONDS);    // socket timeout

        safeOkHttpClient.networkInterceptors().add(new AuthenticationInterceptor(apiValue));
        safeOkHttpClient.setAuthenticator(new TokenAuthenticator(apiValue));

        // Interceptor for reporting webservices
        safeOkHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                String requestTime = request.header(PlayRetailRequestInterceptor.REQUEST_TIME_PARAM);
                String reportId = response.header(WSReport.REPORT_ID_KEY);
                if (requestTime != null && reportId != null) {
                    try {
                        double totalTime = System.currentTimeMillis() - Long.valueOf(requestTime);
                        RealmHelper.saveObjectToRealm(context, new ReportData(reportId,
                                totalTime / 1000));
                    } catch (NumberFormatException e) {
                    }
                }


                return response;
            }
        });

        GsonConverter gsonConverter = new GsonConverter(initGson());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        ClientUtil.updateMessage(msg);
                        //showLogMessage(context, msg);
                    }
                })
                .setClient(new OkClient(safeOkHttpClient))
                .setConverter(gsonConverter)
                .setRequestInterceptor(requestInterceptor)
                .build();

        playRetailAPI = restAdapter.create(RetrofitPlayRetailService.class);
    }
}
